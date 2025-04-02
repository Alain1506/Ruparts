package com.ruparts;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.google.android.material.textview.MaterialTextView;
import com.ruparts.context.library.LibraryModel;
import com.ruparts.context.library.LibraryRepository;
import com.ruparts.context.library.TaskLibraryModel;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.context.task.model.TaskStatusEnum;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.ruparts.context.task.service.TaskRepository;
import com.ruparts.main.Container;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class TasksStructure extends AppCompatActivity {

    private Toolbar toolbar;
    private TaskObject task;

    private ImageView priority;
    private EditText description;
    private EditText date;
    private TextView taskTitle;
    private TextView taskStatus;
    private TextView taskCreatedDate;
    private TextView taskChangeddDate;
    private TextView taskImplementer;
    private Calendar changeableDate = Calendar.getInstance();
    private MaterialButton btnSave;
    private MaterialButton btnInWork;
    private MaterialButton btnCancelled;
    private LinearLayout threeButtonsLayout;
    private MaterialTextView changeablePriority;
    private TextView finishDateHeader;

    private LibraryRepository libraryRepository;
    private TaskLibraryModel taskLibraryModel;
    private TaskRepository taskRepository;



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

            libraryRepository = Container.getLibraryRepository();
            libraryRepository.init(this.getApplicationContext());
            taskLibraryModel = libraryRepository.getLibrary().taskLibraryModel;

            taskRepository = Container.getTaskRepository();

            toolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Задача");

            description = findViewById(R.id.description_view);
            date = findViewById(R.id.finishAt_date_view);
            priority = findViewById(R.id.priority_imageview);
            taskTitle = findViewById(R.id.title_view);
//            taskStatus = findViewById(R.id.status_view);
            taskCreatedDate = findViewById(R.id.date_view);
            taskChangeddDate = findViewById(R.id.date_view2);
            taskImplementer = findViewById(R.id.implementer_view);
            threeButtonsLayout = findViewById(R.id.three_buttons_layout);

            btnSave = findViewById(R.id.button_save);
            btnSave.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
            btnInWork = findViewById(R.id.button_in_work);
            btnInWork.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_play_arrow_24, 0, 0, 0);
            btnCancelled = findViewById(R.id.button_cancelled);
            btnCancelled.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_cancel_24, 0, 0, 0);

            changeablePriority = findViewById(R.id.priority_material_button);
