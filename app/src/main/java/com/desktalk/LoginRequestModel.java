package com.desktalk;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anshikas on 25-01-2017.
 *
 * LoginRequestModel is having  the getter and setter method of login request.
 */

public class LoginRequestModel {


    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String password;


}
