package com.ruparts;

import static com.ruparts.TasksActivity.fragmentPagerAdapter;
import static com.ruparts.TasksActivity.taskRepository;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ruparts.context.task.model.TaskFilter;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.context.task.model.TaskStatusEnum;
import com.ruparts.context.task.service.TaskRepository;
import com.ruparts.main.Container;

import java.util.ArrayList;
import java.util.List;

public class TasksInProgressFragment extends Fragment {

    private Context context;
    private ExpandableListView list_view;
    private ArrayList<ExpListGroup> filtered = new ArrayList<>();
    ExpandableListAdapter adapter;

    private SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = this.getActivity();
        return inflater.inflate(R.layout.fragment_tasks_in_progress, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        MenuItem menuItem = menu.findItem(R.id.search_bar);
        searchView = (SearchView) menuItem.getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        assert searchView != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Поиск");
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.filterData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filterData(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void loadListView() {
        list_view.setGroupIndicator(null);
        list_view.setChildIndicator(null);
        list_view.setChildDivider(getResources().getDrawable(R.color.based_background));
        list_view.setDivider(getResources().getDrawable(R.color.based_background));
        list_view.setDividerHeight(20);
        filtered = fragmentPagerAdapter.createListData(TaskStatusEnum.IN_PROGRESS);
        adapter = new ExpandableListAdapter(context, filtered);
        list_view.setAdapter(adapter);
        list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int parentPosition, int childPosition, long l) {

                ExpListGroup elg = filtered.get(parentPosition);
                TaskObject tbo = elg.itemsList.get(childPosition);

                Intent intent = new Intent(context, TasksStructure.class);
                intent.putExtra(TaskObject.class.getSimpleName(), tbo);
                startActivity(intent);

                return false;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_view = (ExpandableListView) view.findViewById(R.id.tasks_exp_list_view_in_progress);
        this.loadListView();
    }


}