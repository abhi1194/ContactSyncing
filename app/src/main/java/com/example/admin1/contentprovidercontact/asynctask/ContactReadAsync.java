package com.example.admin1.contentprovidercontact.asynctask;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.example.admin1.contentprovidercontact.datamodel.Contact;
import com.example.admin1.contentprovidercontact.interfaces.OnListReceivedListener;

import java.util.ArrayList;

/**
 * This class is used to sync the contacts of the phone in the background
 */

public class ContactReadAsync extends AsyncTask<String, Integer, ArrayList<Contact>> {

    private Context mContext;
    private ProgressDialog mProgressBar;

    public ContactReadAsync(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressBar = new ProgressDialog(mContext);
        mProgressBar.setTitle("Please Wait..");
        mProgressBar.setMessage("Contacts are Loading..");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setCancelable(false);
        mProgressBar.show();
    }

    @Override
    protected ArrayList<Contact> doInBackground(String... strings) {
        return fetchContacts();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Contact> list) {
        super.onPostExecute(list);
        OnListReceivedListener onListReceivedListener = (OnListReceivedListener) mContext;
        onListReceivedListener.onListReceived(list);
        mProgressBar.dismiss();
    }

    /**
     * Fetching contacts from ContactsContract and adding in list
     * & returning the list to doInBackground method
     */
    private ArrayList<Contact> fetchContacts() {

        String id,name,number,photo;
        ArrayList<Contact> list = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

        if (cursor != null) {
            mProgressBar.setMax(cursor.getCount());
            while (cursor.moveToNext()) {
                int i = cursor.getPosition();
                publishProgress(i);
                id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

                if ((cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?", new String[]{id}, null);
                    if (cursor1 != null) {
                        while (cursor1.moveToNext()) {
                            number = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Contact contact = new Contact(name, number, photo);
                            list.add(contact);
                        }
                        cursor1.close();
                    }
                }
            }
            cursor.close();
        }
        return list;
    }
}
