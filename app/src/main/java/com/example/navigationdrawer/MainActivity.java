package com.example.navigationdrawer;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

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

import com.example.navigationdrawer.helperclasses.LibraryMaps;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String token = null;
    public static LibraryMaps libraryMaps = new LibraryMaps();
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SharedPreferences sharedToken;
    private SharedPreferences sharedLibraryTaskTypes;
    private SharedPreferences sharedLibraryUserRoles;
    private SharedPreferences sharedLibraryUserRolesEditable;
    private SharedPreferences sharedLibraryImplementer;
    private SharedPreferences sharedLibraryStatus;
    private SharedPreferences sharedLibraryIdReferenceType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        createLibraryMaps();

        if (token == null || token.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
            startActivity(intent);
        }

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
                    SharedPreferences.Editor editor = sharedToken.edit();
                    editor.clear();
                    editor.apply();

                    SharedPreferences.Editor editor1 = sharedLibraryTaskTypes.edit();
                    editor1.clear();
                    editor1.apply();

                    SharedPreferences.Editor editor2 = sharedLibraryUserRoles.edit();
                    editor2.clear();
                    editor2.apply();

                    SharedPreferences.Editor editor3 = sharedLibraryUserRolesEditable.edit();
                    editor3.clear();
                    editor3.apply();

                    SharedPreferences.Editor editor4 = sharedLibraryImplementer.edit();
                    editor4.clear();
                    editor4.apply();

                    SharedPreferences.Editor editor5 = sharedLibraryStatus.edit();
                    editor5.clear();
                    editor5.apply();

                    SharedPreferences.Editor editor6 = sharedLibraryIdReferenceType.edit();
                    editor6.clear();
                    editor6.apply();
                }catch (Exception e) {
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

    public void createLibraryMaps() {
        sharedLibraryTaskTypes = getSharedPreferences("SharedlibraryTaskTypes", MODE_PRIVATE);
        libraryMaps.taskTypes = (HashMap<String, String>) sharedLibraryTaskTypes.getAll();
        sharedLibraryUserRoles = getSharedPreferences("SharedlibraryUserRoles", MODE_PRIVATE);
        libraryMaps.userRoles = (HashMap<String, String>) sharedLibraryUserRoles.getAll();
        sharedLibraryUserRolesEditable = getSharedPreferences("SharedlibraryUserRolesEditable", MODE_PRIVATE);
        libraryMaps.userRolesEditable = (HashMap<String, String>) sharedLibraryUserRolesEditable.getAll();
        sharedLibraryImplementer = getSharedPreferences("SharedlibraryImplementer", MODE_PRIVATE);
        libraryMaps.implementer = (HashMap<String, String>) sharedLibraryImplementer.getAll();
        sharedLibraryStatus = getSharedPreferences("SharedlibraryStatus", MODE_PRIVATE);
        libraryMaps.status = (HashMap<String, String>) sharedLibraryStatus.getAll();
        sharedLibraryIdReferenceType = getSharedPreferences("SharedlibraryIdReferenceType", MODE_PRIVATE);
        libraryMaps.idReferenceType = (HashMap<String, String>) sharedLibraryIdReferenceType.getAll();
    }
}
