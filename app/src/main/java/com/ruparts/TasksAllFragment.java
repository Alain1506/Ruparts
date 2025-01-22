package com.ruparts;

import static com.ruparts.TasksActivity.fragmentPagerAdapter;

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

import com.ruparts.context.task.model.TaskObject;

import java.util.ArrayList;

public class TasksAllFragment extends Fragment {

    private Context context;
    private ExpandableListView listView;
    private ArrayList<ExpListGroup> filtered = new ArrayList<>();
    private ExpandableListAdapter adapter;

    private SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = this.getActivity();
        return inflater.inflate(R.layout.fragment_tasks_all, container, false);
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
        filtered.clear();
        listView.setGroupIndicator(null);
        listView.setChildIndicator(null);
        listView.setChildDivider(context.getDrawable(R.color.based_background));
        listView.setDivider(context.getDrawable(R.color.based_background));
        listView.setDividerHeight(20);
        filtered = fragmentPagerAdapter.createListData(null);
        adapter = new ExpandableListAdapter(context, filtered);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int parentPosition, int childPosition, long l) {

                filtered = fragmentPagerAdapter.createListData(null);
                ExpListGroup elg = filtered.get(parentPosition);
                TaskObject task = elg.itemsList.get(childPosition);

                int number = task.id.id;

                Intent intent = new Intent(context, TasksStructure.class);

                Bundle extras = new Bundle();
                extras.putSerializable(TaskObject.class.getSimpleName(), task);
                extras.putInt("id", number);
                intent.putExtras(extras);

                startActivity(intent);

                return false;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ExpandableListView) view.findViewById(R.id.tasks_exp_list_view_all);
        this.loadListView();

    }


}