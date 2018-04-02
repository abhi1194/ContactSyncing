package com.example.admin1.contentprovidercontact.interfaces;

import com.example.admin1.contentprovidercontact.datamodel.Contact;

import java.util.ArrayList;

/**
 * Interface for getting list from ContactRead.java
 * into MainActivity.java
 */
public interface OnListReceivedListener{
    void onListReceived(ArrayList<Contact> list);
}
