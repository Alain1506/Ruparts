package com.ruparts;

import static com.ruparts.MainActivity.libraryMaps;
import static com.ruparts.MainActivity.token;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.ruparts.context.task.model.TaskId;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.context.task.service.TaskRepository;
import com.ruparts.helperclasses.TaskObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.android.material.tabs.TabLayout;
import com.ruparts.main.ApiClient;
import com.ruparts.main.Container;
import com.ruparts.main.CrashHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TasksActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    public static List<TaskObject> listOfTasks = new ArrayList<>();
    public static Map<Integer, TaskObject> mapOfTasks = new HashMap<>();
    public static ArrayList<ExpListGroup> expListContents;

    private SearchManager searchManager;
    private ExpandableListAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 fragmentPager;
    private TasksViewPager2Adapter fragmentPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tasks);

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TaskRepository x = Container.getTaskRepository();
                TaskObject y = x.getById(new TaskId(1));
                y = y;
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //добавить видимость еще одного layout.xml
        LayoutInflater layInfl = this.getLayoutInflater();
        RelativeLayout linLay = findViewById(R.id.list_of_tasks);
        View vv = layInfl.inflate(R.layout.task_explist_item, linLay, true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_tasks), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Задачи");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        expListContents = initializeExpListContents();

        adapter = new ExpandableListAdapter(this, expListContents);
        ExpandableListView expandableListView = new ExpandableListView(this);
        expandableListView.setAdapter(adapter);
        expandableListView = findViewById(R.id.tasks_exp_list_view);
        expandableListView.setGroupIndicator(null); //здесь можно задать разные индикаторы, если группа раскрыта или свернута
        expandableListView.setChildIndicator(null);
        expandableListView.setChildDivider(getDrawable(R.color.based_background));
        expandableListView.setDivider(getDrawable(R.color.based_background));
        expandableListView.setDividerHeight(20);

        tabLayout = findViewById(R.id.tasks_tablayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        fragmentPager = findViewById(R.id.tasks_view_pager2);
        fragmentPagerAdapter = new TasksViewPager2Adapter(this);
        fragmentPager.setAdapter(fragmentPagerAdapter);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (tabLayout.getSelectedTabPosition() >= 0) {
            doInitializeFragmentPage(tabLayout.getSelectedTabPosition());
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        fragmentPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int parentPosition, int childPosition, long l) {

                ExpListGroup elg = expListContents.get(parentPosition);
                TaskObject task = elg.itemsList.get(childPosition);

                Intent intent = new Intent(getBaseContext(), TasksStructure.class);
                intent.putExtra(TaskObject.class.getSimpleName(), task);
                startActivity(intent);

                return false;
            }
        });

    }

    private ArrayList<ExpListGroup> initializeExpListContents() {

        ObjectMapper objectMapper = new ObjectMapper();
        TaskObjectRequest taskObject = new TaskObjectRequest();
        taskObject.action = "app.task.list";
        taskObject.id = "325ege324ll23el42uicc";

        final String taskObjectAsString;
        try {
            taskObjectAsString = objectMapper.writeValueAsString(taskObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final JSONObject[] jsonObject = {null};

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(taskObjectAsString, mediaType);
                    Request request = new Request.Builder()
                            .url("http://stage.ruparts.ru/api/endpoint?XDEBUG_TRIGGER=0")
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.code() == 401) {
                        Toast.makeText(TasksActivity.this, "Токен устарел", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TasksActivity.this, AuthorizationActivity.class);
                        startActivity(intent);
                    } else if (response.code() != 200) {
                        Toast.makeText(TasksActivity.this, "Произошла ошибка загрузки задач", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TasksActivity.this, TasksActivity.class);
                        startActivity(intent);
                    }
                    assert response.body() != null;
                    String responseString = response.body().string();
                    jsonObject[0] = new JSONObject(responseString);

                    if (jsonObject[0].getInt("type") != 0) {
                        Toast.makeText(TasksActivity.this, "Список задач не получен", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TasksActivity.this, TasksActivity.class);
                        startActivity(intent);
                    }

//                    JSONObject jo =
                    String jsonListOfTasks = jsonObject[0].getJSONObject("data").getJSONArray("list").toString();

                    listOfTasks = objectMapper.readValue(jsonListOfTasks, TypeFactory.defaultInstance().constructCollectionType(List.class,
                            TaskObject.class));

                    for (TaskObject task : listOfTasks) {
                        mapOfTasks.put(task.taskId, task);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<ExpListGroup> allGroups = new ArrayList<>();

        for (String s : libraryMaps.taskTypes.keySet()) {
            ExpListGroup elg = new ExpListGroup(s);
            elg.elgTaskTypeToShow = libraryMaps.taskTypes.get(s);
            allGroups.add(elg);
        }

        for (int i = 0; i < listOfTasks.size(); i++) {
            TaskObject task = listOfTasks.get(i);

            for (ExpListGroup elg : allGroups) {
                if (task.taskType.equals(elg.elgTaskType)) {
                    elg.itemsList.add(task);
                    break;
                }
            }
        }

        Iterator<ExpListGroup> iterator = allGroups.iterator();
        while (iterator.hasNext()) {
            ExpListGroup element = iterator.next();
            if (element.itemsList.isEmpty()) {
                iterator.remove();
            } else {
                element.elgTaskTypeToShow = element.elgTaskTypeToShow.substring(0, 1).toUpperCase()
                        + element.elgTaskTypeToShow.substring(1) + " (" + element.itemsList.size() + ")";
            }
        }

        return allGroups;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasks_activity_toolbar_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchview = (SearchView) menuItem.getActionView();
        assert searchview != null;
        searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchview.setQueryHint("Поиск");
//        searchview.setIconifiedByDefault(true);
        searchview.setOnQueryTextListener(this);
        searchview.setOnCloseListener(this);
        searchview.requestFocus();

        return true;
    }


    @Override
    public boolean onClose() {
        adapter.filterData("");
        return false;
    }

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

    private void doInitializeFragmentPage(int selectedTabPosition) {
        try {
            Objects.requireNonNull(tabLayout.getTabAt(fragmentPager.getCurrentItem())).select();
            fragmentPager.setCurrentItem(tabLayout.getSelectedTabPosition());

            switch (selectedTabPosition) {
                case (0):
                    TasksToDoFragment fr = (TasksToDoFragment) fragmentPagerAdapter.getFragmentByPosition(tabLayout.getSelectedTabPosition());
                    fr.loadListView();
                    break;
                case (1):
                    TasksInProgressFragment fr1 = (TasksInProgressFragment) fragmentPagerAdapter.getFragmentByPosition(tabLayout.getSelectedTabPosition());
                    fr1.loadListView();
                    break;
                case  (2):
                    TasksCompletedFragment fr2 = (TasksCompletedFragment) fragmentPagerAdapter.getFragmentByPosition(tabLayout.getSelectedTabPosition());
                    fr2.loadListView();
                    break;
                case (3):
                    TasksCancelledFragment fr3 = (TasksCancelledFragment) fragmentPagerAdapter.getFragmentByPosition(tabLayout.getSelectedTabPosition());
                    fr3.loadListView();
                    break;
                case  (4):
                    TasksAllFragment fr4 = (TasksAllFragment) fragmentPagerAdapter.getFragmentByPosition(tabLayout.getSelectedTabPosition());
                    fr4.loadListView();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        int position = tabLayout.getSelectedTabPosition();
        doInitializeFragmentPage(position);





//        tasksViewPager2Adapter.createFragment(viewPager2.getCurrentItem());
//        String s = null;


        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }




}