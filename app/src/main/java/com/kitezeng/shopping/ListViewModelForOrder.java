package com.kitezeng.shopping;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kitezeng.shopping.Model.Order;
import com.kitezeng.shopping.Model.OrderItem;
import com.kitezeng.shopping.Model.User;

import java.util.ArrayList;


public class ListViewModelForOrder extends ViewModel {
    private final MutableLiveData<Order> userMutableLiveData = new MutableLiveData<>();

    public void setOrderMutableLiveData(Order orderItem){
        userMutableLiveData.setValue(orderItem);
    }
    public LiveData<Order> getOrderMutableLiveData(){
        return userMutableLiveData;
    }
}
