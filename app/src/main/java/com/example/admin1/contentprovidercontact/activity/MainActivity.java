package com.example.admin1.contentprovidercontact.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.admin1.contentprovidercontact.R;
import com.example.admin1.contentprovidercontact.adapter.ContactAdapter;
import com.example.admin1.contentprovidercontact.asynctask.ContactReadAsync;
import com.example.admin1.contentprovidercontact.datamodel.Contact;
import com.example.admin1.contentprovidercontact.interfaces.OnAdapterItemClickListener;
import com.example.admin1.contentprovidercontact.interfaces.OnListReceivedListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnAdapterItemClickListener, OnListReceivedListener{

    private ContactAdapter adapter;
    private ArrayList<Contact> mContactList;
    private ContactReadAsync mContactReadAsync;
    private int mPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = findViewById(R.id.rv_contacts);
        mContactList = new ArrayList<>();
        // Checking Permission For Contacts
        checkPermission();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Initializing and setAdapter
        adapter = new ContactAdapter(this,mContactList,this);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Taking Runtime Permission to read contacts
     */
    private void checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},2);
            }else {
                mContactReadAsync = new ContactReadAsync(this);
                mContactReadAsync.execute();
            }
        }else {
            mContactReadAsync = new ContactReadAsync(this);
            mContactReadAsync.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==4) {
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mContactReadAsync = new ContactReadAsync(this);
                mContactReadAsync.execute();
            }else{
                Toast.makeText(this,"Permission is required",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This method is used to get the list of all the contacts of the phone
     * @param list contact list containing name,number & photoUri
     */
    @Override
    public void onListReceived(ArrayList<Contact> list) {
        mContactList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_plus:
                Intent intent = new Intent(MainActivity.this,CountDownActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method gets a callback when the row of the adapter is clicked
     * and it goes to EditProfileActivity
     * @param view adapter view
     * @param position adapter item position
     */
    @Override
    public void onAdapterItemClick(View view, int position) {
        mPosition = position;
        Contact contact = mContactList.get(position);
        Intent intent = new Intent(MainActivity.this,EditProfileActivity.class);
        intent.putExtra("photo",contact.getPhotoUri());
        intent.putExtra("name",contact.getName());
        intent.putExtra("number",contact.getNumber());
        startActivityForResult(intent,1);
    }

    /**
     * This method gets the callback when user edits its profile
     * and the contact gets updated
     * @param requestCode 1
     * @param resultCode RESULT_OK & RESULT_CANCEL
     * @param data getting updated data from EditProfileActivity.java
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK){
            String name = data.getStringExtra("name");
            String number = data.getStringExtra("phone");
            String photoUri = data.getStringExtra("image");
            if(photoUri!=null) {
                mContactList.set(mPosition,new Contact(name, number,photoUri));
            }else{
                photoUri= "NO IMAGE";
                mContactList.set(mPosition,new Contact(name,number,photoUri));
            }
            adapter.notifyItemChanged(mPosition);
        }
    }
}
