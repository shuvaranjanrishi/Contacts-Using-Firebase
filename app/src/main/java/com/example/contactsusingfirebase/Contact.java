package com.example.contactsusingfirebase;

public class Contact {

    private String contactId;
    private String name;
    private String phoneNo;

    public Contact() {

    }

    public Contact(String contactId, String name, String phoneNo) {
        this.contactId = contactId;
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public Contact(String name, String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public String getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
