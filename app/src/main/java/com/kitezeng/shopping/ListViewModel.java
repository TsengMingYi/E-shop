package com.kitezeng.shopping;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class ListViewModel extends ViewModel {
    private final MutableLiveData<FirebaseUser> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isTure = new MutableLiveData<>();

    public void setUserMutableLiveData(FirebaseUser user){
        userMutableLiveData.setValue(user);
    }
    public LiveData<FirebaseUser> getUserMutableLiveData(){
        return userMutableLiveData;
    }
    public void setIsTrue(boolean is){
        isTure.setValue(is);
    }
    public LiveData<Boolean> getIsTrue(){

        return isTure;
    }
}
