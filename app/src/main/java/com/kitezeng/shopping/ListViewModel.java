package com.kitezeng.shopping;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.kitezeng.shopping.Model.User;

public class ListViewModel extends ViewModel {
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isTure = new MutableLiveData<>();

    public void setUserMutableLiveData(User user){
        userMutableLiveData.setValue(user);
    }
    public LiveData<User> getUserMutableLiveData(){
        return userMutableLiveData;
    }
    public void setIsTrue(boolean is){
        isTure.setValue(is);
    }
    public LiveData<Boolean> getIsTrue(){

        return isTure;
    }
}
