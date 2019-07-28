package com.prashantdhiman.chatie.models;

public class UserObject {
    private String uId;
    private String name;
    private String phone;

    public UserObject(String uId, String name, String phone) {
        this.uId = uId;
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getUId() {
        return uId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }
}
