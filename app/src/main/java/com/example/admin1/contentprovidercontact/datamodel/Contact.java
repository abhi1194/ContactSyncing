package com.example.admin1.contentprovidercontact.datamodel;

/**
 * Getter & Setter class for Contacts Information
 *
 */
public class Contact {
    private String mName;
    private String mNumber;
    private String mPhotoUri;

    public Contact(String mName, String mNumber, String mPhotoUri) {
        this.mName = mName;
        this.mNumber = mNumber;
        this.mPhotoUri = mPhotoUri;
    }

    public String getName() {
        return mName;
    }

    public String getNumber() {
        return mNumber;
    }

    public String getPhotoUri() {
        return mPhotoUri;
    }

}