package com.kitezeng.shopping.Fragment.manager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kitezeng.shopping.R;
import com.kitezeng.shopping.apiHelper.ApiHelper;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUpdate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUpdate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText update_edit_id;
    private EditText update_edit_name;
    private EditText update_edit_category;
    private EditText update_edit_imageUrl;
    private EditText update_edit_price;
    private EditText update_edit_stock;
    private Button update_request_btn;

    public FragmentUpdate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUpdate.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUpdate newInstance(String param1, String param2) {
        FragmentUpdate fragment = new FragmentUpdate();
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
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        update_edit_name = view.findViewById(R.id.update_edit_name);
        update_edit_id = view.findViewById(R.id.update_edit_id);
        update_edit_category = view.findViewById(R.id.update_edit_category);
        update_edit_imageUrl = view.findViewById(R.id.update_edit_imageUrl);
        update_edit_price = view.findViewById(R.id.update_edit_price);
        update_edit_stock = view.findViewById(R.id.update_edit_stock);
        update_request_btn = view.findViewById(R.id.update_request_btn);

        update_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(update_edit_name!=null && update_edit_category !=null &&
                        update_edit_imageUrl !=null && update_edit_price !=null
                        && update_edit_stock !=null){
                    JSONObject jsonObject = new JSONObject();
                    try{
                        jsonObject.put("productName",update_edit_name.getText().toString());
                        jsonObject.put("productId",update_edit_id.getText().toString());
                        jsonObject.put("category",update_edit_category.getText().toString());
                        jsonObject.put("imageUrl",update_edit_imageUrl.getText().toString());
                        jsonObject.put("price",update_edit_price.getText().toString());
                        jsonObject.put("stock",update_edit_stock.getText().toString());
                        ApiHelper.updateHTTPData("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products/"+update_edit_id.getText().toString(), jsonObject, new ApiHelper.Callback2() {
                            @Override
                            public void success() {
                                Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void fail(Exception exception) {
                                Toast.makeText(getContext(), "失敗", Toast.LENGTH_LONG).show();
                            }
                        });
                    }catch (Exception e){
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