package com.kitezeng.shopping.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kitezeng.shopping.Fragment.manager.FragmentAdd;
import com.kitezeng.shopping.Fragment.manager.FragmentDelete;
import com.kitezeng.shopping.Fragment.manager.FragmentUpdate;

import java.util.ArrayList;
import java.util.List;

public class ManagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList=new ArrayList<>();

    public ManagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentList.add(new FragmentAdd());
        fragmentList.add(new FragmentDelete());
        fragmentList.add(new FragmentUpdate());
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
