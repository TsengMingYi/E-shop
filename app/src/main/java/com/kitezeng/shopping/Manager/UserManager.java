package com.kitezeng.shopping.Manager;

import android.util.Log;

import com.kitezeng.shopping.Model.Page;
import com.kitezeng.shopping.Model.Product;
import com.kitezeng.shopping.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserManager {
    private UserManager(){ }
    private static UserManager instance;
    public static UserManager getInstance(){
        if(instance == null){
            instance = new UserManager();
        }
        return instance;
    }
    public User loadUserDataFromRawData(String rawData){
        Log.e("response",rawData);
        User user = new User();
        try {
            JSONObject jsonObject = new JSONObject(rawData);
            Integer userId = jsonObject.getInt("userId");
            String createdDate = jsonObject.getString("createdDate");
            String lastModifiedDate = jsonObject.getString("lastModifiedDate");
            String e_mail = jsonObject.getString("e_mail");
            user.setUserId(userId);
            user.setEmail(e_mail);
            user.setCreatedDate(createdDate);
            user.setLastModifiedDate(lastModifiedDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
