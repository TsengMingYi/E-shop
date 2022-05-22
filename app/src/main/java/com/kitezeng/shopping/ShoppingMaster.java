package com.kitezeng.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.kitezeng.shopping.Adapter.ManagerAdapter;

public class ShoppingMaster extends AppCompatActivity {

    private ViewPager2 viewPager_manager;
    private NavigationView navigation_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_master);

        findView();

        ManagerAdapter managerAdapter = new ManagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager_manager.setAdapter(managerAdapter);
        viewPager_manager.setOffscreenPageLimit(3);
        viewPager_manager.setUserInputEnabled(false);

        navigation_manager.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add:

                        viewPager_manager.setCurrentItem(0);

                        break;

                    case R.id.delete:

                        viewPager_manager.setCurrentItem(1);

                        break;

                    case R.id.update:

                        viewPager_manager.setCurrentItem(2);

                        break;
                }
                return true;
            }
        });

    }

    private void findView(){
        viewPager_manager = findViewById(R.id.viewPager_manager);
        navigation_manager = findViewById(R.id.navigation_manager);
    }
}