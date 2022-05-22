package com.kitezeng.shopping.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kitezeng.shopping.Fragment.FragmentFavorite;
import com.kitezeng.shopping.Fragment.FragmentHome;
import com.kitezeng.shopping.Fragment.FragmentMemberCentre;
import com.kitezeng.shopping.Fragment.FragmentProduct;
import com.kitezeng.shopping.Fragment.FragmentShoppingCar;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList=new ArrayList<>();

    public MainPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentList.add(new FragmentHome());
        fragmentList.add(new FragmentProduct());
        fragmentList.add(new FragmentFavorite());
        fragmentList.add(new FragmentShoppingCar());
        fragmentList.add(new FragmentMemberCentre());
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
