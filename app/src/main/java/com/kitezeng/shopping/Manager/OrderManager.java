package com.kitezeng.shopping.Manager;

import android.util.Log;

import com.kitezeng.shopping.Model.Order;
import com.kitezeng.shopping.Model.OrderItem;
import com.kitezeng.shopping.Model.User;
import com.kitezeng.shopping.apiHelper.ApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private OrderManager(){ }
    private static OrderManager instance;
    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private Order order = new Order();
    private ArrayList<OrderItem> orderItems = new ArrayList<>();

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static OrderManager getInstance(){
        if(instance == null){
            instance = new OrderManager();
        }
        return instance;
    }

    public void syncDataFromRemote(String apiUrl,Runnable doSomething){
        ApiHelper.requestApi(apiUrl, null, new ApiHelper.Callback() {
            @Override
            public void success(String rawString) {
                OrderManager.getInstance().loadOrderDataFromRawData(rawString);
                if (doSomething != null) {
                    doSomething.run();
                }
            }

            @Override
            public void fail(Exception exception) {

            }
        });
    }

    public void loadOrderDataFromRawData(String rawData){
        Log.e("response",rawData);
        order = new Order();
        orderItems.clear();
        orderArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(rawData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                order.setOrderId(jsonObject1.getInt("orderId"));
                order.setUserId(jsonObject1.getInt("userId"));
                order.setTotalAmount(jsonObject1.getInt("totalAmount"));
                order.setCreatedDate(jsonObject1.getString("createdDate"));
                order.setLastModifiedDate(jsonObject1.getString("lastModifiedDate"));
                JSONArray jsonArray1 = jsonObject1.getJSONArray("orderItemList");
                for(int j = 0;j<jsonArray1.length();j++){
                    OrderItem orderItem = new OrderItem();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    orderItem.setOrderItemId(jsonObject2.getInt("orderItemId"));
                    orderItem.setOrderId(jsonObject2.getInt("orderId"));
                    orderItem.setProductId(jsonObject2.getInt("productId"));
                    orderItem.setQuantity(jsonObject2.getInt("quantity"));
                    orderItem.setAmount(jsonObject2.getInt("amount"));
                    orderItem.setProductName(jsonObject2.getString("productName"));
                    orderItem.setImageUrl(jsonObject2.getString("imageUrl"));
                    orderItems.add(orderItem);
                }
                order.setOrderItemList(orderItems);//如果是多筆資料要用list
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("order",order.getOrderId()+"");
    }
}
