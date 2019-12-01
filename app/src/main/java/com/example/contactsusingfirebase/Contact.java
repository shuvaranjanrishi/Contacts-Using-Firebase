package com.example.contactsusingfirebase;

public class Contact {

    private String contactId;
    private String contactImage;
    private String name;
    private String phoneNo;

    public Contact() {

    }

    public Contact(String contactId, String contactImage, String name, String phoneNo) {
        this.contactId = contactId;
        this.contactImage = contactImage;
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public String getContactId() {
        return contactId;
    }

    public String getContactImage() {
        return contactImage;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
