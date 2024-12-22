package com.example.navigationdrawer;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.navigationdrawer.helperclasses.LibraryMaps;
import com.example.navigationdrawer.helperclasses.TaskBodyObject;
import com.example.navigationdrawer.helperclasses.TaskObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.android.material.tabs.TabLayout;

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

    private SearchManager searchManager;
    private SearchView searchview;
    private List<TaskBodyObject> listOfTasks = new ArrayList<>();
    public static Map<Integer, TaskBodyObject> mapOfTasks = new HashMap<>();
    private ExpandableListView listView;
    public static ArrayList<ExpListGroup> groups;

    public static String token;
    public static LibraryMaps libraryMaps = new LibraryMaps();

    private ExpandableListAdapter adapter;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TasksViewPager2Adapter tasksViewPager2Adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tasks);

        //добавить видимость еще одного layout.xml
        LayoutInflater layInfl = this.getLayoutInflater();
        RelativeLayout linLay = findViewById(R.id.list_of_tasks);
        View vv = layInfl.inflate(R.layout.list_item, linLay, true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_tasks), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        createLibraryMaps();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


            groups = initData();
            adapter = new ExpandableListAdapter(this, groups);
            listView = new ExpandableListView(this);
            listView.setAdapter(adapter);

            Objects.requireNonNull(getSupportActionBar()).setTitle("Задачи");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            listView = findViewById(R.id.tasks_exp_list_view);
            listView.setGroupIndicator(null);
            listView.setChildIndicator(null);
            listView.setChildDivider(getDrawable(R.color.based_background));
            listView.setDivider(getDrawable(R.color.based_background));
            listView.setDividerHeight(20);

        viewPager2 = findViewById(R.id.tasks_view_pager2);
        tasksViewPager2Adapter = new TasksViewPager2Adapter(this);
        viewPager2.setAdapter(tasksViewPager2Adapter);

            tabLayout = findViewById(R.id.tasks_tablayout);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


            searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);





        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });



        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int parentPosition, int childPosition, long l) {

                ExpListGroup elg = groups.get(parentPosition);
                TaskBodyObject tbo = elg.items.get(childPosition);

                Intent intent = new Intent(getBaseContext(), TasksStructure.class);
                intent.putExtra(TaskBodyObject.class.getSimpleName(), tbo);
                startActivity(intent);

                return false;
            }
        });
    }


    private ArrayList<ExpListGroup> initData() {

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
                        Intent intent = new Intent(TasksActivity.this, AuthorizationActivity.class);
                        startActivity(intent);
                        Toast.makeText(TasksActivity.this, "Токен устарел", Toast.LENGTH_LONG).show();
                    } else if (response.code() != 200) {
//                        String string = response.body().string();
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

                    String jsonListOfTasks = jsonObject[0].getJSONObject("data").getJSONArray("list").toString();

                    listOfTasks = objectMapper.readValue(jsonListOfTasks, TypeFactory.defaultInstance().constructCollectionType(List.class,
                            TaskBodyObject.class));

                    for (TaskBodyObject tbo : listOfTasks) {
                        mapOfTasks.put(tbo.tbdo_id, tbo);
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

        for (String s : libraryMaps.task_types.keySet()) {
            ExpListGroup elg = new ExpListGroup(s);
            elg.elgTaskTypeToShow = libraryMaps.task_types.get(s);
            allGroups.add(elg);
        }

        for (int i = 0; i < listOfTasks.size(); i++) {
            TaskBodyObject tbo = listOfTasks.get(i);

            for (ExpListGroup elg : allGroups) {
                if (tbo.type.equals(elg.elgTaskType)) {
                    elg.items.add(tbo);
                }
            }
        }

        Iterator<ExpListGroup> iterator = allGroups.iterator();
        while (iterator.hasNext()) {
            ExpListGroup element = iterator.next();
            if (element.items.isEmpty()) {
                iterator.remove();
            } else {
                element.elgTaskTypeToShow = element.elgTaskTypeToShow.substring(0, 1).toUpperCase()
                        + element.elgTaskTypeToShow.substring(1) + " (" + element.items.size() + ")";
            }
        }

        return allGroups;
    }


    public void createLibraryMaps() {
        SharedPreferences pref = getSharedPreferences("SharedlibraryTaskTypes", MODE_PRIVATE);
        libraryMaps.task_types = (HashMap<String, String>) pref.getAll();
        SharedPreferences pref1 = getSharedPreferences("SharedlibraryUserRoles", MODE_PRIVATE);
        libraryMaps.user_roles = (HashMap<String, String>) pref1.getAll();
        SharedPreferences pref2 = getSharedPreferences("SharedlibraryUserRolesEditable", MODE_PRIVATE);
        libraryMaps.user_roles_editable = (HashMap<String, String>) pref2.getAll();
        SharedPreferences pref3 = getSharedPreferences("SharedlibraryImplementer", MODE_PRIVATE);
        libraryMaps.implementer = (HashMap<String, String>) pref3.getAll();
        SharedPreferences pref4 = getSharedPreferences("SharedlibraryStatus", MODE_PRIVATE);
        libraryMaps.status = (HashMap<String, String>) pref4.getAll();
        SharedPreferences pref5 = getSharedPreferences("SharedlibraryIdReferenceType", MODE_PRIVATE);
        libraryMaps.id_reference_type = (HashMap<String, String>) pref5.getAll();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasks_activity_toolbar_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        searchview = (SearchView) menuItem.getActionView();
        assert searchview != null;
        searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchview.setQueryHint("Поиск");
        searchview.setIconifiedByDefault(true);
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

}