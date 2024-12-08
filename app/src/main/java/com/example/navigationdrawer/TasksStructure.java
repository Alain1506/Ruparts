package com.example.navigationdrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.navigationdrawer.helperclasses.TaskBodyObject;

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

    int imagevalue;
    TextView priorityText;

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
        getSupportActionBar().setTitle("Задача");

        comments = findViewById(R.id.details_view);
        data = findViewById(R.id.deadline_date_view);
        priority = findViewById(R.id.priority_imageview);
        taskType = findViewById(R.id.type_view);
        taskStatus = findViewById(R.id.status_view);
        taskCreatedDate = findViewById(R.id.date_view);
        taskImplementer = findViewById(R.id.implementer_view);

        Bundle arguments = getIntent().getExtras();
        imagevalue = arguments.getInt("image");


        if(arguments != null){

            tbo = (TaskBodyObject) arguments.getSerializable(TaskBodyObject.class.getSimpleName());

            comments.setText(tbo.description);
            data.setText(tbo.finish_at);
            priority.setImageResource(imagevalue);
            taskType.setText(tbo.type);
            taskStatus.setText(tbo.status);
            taskCreatedDate.setText(tbo.created_at);
            taskImplementer.setText(tbo.implementer);

            priorityText = findViewById(R.id.priority_view);

            if (imagevalue == R.drawable.baseline_circle_24) {
                priorityText.setText("Высокий");
            }

            getSupportActionBar().setSubtitle(tbo.title);

        }

        getSupportActionBar().setTitle("Задача");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}