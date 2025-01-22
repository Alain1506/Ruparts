package com.ruparts;

import static com.ruparts.TasksActivity.taskRepository;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ruparts.context.task.model.TaskFilter;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.context.task.model.TaskStatusEnum;

import java.util.ArrayList;
import java.util.List;

public class TasksViewPager2Adapter extends FragmentStateAdapter {

    private ArrayList<ExpListGroup> filtered = new ArrayList<>();

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

    private Fragment doCreateFragment(int position) {
        switch (position) {
            case 1:
                return new TasksInProgressFragment();
            case 2:
                return new TasksCompletedFragment();
            case 3:
                return new TasksCancelledFragment();
            case 4:
                return new TasksAllFragment();
            default:
                return new TasksToDoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return this.fragments.length;
    }


    public ArrayList<ExpListGroup> createListData(TaskStatusEnum status) {

        filtered.clear();

        TaskFilter taskFilter = new TaskFilter();
        taskFilter.status = status;
        List<TaskObject> list = taskRepository.getByFilter(taskFilter);

        if (list.size() != 0) {
            for (ExpListGroup elg : TasksActivity.expListContents) {
                ArrayList<TaskObject> newList = new ArrayList<>();

                for (TaskObject task : list) {
                    if (task.taskType.equals(elg.elgTaskType)) {
                        newList.add(task);
                    }
                }

                if (!newList.isEmpty()) {
                    String name = elg.elgTaskTypeToShow.substring(0, elg.elgTaskTypeToShow.length() - 3);
                    ExpListGroup newExpListGroup = new ExpListGroup(name);
                    newExpListGroup.itemsList = newList;
                    newExpListGroup.elgTaskTypeToShow = name + " (" + newExpListGroup.itemsList.size() + ")";
                    filtered.add(newExpListGroup);
                }
            }
        }
        return filtered;
    }


}
