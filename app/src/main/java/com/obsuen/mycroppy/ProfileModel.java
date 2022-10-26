package com.obsuen.mycroppy;

import io.realm.RealmObject;

public class ProfileModel extends RealmObject {
    String profile_image,name,email,address,phone;
    public ProfileModel(String profile_image,String name,String email, String address,String phone){
        this.profile_image = profile_image;
        this.name = name;
        this.email = email;
        this.address = address;
    }
    public ProfileModel(){

    };

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
