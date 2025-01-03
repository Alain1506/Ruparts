package com.ruparts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TasksViewPager2Adapter extends FragmentStateAdapter {

    private Fragment[] fragments = new Fragment[5];

    public TasksViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = doCreateFragment(position);
        this.fragments[position] = fragment;
        return fragment;
    }

    public Fragment getFragmentByPosition(int position) {
        return this.fragments[position];
    }

    private Fragment doCreateFragment(int position){
        switch (position) {
            case 1: return new TasksInProgressFragment();
            case 2: return new TasksCompletedFragment();
            case 3: return new TasksCancelledFragment();
            case 4: return new TasksAllFragment();
            default: return new TasksToDoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return this.fragments.length;
    }
}
