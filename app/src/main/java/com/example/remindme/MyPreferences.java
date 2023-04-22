package com.example.remindme;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Shared preference to store user data
 */
public class MyPreferences {

    private static MyPreferences myPreferences;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    // clear preference
    public void clear(){
        editor.clear();
    }

    public static MyPreferences getPreferences(Context context) {
        if (myPreferences == null) myPreferences = new MyPreferences(context);
        return myPreferences;
    }

    public void setUserName(String userName){
        editor.putString(Config.USER_NAME, userName);
        editor.apply();
    }

    public String getUserName(){
        return sharedPreferences.getString(Config.USER_NAME, "");
    }

    public void setEmail(String email){
        editor.putString(Config.EMAIL, email);
        editor.apply();
    }

    public String getEmail(){
        return sharedPreferences.getString(Config.EMAIL, "");
    }

    public void setMobileNumber(String mobileNumber){
        editor.putString(Config.MOBILE, mobileNumber);
        editor.apply();
    }

    public String getMobileNumber(){
        return sharedPreferences.getString(Config.MOBILE, "");
    }

    public void setImage(String image){
        editor.putString(Config.IMAGE, image);
        editor.apply();
    }

    public String getImage(){
        return sharedPreferences.getString(Config.IMAGE, "");
    }

    public void setAge(String age){
        editor.putString(Config.AGE, age);
        editor.apply();
    }

    public String getAge(){
        return sharedPreferences.getString(Config.AGE, ""); //if user's age not found then it'll return -1
    }

    public void setIsNotification(boolean isNotification){
        editor.putBoolean(Config.IS_NOTIFICATION_ON, isNotification);
        editor.apply();
    }

    public boolean isNotification(){
        return sharedPreferences.getBoolean(Config.IS_NOTIFICATION_ON, false); //assume the default value is false
    }

}
