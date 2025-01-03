package com.example.navigationdrawer;

import static com.example.navigationdrawer.TasksActivity.expListContents;

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

import com.example.navigationdrawer.helperclasses.TaskObject;

import java.util.ArrayList;

public class TasksToDoFragment extends Fragment {

    private Context context;
    private ExpandableListView listView;
    private ArrayList<ExpListGroup> filteredContents = new ArrayList<>();
    private ExpandableListAdapter adapter;
    private SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = this.getActivity();
        return inflater.inflate(R.layout.fragment_tasks_to_do, container, false);
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
//        searchView.setIconifiedByDefault(true);

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
        listView.setGroupIndicator(null);
        listView.setChildIndicator(null);
        listView.setChildDivider(context.getDrawable(R.color.based_background));
        listView.setDivider(context.getDrawable(R.color.based_background));
        listView.setDividerHeight(20);
        createListData("to_do");
        adapter = new ExpandableListAdapter(context, filteredContents);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int parentPosition, int childPosition, long l) {

                ExpListGroup elg = filteredContents.get(parentPosition);
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
        listView = (ExpandableListView) view.findViewById(R.id.tasks_exp_list_view_To_Do);
        this.loadListView();
//        list_view.setGroupIndicator(null);
//        list_view.setChildIndicator(null);
//        list_view.setChildDivider(getResources().getDrawable(R.color.based_background));
//        list_view.setDivider(getResources().getDrawable(R.color.based_background));
//        list_view.setDividerHeight(20);
//        createListData("to_do");
//        adapter = new ExpandableListAdapter(context, filtered);
//        list_view.setAdapter(adapter);
//
//        list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//        public boolean onChildClick(ExpandableListView expandableListView, View view, int parentPosition, int childPosition, long l) {
//
//            ExpListGroup elg = filtered.get(parentPosition);
//            TaskBodyObject tbo = elg.items.get(childPosition);
//
//            Intent intent = new Intent(context, TasksStructure.class);
//            intent.putExtra(TaskBodyObject.class.getSimpleName(), tbo);
//            startActivity(intent);
//
//            return false;
//        }
//        });
    }

    private void createListData(String status) {

        filteredContents.clear();

        for (ExpListGroup elg : expListContents) {
            ArrayList<TaskObject> childList = elg.itemsList;
            ArrayList<TaskObject> newList = new ArrayList<>();

            for (TaskObject task : childList) {
                if (task.taskStatus.equals(status)) {
                    newList.add(task);
                }
            }

            if (!newList.isEmpty()) {
                String name = elg.elgTaskTypeToShow.substring(0, elg.elgTaskTypeToShow.length() - 3);
                ExpListGroup newExpListGroup = new ExpListGroup(name);
                newExpListGroup.itemsList = newList;
                newExpListGroup.elgTaskTypeToShow = name + " (" + newExpListGroup.itemsList.size() + ")";
                filteredContents.add(newExpListGroup);
            }
        }

    }




}