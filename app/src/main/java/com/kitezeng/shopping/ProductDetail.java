package com.kitezeng.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kitezeng.shopping.Model.Product;
import com.kitezeng.shopping.apiHelper.ApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetail extends AppCompatActivity {

    private ImageView product_image_detail;
    private ImageView subtraction;
    private TextView product_number;
    private TextView product_price_detail;
    private TextView product_decrption_detail;
    private TextView product_name_detail;
    private TextView last_stock;
    private ImageView plus;
    private Button add_shopping_car_btn;
    private TextView user_id;
    private int number = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        findView();
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product_detail");
        Glide.with(ProductDetail.this).load(product.getImageUrl()).centerCrop().into(product_image_detail);
        product_price_detail.setText("$"+product.getPrice());
        last_stock.setText(product.getStock()+"");
        if(product.getDescription().contains("null")){
            product_decrption_detail.setText("");
        }else{
            product_decrption_detail.setText(product.getDescription());
        }
        product_name_detail.setText(product.getProductName());

        user_id.setText(product.getUserId()+"");

        if(product.getStock() == 1 ){
            subtraction.setEnabled(false);
            subtraction.setAlpha(0.1f);
            plus.setEnabled(false);
            plus.setAlpha(0.1f);
        }else if(number >=1 && product.getStock() > 1){
            subtraction.setEnabled(false);
            subtraction.setAlpha(0.1f);
            plus.setEnabled(true);
            plus.setAlpha(1f);
        }else if(product.getStock() == 0){
            subtraction.setEnabled(false);
            subtraction.setAlpha(0.1f);
            plus.setEnabled(false);
            plus.setAlpha(0.1f);
        }

//        product_stock_detail.setText("剩餘："+product.getStock());

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number++;
                if(product.getStock() >= 1 && number < product.getStock()){
                    subtraction.setEnabled(true);
                    subtraction.setAlpha(1f);
                }else if(number == product.getStock()){
                    plus.setEnabled(false);
                    plus.setAlpha(0.1f);
                    subtraction.setEnabled(true);
                    subtraction.setAlpha(1f);
                    Toast.makeText(ProductDetail.this, "已達庫存上限", Toast.LENGTH_SHORT).show();
                }else if(number > product.getStock()){
                    subtraction.setEnabled(true);
                    subtraction.setAlpha(1f);
                }

                    product_number.setText(number+"");
            }
        });
        subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number--;
                if(number <=1){
                    subtraction.setEnabled(false);
                    subtraction.setAlpha(0.1f);
                    plus.setEnabled(true);
                    plus.setAlpha(1f);
                }else if(number >=1 && number < product.getStock()){
//                    number++ ;
                    plus.setAlpha(1f);
                    plus.setEnabled(true);
                    subtraction.setAlpha(1f);
                }
                product_number.setText(number+"");
            }
        });

            add_shopping_car_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(product.getUserId() == 0){
                        Toast.makeText(v.getContext(), "請先登入", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(product.getStock() <= 0){
                        Toast.makeText(v.getContext(), "已售完", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    if((product.getStock() - number) == 0){
//                        plus.setEnabled(false);
//                        plus.setAlpha(0.1f);
//                        subtraction.setEnabled(false);
//                        subtraction.setAlpha(0.1f);
//                        Toast.makeText(ProductDetail.this, "已售完", Toast.LENGTH_SHORT).show();
//                        return;
//                }else
                        if(number > product.getStock()){
                        Toast.makeText(ProductDetail.this, "庫存不足", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    try {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("productId",product.getProductId());
                        jsonObject1.put("quantity",number);
                        jsonArray.put(0,jsonObject1);
                        jsonObject.put("buyItemList",jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ApiHelper.sendHTTPData("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/users/" + product.getUserId() + "/orders", jsonObject, 201, new ApiHelper.Callback5() {
                        @Override
                        public void success(String rawString) {
                            product.setStock(product.getStock() - number);
                            Toast.makeText(ProductDetail.this, "購物車添加成功", Toast.LENGTH_SHORT).show();
                            last_stock.setText(product.getStock()+"");
                            Intent intent = new Intent();
                            setResult(RESULT_FIRST_USER,intent);
                        }

                        @Override
                        public void fail(Exception exception) {
                            Toast.makeText(ProductDetail.this, "購物車添加失敗", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
    }
    private void findView(){
        product_image_detail = findViewById(R.id.product_image_detail);
        subtraction = findViewById(R.id.subtraction);
        product_number = findViewById(R.id.product_number);
        product_price_detail = findViewById(R.id.product_price_detail);
        product_decrption_detail = findViewById(R.id.product_decrption_detail);
        product_name_detail = findViewById(R.id.product_name_detail);
        add_shopping_car_btn = findViewById(R.id.add_shopping_car_btn);
        user_id = findViewById(R.id.user_id);
        plus = findViewById(R.id.plus);
        last_stock = findViewById(R.id.last_stock);
    }
}