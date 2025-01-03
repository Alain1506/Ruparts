package com.example.navigationdrawer;

import static com.example.navigationdrawer.MainActivity.libraryMaps;
import static com.example.navigationdrawer.MainActivity.token;
import static com.example.navigationdrawer.TasksActivity.expListContents;
import static com.example.navigationdrawer.TasksActivity.listOfTasks;
import static com.example.navigationdrawer.TasksActivity.mapOfTasks;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.navigationdrawer.helperclasses.TaskObject;
import com.example.navigationdrawer.helperclasses.TaskStatusRequest;
import com.example.navigationdrawer.helperclasses.TaskUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TasksStructure extends AppCompatActivity {

    private Toolbar toolbar;
    private TaskObject task;

    private ImageView priority;
    private EditText description;
    private EditText date;
    private TextView taskType;
    private TextView taskStatus;
    private TextView taskCreatedDate;
    private TextView taskImplementer;
    private Calendar changeableDate = Calendar.getInstance();
    private MaterialButton btnSave;
    private MaterialButton btnInWork;
    private MaterialButton btnCancelled;
    private LinearLayout threeButtonsLayout;
    private TextView changeablePriority;
    private TextView finishDateHeader;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_tasks_structure);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tasks_structure), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            toolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Задача");

            description = findViewById(R.id.description_view);
            date = findViewById(R.id.finishAt_date_view);
            priority = findViewById(R.id.priority_imageview);
            taskType = findViewById(R.id.type_view);
            taskStatus = findViewById(R.id.status_view);
            taskCreatedDate = findViewById(R.id.date_view);
            taskImplementer = findViewById(R.id.implementer_view);
            threeButtonsLayout = findViewById(R.id.three_buttons_layout);

            btnSave = findViewById(R.id.button_save);
            btnSave.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
            btnInWork = findViewById(R.id.button_in_work);
            btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            btnCancelled = findViewById(R.id.button_cancelled);
            btnCancelled.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_cancel_24, 0, 0, 0);

            changeablePriority = findViewById(R.id.priority_material_button);
            finishDateHeader = findViewById(R.id.deadline_date_header);


            Bundle arguments = getIntent().getExtras();

            if (arguments != null) {

                task = (TaskObject) arguments.getSerializable(TaskObject.class.getSimpleName());

                assert task != null;
                description.setText(task.taskDescription);

                if (task.taskFinishAt != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
                    String formattedDate = simpleDateFormat.format(task.taskFinishAt);
                    date.setText(formattedDate);
                }

                switch (task.taskPriority) {
                    case ("high"):
                        priority.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24);
                        changeablePriority.setText("Высокий");
                        break;
                    case ("low"):
                        priority.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24);
                        changeablePriority.setText("Низкий");
                        break;
                    case ("medium"):
                        priority.setImageResource(R.drawable.equal_priority);
                        changeablePriority.setText("Средний");
                        break;
                    default:
                        break;
                }

                String textForTaskTypeField = Objects.requireNonNull(libraryMaps.taskTypes.get(task.taskType)).substring(0, 1).toUpperCase()
                        + Objects.requireNonNull(libraryMaps.taskTypes.get(task.taskType)).substring(1);
                taskType.setText(textForTaskTypeField);

                String textForTaskStatusField = Objects.requireNonNull(libraryMaps.status.get(task.taskStatus)).substring(0, 1).toUpperCase()
                        + Objects.requireNonNull(libraryMaps.status.get(task.taskStatus)).substring(1);
                taskStatus.setText(textForTaskStatusField);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
                String formattedCreatedDate = dateFormat.format(task.taskCreatedAt);
                taskCreatedDate.setText(formattedCreatedDate);

                taskImplementer.setText(libraryMaps.implementer.get(task.taskImplementer));

                if (task.taskStatus.equals("to_do")) {
                    btnInWork.setText("В работу");
                    btnInWork.setIcon(getDrawable(R.drawable.baseline_play_arrow_24));
                } else if (task.taskStatus.equals("in_progress")) {
                    btnInWork.setText("Закрыть");
                    btnInWork.setIcon(getDrawable(R.drawable.baseline_close_24));
                } else {
                    String textForButton = Objects.requireNonNull(libraryMaps.status.get(task.taskStatus)).substring(0, 1).toUpperCase()
                            + Objects.requireNonNull(libraryMaps.status.get(task.taskStatus)).substring(1);
                    btnInWork.setText(textForButton);
                    btnInWork.setBackgroundColor(getResources().getColor(R.color.white));
                    btnInWork.setTextColor(getResources().getColor(R.color.gray));
                    btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    btnCancelled.setVisibility(View.INVISIBLE);
                }
            }

            getSupportActionBar().setSubtitle(task.taskTitle);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);

            changeablePriority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TasksStructure.this);
                    @SuppressLint("InflateParams") View view1 = LayoutInflater.from(TasksStructure.this).inflate(R.layout.task_structure_bottom_sheet_priority, null);
                    bottomSheetDialog.setContentView(view1);
                    bottomSheetDialog.show();

                    TextView high = view1.findViewById(R.id.ts_status_high);
                    TextView low = view1.findViewById(R.id.ts_status_low);
                    TextView medium = view1.findViewById(R.id.ts_status_medium);

                    high.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeablePriority.setText("Высокий");
                            priority.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24);
                            task.taskPriority = "high";
                            bottomSheetDialog.dismiss();
                        }
                    });
                    low.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeablePriority.setText("Низкий");
                            priority.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24);
                            task.taskPriority = "low";
                            bottomSheetDialog.dismiss();
                        }
                    });
                    medium.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeablePriority.setText("Средний");
                            priority.setImageResource(R.drawable.equal_priority);
                            task.taskPriority = "medium";
                            bottomSheetDialog.dismiss();
                        }
                    });
                }
            });

            taskImplementer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task.taskImplementer = showBottomSheetImplementers();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveChanges(task);
                }
            });

            btnInWork.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    if (task.taskStatus.equals("to_do")) {
                        task.taskStatus = "in_progress";
                        changeTaskStatus(task);
                        taskStatus.setText(libraryMaps.status.get(task.taskStatus));
                        btnInWork.setText("Закрыть");
                        btnInWork.setIcon(getDrawable(R.drawable.baseline_close_24));
                        saveChanges(task);

                    } else if (task.taskStatus.equals("in_progress")) {
                        task.taskStatus = "completed";
                        changeTaskStatus(task);
                        taskStatus.setText(libraryMaps.status.get(task.taskStatus));
                        btnCancelled.setVisibility(View.INVISIBLE);
                        String textForButton = Objects.requireNonNull(libraryMaps.status.get(task.taskStatus)).substring(0, 1).toUpperCase()
                                + Objects.requireNonNull(libraryMaps.status.get(task.taskStatus)).substring(1);
                        btnInWork.setText(textForButton);
                        btnInWork.setTextColor(R.color.gray);
                        btnInWork.setBackgroundColor(R.color.white);
                        btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        saveChanges(task);
                    }
                }
            });

            btnCancelled.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    task.taskStatus = "cancelled";
                    changeTaskStatus(task);
                    taskStatus.setText(libraryMaps.status.get(task.taskStatus));
                    String textForButton = Objects.requireNonNull(libraryMaps.status.get(task.taskStatus)).substring(0, 1).toUpperCase()
                            + Objects.requireNonNull(libraryMaps.status.get(task.taskStatus)).substring(1);
                    btnInWork.setText(textForButton);
                    btnInWork.setTextColor(R.color.gray);
                    btnInWork.setBackgroundColor(R.color.white);
                    btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    btnCancelled.setVisibility(View.INVISIBLE);
                    saveChanges(task);
                }
            });
        } catch (Throwable e) {
            e.getMessage();
        }

    }

    private String showBottomSheetImplementers() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.task_structure_bottom_sheet_implementer, null);
        ListView listView = bottomSheetView.findViewById(R.id.ts_bottom_sheet_implementers);

        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry<String, String> entry : libraryMaps.implementer.entrySet()) {
            if (!entry.getKey().contains(":") && !list.contains(entry.getValue())) {
                list.add(entry.getValue());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, list
        );
        listView.setAdapter(adapter);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskImplementer.setText(list.get(position));

                for (Map.Entry<String, String> entry : libraryMaps.implementer.entrySet()) {
                    if (entry.getValue().equals(list.get(position))) {
                        task.taskImplementer = entry.getKey();
                    }
                }
                bottomSheetDialog.dismiss();
            }
        });
        return task.taskImplementer;
    }

    public void saveChanges(TaskObject task) {

        if (task.taskFinishAt == null) {
            finishDateHeader.setText("Введите дату");
            finishDateHeader.setTextColor(getResources().getColor(R.color.red));
        } else {

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                task.taskDescription = description.getText().toString();
                TaskUpdateRequest tur = new TaskUpdateRequest(task);
                tur.action = "app.task.update";
                tur.id = "325ege324ll23el42uicc";

                final String updateObjectAsString;
                try {
                    updateObjectAsString = objectMapper.writeValueAsString(tur);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final JSONObject[] jsonObject = {null};

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Thread current = Thread.currentThread();
                        try {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(updateObjectAsString, mediaType);
                            Request request = new Request.Builder()
                                    .url("http://stage.ruparts.ru/api/endpoint?XDEBUG_TRIGGER=0")
                                    .method("POST", body)
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Authorization", "Bearer " + token)
                                    .build();
                            Response response = client.newCall(request).execute();
                            if (response.code() != 200) {
                                Toast.makeText(TasksStructure.this, "Невозможно скорректировать задачу", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TasksStructure.this, TasksStructure.class);
                                startActivity(intent);
                            }
                            assert response.body() != null;
                            String responseString = response.body().string();
                            jsonObject[0] = new JSONObject(responseString);

                            if (jsonObject[0].getInt("type") != 0) {
                                current.interrupt();
                            }

                            String receivedTask = jsonObject[0].getJSONObject("data").toString();

                            TaskObject savedTask = objectMapper.readValue(receivedTask, TaskObject.class);
                            mapOfTasks.put(savedTask.taskId, savedTask);

                            for (int i = 0; i < expListContents.size(); i++) {
                                if (expListContents.get(i).elgTaskType.equals(savedTask.taskType)) {
                                    expListContents.get(i).updateTask(savedTask);
                                }
                            }

                            for (int i = 0; i < listOfTasks.size(); i++) {
                                if (listOfTasks.get(i).taskId == savedTask.taskId) {
                                    listOfTasks.set(i, savedTask);
                                }
                            }

                        } catch (Exception e) {
                            current.interrupt();
                            e.getMessage();
                        }
                    }
                });
                thread.start();
            } catch (Throwable e) {
                e.getMessage();
            }
        }
    }

    public void changeTaskStatus(TaskObject tbo) {
        ObjectMapper objectMapper = new ObjectMapper();
        TaskStatusRequest tsr = new TaskStatusRequest(tbo);
        tsr.action = "app.task.status";
        tsr.id = "325ege324ll23el42uicc";

        final String statusObjectAsString;
        try {
            statusObjectAsString = objectMapper.writeValueAsString(tsr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final JSONObject[] jsonObject = {null};

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(statusObjectAsString, mediaType);
                    Request request = new Request.Builder()
                            .url("http://stage.ruparts.ru/api/endpoint?XDEBUG_TRIGGER=0")
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.code() != 200) {
                        Toast.makeText(TasksStructure.this, "Невозможно отправить задачу в работу", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TasksStructure.this, TasksStructure.class);
                        startActivity(intent);
                    }
                    assert response.body() != null;
                    String responseString = response.body().string();
                    jsonObject[0] = new JSONObject(responseString);
                    String receivedTask = jsonObject[0].getJSONObject("data").toString();

                    TaskObject changedTask = objectMapper.readValue(receivedTask, TaskObject.class);
                    mapOfTasks.put(changedTask.taskId, changedTask);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    public void setDate(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(TasksStructure.this, d,
                changeableDate.get(Calendar.YEAR),
                changeableDate.get(Calendar.MONTH),
                changeableDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            changeableDate.set(Calendar.YEAR, year);
            changeableDate.set(Calendar.MONTH, monthOfYear);
            changeableDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date yesterday = calendar.getTime();

            Date finishDate = changeableDate.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
            String formattedDate = simpleDateFormat.format(finishDate);
            date.setText(formattedDate);
            task.taskFinishAt = finishDate;
        }
    };

//    public void refreshData(TaskObject task) {
//        taskStatus.setText(libraryMaps.status.get(task.taskStatus));
//    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasks_structure_toolbar_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

//    @Override
//    public void onBackPressed() {
//
////        if (getFragmentManager().getBackStackEntryCount() > 0) {
////            getFragmentManager().popBackStack();
////        } else {
////            super.onBackPressed();
////        }
//    }


}