package com.kitezeng.shopping;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.kitezeng.shopping.Adapter.MainPagerAdapter;
import com.kitezeng.shopping.Fragment.FragmentHome;

public class MainActivity extends AppCompatActivity {

    private String requestPermissionName = "";
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private final int HOME = 0;
    private final int PRODUCT = 1;
    private final int FAVORITE = 2;
    private final int SHOPPING_CART = 3;
    private final int MEMBER = 4;

//    private PresenterA presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        requestPermissionName = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        requestPermissionLauncher.launch(requestPermissionName);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager2.setAdapter(mainPagerAdapter);
        viewPager2.setOffscreenPageLimit(4);
        viewPager2.setUserInputEnabled(false);



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:

                        viewPager2.setCurrentItem(HOME);

                        break;
                    case R.id.product:

                        viewPager2.setCurrentItem(PRODUCT);

                        break;
                    case R.id.favorite:

                        viewPager2.setCurrentItem(FAVORITE);

                        break;
                    case R.id.shopping_cart:

                        viewPager2.setCurrentItem(SHOPPING_CART);

                        break;
                    case R.id.member:

                        viewPager2.setCurrentItem(MEMBER);

                        break;
                }
                return true;
            }
        });

    }
    private void findView(){
        viewPager2 = findViewById(R.id.viewPager2);
        bottomNavigationView = findViewById(R.id.navigation);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        FirebaseAuth.getInstance().signOut();
//    }
private ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Toast.makeText(this, "同意授權", Toast.LENGTH_SHORT).show();
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(requestPermissionName)) {
                        //用户拒绝权限并且系统不再弹出请求权限的弹窗
                        //这时需要我们自己处理，比如自定义弹窗告知用户为何必须要申请这个权限
                        Log.e("-,-,-", "$requestPermissionName not granted and should not show rationale");
                    }
                }
            }
        });
}