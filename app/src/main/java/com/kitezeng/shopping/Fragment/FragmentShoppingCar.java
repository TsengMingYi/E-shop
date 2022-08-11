package com.kitezeng.shopping.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kitezeng.shopping.Adapter.OrderAdapter;
import com.kitezeng.shopping.ListViewModel;
import com.kitezeng.shopping.ListViewModelForOrder;
import com.kitezeng.shopping.Login;
import com.kitezeng.shopping.Manager.OrderManager;
import com.kitezeng.shopping.Model.Order;
import com.kitezeng.shopping.Model.OrderItem;
import com.kitezeng.shopping.Model.User;
import com.kitezeng.shopping.R;
import com.kitezeng.shopping.apiHelper.ApiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentShoppingCar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentShoppingCar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textView1;
    private User user;
    private FirebaseAuth mAuth;
    private TextView total_amount;
    private int number = 0;
    private int userId;
    private ListViewModelForOrder modelForOrder;
    private OrderAdapter orderAdapter = new OrderAdapter(FragmentShoppingCar.this);
    private RecyclerView order_recycleView;

    public FragmentShoppingCar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankShoppingCar.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentShoppingCar newInstance(String param1, String param2) {
        FragmentShoppingCar fragment = new FragmentShoppingCar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("testShopping", "testShopping");

        View view = inflater.inflate(R.layout.fragment_shopping_car, container, false);
        textView1 = view.findViewById(R.id.textView_shopping_car);
        order_recycleView = view.findViewById(R.id.order_recycleView);
        total_amount = view.findViewById(R.id.total_amount);
        modelForOrder = new ViewModelProvider(requireActivity()).get(ListViewModelForOrder.class);
        if (user == null) {
            textView1.setText("這是購物車");
//            startActivity(new Intent(FragmentShoppingCar.this.getActivity(), Login.class));
        }
//        else{
//            user = mAuth.getCurrentUser();
//            textView1.setText(user.getEmail()+" 成功登入");
//        }
//
//
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(false);
        order_recycleView.setLayoutManager(linearLayoutManager);
        order_recycleView.setAdapter(orderAdapter);

        ListViewModel model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        ListViewModelForOrder modelForOrder = new ViewModelProvider(requireActivity()).get(ListViewModelForOrder.class);
        model.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User firebaseUser) {
                textView1.setText(firebaseUser.getEmail() + " 成功登入");
                userId = firebaseUser.getUserId();
                OrderManager.getInstance().syncDataFromRemote("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/users/"+firebaseUser.getUserId()+"/orders", new Runnable() {
                    @Override
                    public void run() {
//                        Order order = OrderManager.getInstance().getOrder();
                        ArrayList<OrderItem> orderItems = OrderManager.getInstance().getOrderItems();
                        Order order = OrderManager.getInstance().getOrder();
                        for(int i = 0;i<orderItems.size();i++){
                            number+= orderItems.get(i).getAmount();
                            orderItems.get(i).setUserId(firebaseUser.getUserId());
                        }
                        total_amount.setText("商品總金額 $"+number+"");
                        orderAdapter.refreshUI(orderItems);
                    }
                });
//                Log.e("ShoppingCarData",firebaseUser.getEmail());
            }
        });

        modelForOrder.getOrderMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                number = 0;
                for(int i = 0;i<order.getOrderItemList().size();i++){
                    number+= order.getOrderItemList().get(i).getAmount();

                }
                orderAdapter.refreshUI(order.getOrderItemList());
                total_amount.setText("商品總金額 $"+number+"");

            }
        });
//
        model.getIsTrue().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    number = 0;
//                    startActivity(new Intent(FragmentShoppingCar.this.getActivity(), Login.class));
                    textView1.setText("這是購物車");
                    orderAdapter.clearUI();
                    total_amount.setText("");
                }
            }
        });

        return view;
    }

    public void hello2(ArrayList<OrderItem> orderItems){
        number = 0;
        for(int i = 0;i<orderItems.size();i++){
            number += orderItems.get(i).getAmount();
        }
        total_amount.setText("商品總金額 $"+number+"");
        orderAdapter.refreshUI(orderItems);
    }
//    public void hello2(){
//        OrderManager.getInstance().syncDataFromRemote("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/users/" + userId + "/orders", new Runnable() {
//            @Override
//            public void run() {
////                        Order order = OrderManager.getInstance().getOrder();
//                ArrayList<OrderItem> orderItems = OrderManager.getInstance().getOrderItems();
//                Order order = OrderManager.getInstance().getOrder();
//                for(int i = 0;i<order.getOrderItemList().size();i++){
//                    order.getOrderItemList().get(i).setUserId(userId);
//                }
//                number = 0;
//                for(int i = 0;i<order.getOrderItemList().size();i++){
//                    number+= order.getOrderItemList().get(i).getAmount();
//
//                }
//                orderAdapter.refreshUI(order.getOrderItemList());
//                total_amount.setText("商品總金額 $"+number+"");
//            }
//        });
//    }
}