//            finishDateHeader = findViewById(R.id.finishAt_date_header);

            Bundle arguments = getIntent().getExtras();

            if (arguments != null) {

                task = (TaskObject) arguments.getSerializable(TaskObject.class.getSimpleName());

                assert task != null;
                description.setText(task.taskDescription);

                if (task.taskFinishAt != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                    String formattedDate = simpleDateFormat.format(task.taskFinishAt);
                    date.setText(formattedDate + " г.");
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

//                String textForTaskTypeField = Objects.requireNonNull(taskLibraryModel.taskTypes.get(task.taskType)).substring(0, 1).toUpperCase()
//                        + Objects.requireNonNull(taskLibraryModel.taskTypes.get(task.taskType)).substring(1);
//                taskType.setText(textForTaskTypeField);
                taskTitle.setText(task.taskTitle);



                String textForTaskStatusField = Objects.requireNonNull(taskLibraryModel.status.get(task.status.getStatusFromLibrary())).substring(0, 1).toUpperCase()
                        + Objects.requireNonNull(taskLibraryModel.status.get(task.status.getStatusFromLibrary())).substring(1);
//                taskStatus.setText(textForTaskStatusField);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                String formattedCreatedDate = dateFormat.format(task.taskCreatedAt);
                taskCreatedDate.setText(formattedCreatedDate + " г.");

                String formattedChangedDate = dateFormat.format(task.taskUpdatedAt);
                taskChangeddDate.setText(formattedChangedDate + " г.");


//                taskImplementer.setText(taskLibraryModel.implementer.get(task.taskImplementer));

                String textForTaskImplementerField = Objects.requireNonNull(taskLibraryModel.implementer.get(task.taskImplementer)).substring(0, 1).toUpperCase()
                        + Objects.requireNonNull(taskLibraryModel.implementer.get(task.taskImplementer)).substring(1);
                taskImplementer.setText(textForTaskImplementerField);

                if (task.status.equals(TaskStatusEnum.TO_DO)) {
                    btnInWork.setText("В работу");
                    btnInWork.setIcon(getDrawable(R.drawable.baseline_play_arrow_24));
                } else if (task.status.equals(TaskStatusEnum.IN_PROGRESS)) {
                    btnInWork.setText("Закрыть");
                    btnInWork.setIcon(getDrawable(R.drawable.baseline_close_24));
                } else {
                    String textForButton = Objects.requireNonNull(taskLibraryModel.status.get(task.status.getStatusFromLibrary())).substring(0, 1).toUpperCase()
                            + Objects.requireNonNull(taskLibraryModel.status.get(task.status.getStatusFromLibrary())).substring(1);
                    btnInWork.setText(textForButton);
                    btnInWork.setBackgroundColor(this.getColor(R.color.white));
                    btnInWork.setTextColor(this.getColor(R.color.gray));
                    btnInWork.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    btnCancelled.setVisibility(View.INVISIBLE);
                }
            }

            String textForTaskTypeField = Objects.requireNonNull(taskLibraryModel.taskTypes.get(task.taskType)).substring(0, 1).toUpperCase()
                    + Objects.requireNonNull(taskLibraryModel.taskTypes.get(task.taskType)).substring(1);
//            getSupportActionBar().setSubtitle(task.taskTitle);
            getSupportActionBar().setSubtitle(textForTaskTypeField);
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
                    if (task.status.equals(TaskStatusEnum.TO_DO)) {
                        task.status = TaskStatusEnum.IN_PROGRESS;
                        changeTaskStatus(task);
                        taskStatus.setText(taskLibraryModel.status.get(task.status.getStatusFromLibrary()));
                        btnInWork.setText("Закрыть");
                        btnInWork.setIcon(getDrawable(R.drawable.baseline_close_24));

                        saveChanges(task);

                    } else if (task.status.equals(TaskStatusEnum.IN_PROGRESS)) {
                        task.status = TaskStatusEnum.COMPLETED;
                        changeTaskStatus(task);
                        taskStatus.setText(taskLibraryModel.status.get(task.status.getStatusFromLibrary()));
                        btnCancelled.setVisibility(View.INVISIBLE);
                        String textForButton = Objects.requireNonNull(taskLibraryModel.status.get(task.status.getStatusFromLibrary())).substring(0, 1).toUpperCase()
                                + Objects.requireNonNull(taskLibraryModel.status.get(task.status.getStatusFromLibrary())).substring(1);
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
                    task.status = TaskStatusEnum.COMPLETED;
                    changeTaskStatus(task);
                    taskStatus.setText(taskLibraryModel.status.get(task.status.getStatusFromLibrary()));
                    String textForButton = Objects.requireNonNull(taskLibraryModel.status.get(task.status.getStatusFromLibrary())).substring(0, 1).toUpperCase()
                            + Objects.requireNonNull(taskLibraryModel.status.get(task.status.getStatusFromLibrary())).substring(1);
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

        for (Map.Entry<String, String> entry : taskLibraryModel.implementer.entrySet()) {
            if (!entry.getKey().contains(":") && !list.contains(entry.getValue())) {

                String textForTaskImplementerField = Objects.requireNonNull(entry.getValue()).substring(0, 1).toUpperCase()
                        + Objects.requireNonNull(entry.getValue()).substring(1);

                list.add(textForTaskImplementerField);
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
                String textForTaskImplementerField = Objects.requireNonNull(taskLibraryModel.implementer.get(task.taskImplementer)).substring(0, 1).toUpperCase()
                        + Objects.requireNonNull(taskLibraryModel.implementer.get(task.taskImplementer)).substring(1);
                taskImplementer.setText(textForTaskImplementerField);

                for (Map.Entry<String, String> entry : taskLibraryModel.implementer.entrySet()) {
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
            date.setText("Введите дату");
            date.setTextColor(this.getColor(R.color.red));
            Toast.makeText(this,"Изменения не сохранены", Toast.LENGTH_LONG).show();
        } else {

            try {
                task.taskDescription = String.valueOf(description.getText());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                String formattedChangedDate = dateFormat.format(new Date());
                taskChangeddDate.setText(formattedChangedDate + " г.");

                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                try {
                    task.taskUpdatedAt = format.parse(formattedChangedDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                task = taskRepository.saveTask(task);
            } catch (Throwable e) {
                e.getMessage();
            }
        }
    }

    public void changeTaskStatus(TaskObject task) {
        task = taskRepository.changeTaskStatus(task);
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

            Date finishDate = changeableDate.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            String formattedDate = simpleDateFormat.format(finishDate);
            date.setText(formattedDate + " г.");
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