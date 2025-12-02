package com.anujtech.app.datamodel;

public class RegDataModel {
    public RegDataModel() {
    }

    private String name, dob, email, mobile, address, type;


    public RegDataModel(String name, String dob, String email, String mobile, String address, String type) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
