package com.ruparts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ruparts.context.library.LibraryRepository;
import com.ruparts.context.library.TaskLibraryModel;
import com.google.android.material.navigation.NavigationView;

import com.ruparts.context.task.model.api.LibraryRequest;
import com.ruparts.main.Container;
import com.ruparts.main.CrashHandler;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String token = null;
//    public static TaskLibraryModel taskLibraryModel = new TaskLibraryModel();
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SharedPreferences sharedToken;
//    private SharedPreferences sharedLibraryTaskTypes;
//    private SharedPreferences sharedLibraryUserRoles;
//    private SharedPreferences sharedLibraryUserRolesEditable;
//    private SharedPreferences sharedLibraryImplementer;
//    private SharedPreferences sharedLibraryStatus;
//    private SharedPreferences sharedLibraryIdReferenceType;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //добавить видимость еще одного layout.xml
        LayoutInflater layInfl = this.getLayoutInflater();
        RelativeLayout linLay = findViewById(R.id.ndh_main);
        View vv = layInfl.inflate(R.layout.navigation_drawer_header, linLay, true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedToken = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        token = sharedToken.getString("token", "");





        if (token == null || token.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
            startActivity(intent);
        }

        Container.getLibraryRepository().init(this.getApplicationContext());

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Главная страница");

        drawerLayout = findViewById(R.id.main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextAppearance(R.style.NavigationDrawerStyle);

        Button logout = vv.findViewById(R.id.logout_button);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = menuItem.getItemId();
        if (id == R.id.tasks) {
            startActivity(new Intent(this, TasksActivity.class));
            return true;
        }
        return true;
    }

    public void logout(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Выход");
        builder.setMessage("Вы хотите закрыть приложение?");
        builder.setPositiveButton("ДА!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    sharedToken.edit().clear().apply();

                    SharedPreferences preferences = getSharedPreferences(LibraryRepository.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    preferences.edit().clear().apply();
                } catch (Exception e) {
                    e.getMessage();
                }

                Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("НЕТ!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    public void createLibraryMaps() {
//        sharedLibraryTaskTypes = getSharedPreferences("SharedlibraryTaskTypes", MODE_PRIVATE);
//        taskLibraryModel.taskTypes = (HashMap<String, String>) sharedLibraryTaskTypes.getAll();
//        sharedLibraryUserRoles = getSharedPreferences("SharedlibraryUserRoles", MODE_PRIVATE);
//        taskLibraryModel.userRoles = (HashMap<String, String>) sharedLibraryUserRoles.getAll();
//        sharedLibraryUserRolesEditable = getSharedPreferences("SharedlibraryUserRolesEditable", MODE_PRIVATE);
//        taskLibraryModel.userRolesEditable = (HashMap<String, String>) sharedLibraryUserRolesEditable.getAll();
//        sharedLibraryImplementer = getSharedPreferences("SharedlibraryImplementer", MODE_PRIVATE);
//        taskLibraryModel.implementer = (HashMap<String, String>) sharedLibraryImplementer.getAll();
//        sharedLibraryStatus = getSharedPreferences("SharedlibraryStatus", MODE_PRIVATE);
//        taskLibraryModel.status = (HashMap<String, String>) sharedLibraryStatus.getAll();
//        sharedLibraryIdReferenceType = getSharedPreferences("SharedlibraryIdReferenceType", MODE_PRIVATE);
//        taskLibraryModel.idReferenceType = (HashMap<String, String>) sharedLibraryIdReferenceType.getAll();
//    }
}
