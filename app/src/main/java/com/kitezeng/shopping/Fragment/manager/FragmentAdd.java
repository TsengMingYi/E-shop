package com.kitezeng.shopping.Fragment.manager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitezeng.shopping.Model.Product;
import com.kitezeng.shopping.R;
import com.kitezeng.shopping.apiHelper.ApiHelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdd extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText edit_name;
    private EditText edit_category;
    private EditText edit_imageUrl;
    private EditText edit_price;
    private EditText edit_stock;
    private Button request_btn;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAdd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentManager.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAdd newInstance(String param1, String param2) {
        FragmentAdd fragment = new FragmentAdd();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        edit_name = view.findViewById(R.id.edit_name);
        edit_category = view.findViewById(R.id.edit_category);
        edit_imageUrl = view.findViewById(R.id.edit_imageUrl);
        edit_price = view.findViewById(R.id.edit_price);
        edit_stock = view.findViewById(R.id.edit_stock);
        request_btn = view.findViewById(R.id.request_btn);


        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_name!=null && edit_category !=null &&
                        edit_imageUrl !=null && edit_price !=null
                        && edit_stock !=null){

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("productName",edit_name.getText().toString());
                        jsonObject.put("category",edit_category.getText().toString());
                        jsonObject.put("imageUrl",edit_imageUrl.getText().toString());
                        jsonObject.put("price",edit_price.getText().toString());
                        jsonObject.put("stock",edit_stock.getText().toString());
                        ApiHelper.sendHTTPData("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", jsonObject, new ApiHelper.Callback2() {
                            @Override
                            public void success() {
                                Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void fail(Exception exception) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "不可為空值", Toast.LENGTH_LONG).show();
                }
            }
        });



        return view;
    }
}