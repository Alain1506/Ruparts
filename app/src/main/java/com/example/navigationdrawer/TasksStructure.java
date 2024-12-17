package com.example.navigationdrawer;

import static com.example.navigationdrawer.TasksActivity.mapOfTasks;
import static com.example.navigationdrawer.TasksActivity.token;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.navigationdrawer.helperclasses.TaskBodyObject;
import com.example.navigationdrawer.helperclasses.TaskObjectRequest;
import com.example.navigationdrawer.helperclasses.TaskUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TasksStructure extends AppCompatActivity {

    Toolbar toolbar;
    TaskBodyObject tbo;

    ImageView priority;
    EditText comments;
    EditText data;
    TextView taskType;
    TextView taskStatus;
    TextView taskCreatedDate;
    TextView taskImplementer;
    Spinner spinner;
    Calendar changeableDate = Calendar.getInstance();
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        data = findViewById(R.id.deadline_date_view);
        priority = findViewById(R.id.priority_imageview);
        taskType = findViewById(R.id.type_view);
        taskStatus = findViewById(R.id.status_view);
        taskCreatedDate = findViewById(R.id.date_view);
        taskImplementer = findViewById(R.id.implementer_view);
        btnSave = findViewById(R.id.button_save);

        spinner = findViewById(R.id.priority_spinner);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.priorities,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {

            tbo = (TaskBodyObject) arguments.getSerializable(TaskBodyObject.class.getSimpleName());

            assert tbo != null;
            comments.setText(tbo.description);
            data.setText(tbo.finish_at);

            switch (tbo.priority) {
                case ("high"):
                    priority.setImageResource(R.drawable.baseline_circle_24);
                    spinner.setSelection(0);
                    break;
                case ("low"):
                    priority.setImageResource(R.drawable.baseline_circle_24_yellow);
                    spinner.setSelection(1);
                    break;
                default:
                    priority.setImageResource(R.drawable.ic_house_foreground);
                    break;
            }
            taskType.setText(TasksActivity.libraryMaps.task_types.get(tbo.type));
            taskStatus.setText(TasksActivity.libraryMaps.status.get(tbo.status));
            taskCreatedDate.setText(tbo.created_at);
            taskImplementer.setText(TasksActivity.libraryMaps.implementer.get(tbo.implementer));

            getSupportActionBar().setSubtitle(tbo.title);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {

                    switch (selectedItemPosition) {
                        case (0):
                            priority.setImageResource(R.drawable.baseline_circle_24);
                            tbo.priority = "high";
                            break;
                        case (1):
                            priority.setImageResource(R.drawable.baseline_circle_24_yellow);
                            tbo.priority = "low";
                            break;
                        default:
                            priority.setImageResource(R.drawable.ic_house_foreground);
                            break;
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String formatted = format.format(changeableDate.getTime());
                data.setText(formatted);
                tbo.finish_at = formatted;
            } else {
                Toast.makeText(TasksStructure.this, "Эта дата уже прошла", Toast.LENGTH_LONG).show();
            }
        }
    };

}