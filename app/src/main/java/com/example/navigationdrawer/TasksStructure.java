package com.example.navigationdrawer;

import static com.example.navigationdrawer.TasksActivity.libraryMaps;
import static com.example.navigationdrawer.TasksActivity.mapOfTasks;
import static com.example.navigationdrawer.TasksActivity.token;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.navigationdrawer.helperclasses.TaskBodyObject;
import com.example.navigationdrawer.helperclasses.TaskStatusRequest;
import com.example.navigationdrawer.helperclasses.TaskUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TasksStructure extends AppCompatActivity {

    private Toolbar toolbar;
    private TaskBodyObject tbo;

    private ImageView priority;
    private EditText comments;
    private EditText date;
    private TextView taskType;
    private TextView taskStatus;
    private TextView taskCreatedDate;
    private TextView taskImplementer;
    private Calendar changeableDate = Calendar.getInstance();
    private Button btnSave;
    private MaterialButton btnInWork;
    private MaterialButton btnCancelled;
    private LinearLayout threeButtonsLayout;
    private TextView changeablePrioriry;


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


            comments = findViewById(R.id.details_view);
            date = findViewById(R.id.deadline_date_view);
            priority = findViewById(R.id.priority_imageview);
            taskType = findViewById(R.id.type_view);
            taskStatus = findViewById(R.id.status_view);
            taskCreatedDate = findViewById(R.id.date_view);
            taskImplementer = findViewById(R.id.implementer_view);
            threeButtonsLayout = findViewById(R.id.three_buttons_layout);
            btnSave = findViewById(R.id.button_save);
            btnInWork = findViewById(R.id.button_in_work);
            btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            btnCancelled = findViewById(R.id.button_cancel);
            int cancelImage = R.drawable.baseline_cancel_24;
            btnCancelled.setCompoundDrawablesWithIntrinsicBounds(cancelImage, 0, 0, 0);
            changeablePrioriry = findViewById(R.id.priority_material_button);

            Bundle arguments = getIntent().getExtras();

            if (arguments != null) {

                tbo = (TaskBodyObject) arguments.getSerializable(TaskBodyObject.class.getSimpleName());

                assert tbo != null;
                comments.setText(tbo.description);

                if (tbo.finish_at != null) {
                    Date finishDate = tbo.finish_at;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
                    String formattedDate = simpleDateFormat.format(finishDate);
                    date.setText(formattedDate);
                }

                switch (tbo.priority) {
                    case ("high"):
                        priority.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24);
                        changeablePrioriry.setText("Высокий");
                        break;
                    case ("low"):
                        priority.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24);
                        changeablePrioriry.setText("Низкий");
                        break;
                    default:
                        priority.setImageResource(R.drawable.equal_priority);
                        break;
                }
                taskType.setText(libraryMaps.task_types.get(tbo.type));
                taskStatus.setText(libraryMaps.status.get(tbo.status));

                Date createdDate = tbo.created_at;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
                String formattedCreatedDate = dateFormat.format(createdDate);
                taskCreatedDate.setText(formattedCreatedDate);
