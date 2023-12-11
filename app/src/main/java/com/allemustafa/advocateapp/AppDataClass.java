package com.allemustafa.advocateapp;

import java.util.List;

public class AppDataClass {
    private final List<officialClass> officialsList;
    private final String  Address;
    public AppDataClass(List<officialClass> officialsList, String address) {
        this.officialsList = officialsList;
        Address = address;
    }

    public List<officialClass> getOfficialsList() {
        return officialsList;
    }

    public String getAddress() {
        return Address;
    }
}
