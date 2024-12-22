package com.example.navigationdrawer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TasksViewPager2Adapter extends FragmentStateAdapter {

    public TasksViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new TasksToDo();
            case 1: return new TasksInProgress();
            case 2: return new TasksCompleted();
            case 3: return new TasksCancelled();
            case 4: return new TasksAll();
            default: return new TasksToDo();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
