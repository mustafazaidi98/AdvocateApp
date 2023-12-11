package com.allemustafa.advocateapp;

import java.io.Serializable;

public class officialClass implements Serializable {
    private final String Office;
    private final String name;
    private final String address;
    private final String party;
    private final String email;
    private final String photoUrl;
    private final String facebook;
    private final String youtube;
    private final String twitter;
    private final String phone;
    private final String url;

    public officialClass(String office, String name, String address, String party, String email, String photoUrl, String facebook, String youtube, String twitter, String phone, String url) {
        Office = office;
        this.name = name;
        this.address = address;
        this.party = party;
        this.email = email;
        this.photoUrl = photoUrl;
        this.facebook = facebook;
        this.youtube = youtube;
        this.twitter = twitter;
        this.phone = phone;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getParty() {
        return party;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getPhone() {
        return phone;
    }

    public String getUrl() {
        return url;
    }

    public String getOffice() {
        return Office;
    }
}
