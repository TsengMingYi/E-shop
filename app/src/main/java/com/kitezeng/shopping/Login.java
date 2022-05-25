package com.kitezeng.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private ImageView back_image;
    private EditText edit_member;
    private EditText edit_member_password;
    private Button login_btn;
    private TextView register_textView;
    private FirebaseAuth mAuth;


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
                    login(member , password);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void findView(){
        edit_member = findViewById(R.id.edit_member);
        login_btn = findViewById(R.id.login_btn);
        edit_member_password = findViewById(R.id.edit_member_password);
        back_image = findViewById(R.id.back_image);
        register_textView = findViewById(R.id.register_textView);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_member.setText("");
                edit_member_password.setText("");
                startActivity(new Intent(Login.this,Register.class));
            }
        });
    }
    private void login(String email , String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user.getEmail().contains("www.mingerzeng@gmail.com")){
                        startActivity(new Intent(Login.this,ShoppingMaster.class));
                    }
                    Intent intent = new Intent();
                    intent.putExtra("data",user);
                    setResult(RESULT_OK,intent);
                    finish();
                    Toast.makeText(Login.this, "登入成功", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Login.this, "登入失敗", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}