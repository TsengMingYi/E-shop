package com.kitezeng.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kitezeng.shopping.Fragment.FragmentMemberCentre;

public class Register extends AppCompatActivity {

    private ImageView back_image2;
    private EditText register_account_number;
    private EditText register_password;
    private FirebaseAuth mAuth;
    private Button register_btn;
    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        findView();
        register();
    }
    private void findView(){
        back_image2 = findViewById(R.id.back_image2);
        register_account_number = findViewById(R.id.register_account_number);
        register_password = findViewById(R.id.register_password);
        register_btn = findViewById(R.id.register_btn);
        back_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void register(){
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = register_account_number.getText().toString();
                password = register_password.getText().toString();

                if(email != null && password != null){
                    firebaseAuth(email,password );
                }else{
                    Toast.makeText(Register.this, "Sorry", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void firebaseAuth(String email , String password ){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(Register.this, "註冊成功", Toast.LENGTH_LONG).show();
                    finish();



                }else{
                    Toast.makeText(Register.this, "註冊失敗", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}