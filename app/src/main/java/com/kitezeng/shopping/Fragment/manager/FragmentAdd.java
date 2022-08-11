package com.kitezeng.shopping.Fragment.manager;

import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitezeng.shopping.Callback4;
import com.kitezeng.shopping.Manager.PickSinglePhotoContract;
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
    private Button button_selectpic;
    private ImageView imageView_pic;
    private EditText edit_price;
    private EditText edit_stock;
    private Button request_btn;
    private String imagepath = "";


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
        imageView_pic = view.findViewById(R.id.imageView_pic);
        button_selectpic = view.findViewById(R.id.button_selectpic);
        edit_price = view.findViewById(R.id.edit_price);
        edit_stock = view.findViewById(R.id.edit_stock);
        request_btn = view.findViewById(R.id.request_btn);


        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagepath.length() == 0) {
                    Toast.makeText(getContext(), "請先選擇相片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edit_name!=null && edit_category !=null && edit_price !=null
                        && edit_stock !=null){

//                    JSONObject jsonObject = new JSONObject();
                    Map<String,Object> map = new HashMap<>();
                    try {
                        map.put("productName",edit_name.getText().toString());
                        map.put("category",edit_category.getText().toString());
                        map.put("price",edit_price.getText().toString());
                        map.put("stock",edit_stock.getText().toString());
                        ApiHelper.sendHttp(imagepath, map, new Callback4() {
                            @Override
                            public void success(Product product) {
                                Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void fail(Throwable e) {
                                Toast.makeText(getContext(), "失敗", Toast.LENGTH_LONG).show();
                            }
                        });
//                        jsonObject.put("productName",edit_name.getText().toString());
//                        jsonObject.put("category",edit_category.getText().toString());
////                        jsonObject.put("imageUrl",edit_imageUrl.getText().toString());
//                        jsonObject.put("price",edit_price.getText().toString());
//                        jsonObject.put("stock",edit_stock.getText().toString());
//                        ApiHelper.sendHTTPData("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", jsonObject, new ApiHelper.Callback2() {
//                            @Override
//                            public void success() {
//                                Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void fail(Exception exception) {
//
//                            }
//                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "不可為空值", Toast.LENGTH_LONG).show();
                }
            }
        });

        button_selectpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch(null);
            }
        });

        return view;
    }
    private ActivityResultLauncher<String> mGetContent = registerForActivityResult(new PickSinglePhotoContract(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            imageView_pic.setImageURI(result);
            //獲取系統返回的照片的Uri
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(result,
                    filePathColumn, null, null, null);//從系統表中查詢指定Uri對應的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagepath = cursor.getString(columnIndex);  //獲取照片路徑
            cursor = null;
//            messageText.setText(imagepath);
        }
    });
}