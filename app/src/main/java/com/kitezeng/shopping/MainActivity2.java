package com.kitezeng.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findView();
        Intent intent = getIntent();
//        intent.getIntExtra("key1",0);


//        Intent g = new Intent(this, ABC.class);
//        Bundle b = new Bundle();
//        b.putInt("key1",3);
//        b.putDouble("key2",3.5);
//        g.putExtras(b);
//        startActivity(g);


//        Bundle bundle = intent.getExtras();
        if (intent != null) {
//            Bitmap bitmap=intent.getParcelableExtra("Image");
            imageView.setImageResource(intent.getIntExtra("Image", 0));
        }
    }

//    private Handler handler = new Handler(Looper.getMainLooper());
////    HandlerThread;
//    private void findView(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        lksdjldshnjkdsh;
//                    }
//                });
//            }
//        }).start();
//
//        v = {
//            a1:2,
//            a2:3,
//            a3:{
//                b1:"hello",
//                b2:3.1
//            }
//        };
//       if(v["a3"]["b2"] != null) {
//
//       }


    private void findView() {

        imageView = findViewById(R.id.intent_image);
    }
}