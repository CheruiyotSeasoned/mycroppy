package com.obsuen.mycroppy;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class for Shared Preference
 */
public class PrefManager {

    Context context;

    PrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(String email, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.commit();
    }
    public void saveProfileDetails(String name,String email,String Address,String phone,String imageDownloadUrl){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ProfileDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name",name);
        editor.putString("Address",Address);
        editor.putString("Phone",phone);
        editor.putString("Email",email);
        editor.putString("imageurl",imageDownloadUrl);
        editor.apply();
    }
    public String getName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ProfileDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Name", "");
    }
    public String getAddress() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ProfileDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Address", "");
    }
    public String getPhone() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("ProfileDetails", Context.MODE_PRIVATE);
            return sharedPreferences.getString("Phone", "");
        }
    public String getImageUrl() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ProfileDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("imageurl", "");
    }

    public String getEmailProfile() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ProfileDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email", "");
    }
    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email", "");
    }

    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("Email", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }
}