//                taskCreatedDate.setText(tbo.created_at);

                taskImplementer.setText(libraryMaps.implementer.get(tbo.implementer));



                if (tbo.status.equals("to_do")) {
                    btnInWork.setText("В работу");
                    btnInWork.setIcon(getDrawable(R.drawable.baseline_play_arrow_24));
                } else if (tbo.status.equals("in_progress")) {
                    btnInWork.setText("Закрыть");
                    btnInWork.setIcon(getDrawable(R.drawable.baseline_close_24));
                } else {
                    String textForButton = Objects.requireNonNull(libraryMaps.status.get(tbo.status)).substring(0,1).toUpperCase()
                            + Objects.requireNonNull(libraryMaps.status.get(tbo.status)).substring(1);
                    btnInWork.setText(textForButton);
                    btnInWork.setBackgroundColor(getResources().getColor(R.color.white));
                    btnInWork.setTextColor(getResources().getColor(R.color.gray));
                    btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    btnCancelled.setVisibility(View.INVISIBLE);
                }
            }

            getSupportActionBar().setSubtitle(tbo.title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            changeablePrioriry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TasksStructure.this);
                    @SuppressLint("InflateParams") View view1 = LayoutInflater.from(TasksStructure.this).inflate(R.layout.task_structure_bottom_sheet,null);
                    bottomSheetDialog.setContentView(view1);
                    bottomSheetDialog.show();

                    TextView high = view1.findViewById(R.id.ts_status_high);
                    TextView low = view1.findViewById(R.id.ts_status_low);

                    high.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeablePrioriry.setText("Высокий");
                            priority.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24);
                            tbo.priority = "high";
                            saveChanges(tbo);
                            bottomSheetDialog.dismiss();
                        }
                    });
                    low.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeablePrioriry.setText("Низкий");
                            priority.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24);
                            tbo.priority = "low";
                            saveChanges(tbo);
                            bottomSheetDialog.dismiss();
                        }
                    });
                }
            });



            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveChanges(tbo);
                }
            });

            btnInWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tbo.status.equals("to_do")) {
                        tbo.status = "in_progress";
                        changeTaskStatus(tbo);
                        refreshData(tbo);
                        btnInWork.setText("Закрыть");
                        btnInWork.setIcon(getDrawable(R.drawable.baseline_close_24));
                        saveChanges(tbo);

                    } else if (tbo.status.equals("in_progress")) {
                        tbo.status = "completed";
                        changeTaskStatus(tbo);
                        refreshData(tbo);
                        btnCancelled.setVisibility(View.INVISIBLE);
                        String textForButton = Objects.requireNonNull(libraryMaps.status.get(tbo.status)).substring(0,1).toUpperCase()
                                + Objects.requireNonNull(libraryMaps.status.get(tbo.status)).substring(1);
                        btnInWork.setText(textForButton);
                        btnInWork.setTextColor(getResources().getColor(R.color.gray));
                        btnInWork.setBackgroundColor(getResources().getColor(R.color.white));
                        btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        saveChanges(tbo);
                    }
                }
            });

            btnCancelled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tbo.status = "cancelled";
                    changeTaskStatus(tbo);
                    refreshData(tbo);
                    String textForButton = Objects.requireNonNull(libraryMaps.status.get(tbo.status)).substring(0,1).toUpperCase()
                            + Objects.requireNonNull(libraryMaps.status.get(tbo.status)).substring(1);
                    btnInWork.setText(textForButton);
                    btnInWork.setTextColor(getResources().getColor(R.color.gray));
                    btnInWork.setBackgroundColor(getResources().getColor(R.color.white));
                    btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    btnCancelled.setVisibility(View.INVISIBLE);
                    saveChanges(tbo);
                }
            });
        } catch (Throwable e) {
            e.getMessage();
        }

    }

    public void saveChanges(TaskBodyObject tbo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TaskUpdateRequest tur = new TaskUpdateRequest(tbo);
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

                        String task = jsonObject[0].getJSONObject("data").toString();

                        TaskBodyObject newTask = objectMapper.readValue(task, TaskBodyObject.class);
                        mapOfTasks.put(newTask.tbdo_id, newTask);

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

    public void changeTaskStatus(TaskBodyObject tbo) {
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
                    String task = jsonObject[0].getJSONObject("data").toString();

                    TaskBodyObject newTask = objectMapper.readValue(task, TaskBodyObject.class);
                    mapOfTasks.put(newTask.tbdo_id, newTask);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    public void setDate(View v) {
        new DatePickerDialog(TasksStructure.this, d,
                changeableDate.get(Calendar.YEAR),
                changeableDate.get(Calendar.MONTH),
                changeableDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            changeableDate.set(Calendar.YEAR, year);
            changeableDate.set(Calendar.MONTH, monthOfYear);
            changeableDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date yesterday = calendar.getTime();

            if (changeableDate.getTime().after(yesterday)) {
                Date finishDate = tbo.finish_at;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
                String formattedDate = simpleDateFormat.format(finishDate);
                date.setText(formattedDate);
                tbo.finish_at = finishDate;
            } else {
                Toast.makeText(TasksStructure.this, "Эта дата уже прошла", Toast.LENGTH_LONG).show();
            }
        }
    };

    public void refreshData(TaskBodyObject tbo) {
        taskStatus.setText(libraryMaps.status.get(tbo.status));
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasks_structure_toolbar_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

}