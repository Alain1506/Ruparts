package com.example.navigationdrawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.navigationdrawer.helperclasses.LibraryMaps;
import com.example.navigationdrawer.helperclasses.TaskBodyObject;
import com.example.navigationdrawer.helperclasses.TaskObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

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

public class TasksActivity extends AppCompatActivity {

    private List<TaskBodyObject> listOfTasks = new ArrayList<>();
    public static Map<Integer, TaskBodyObject> mapOfTasks = new HashMap<>();

    public static String token;
    public static LibraryMaps libraryMaps = new LibraryMaps();


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

        Objects.requireNonNull(getSupportActionBar()).setTitle("Задачи");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ExpandableListView listView = findViewById(R.id.tasks_exp_list_view);

        ImageView priorityItem = vv.findViewById(R.id.item_priority);
        TextView taskItem = vv.findViewById(R.id.item_name);
        TextView commentsItem = vv.findViewById(R.id.item_comment);
        TextView dateItem = vv.findViewById(R.id.item_date);

        ArrayList<ExpListGroup> groups = initData();

        ExpandableListAdapter adapter = new ExpandableListAdapter(this, groups);
        listView.setAdapter(adapter);

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

                    for (TaskBodyObject tbo: listOfTasks) {
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

}