package com.kitezeng.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private ImageView back_image;
    private EditText edit_member;
    private EditText edit_member_password;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String member = edit_member.getText().toString();
                    String password = edit_member_password.getText().toString();
                    Log.e("member:",member);
                    Log.e("password:",password);
                    if(member != null && password != null){
                        if(member.equals("kitezeng") && password.equals("123456zx")){
                            startActivity(new Intent(Login.this, ShoppingMaster.class));
                        }
                    }else {
                    Toast.makeText(Login.this, "不可為空值", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void findView(){
        edit_member = findViewById(R.id.edit_member);
        login_btn = findViewById(R.id.login_btn);
        edit_member_password = findViewById(R.id.edit_member_password);
        back_image = findViewById(R.id.back_image);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}