package com.projeto2.env_station_app.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.projeto2.env_station_app.Dashboard;
import com.projeto2.env_station_app.MainActivity;
import com.projeto2.env_station_app.Model.User;

import java.util.Arrays;
import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int Private_mode = 0;
    private static final  String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";
    public static final String KEY_DEVICE = "device";

    public SessionManager (Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Private_mode);
        editor = pref.edit();
    }

    // Create login sesson for our app
    public void createLoginSession(User user){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_DEVICE, user.getDevice()[0]);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

//    public void checkLogin(){
//        if(!this.isLoggedIn()){
//            Intent i = new Intent(_context, Dashboard.class);
//            i.putExtra("user", user);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            _context.startActivity(i);
//        }
//    }

    public User checkLogin() {
        if (!isLoggedIn()) {
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

        // Retrieve the stored user details
        String id = pref.getString(KEY_ID, null);
        String email = pref.getString(KEY_EMAIL, null);
        String password = pref.getString(KEY_PASSWORD, null);
        String name = pref.getString(KEY_NAME, null);
        String device = pref.getString(KEY_DEVICE, null);

        // Create a new User object with the retrieved details
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        String [] listDevice = {device};
        user.setDevice(Arrays.asList(listDevice));

        return user;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String > user = new HashMap<String, String>();

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }

    public void logoutUser(Context _context){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
}
