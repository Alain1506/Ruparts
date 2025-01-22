package com.ruparts;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ruparts.helperclasses.AuthorizationMap;
import com.ruparts.helperclasses.LibraryMaps;
import com.ruparts.helperclasses.LibraryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthorizationActivity extends AppCompatActivity {

    private LibraryMaps libraryMaps = new LibraryMaps();
    private String token = null;

    private String enteredSymbols;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authorization);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.authorization), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button1 = findViewById(R.id.btn_1);
        Button button2 = findViewById(R.id.btn_2);
        Button button3 = findViewById(R.id.btn_3);
        Button button4 = findViewById(R.id.btn_4);
        Button button5 = findViewById(R.id.btn_5);
        Button button6 = findViewById(R.id.btn_6);
        Button button7 = findViewById(R.id.btn_7);
        Button button8 = findViewById(R.id.btn_8);
        Button button9 = findViewById(R.id.btn_9);
        Button button0 = findViewById(R.id.btn_0);
        Button cancelButton = findViewById(R.id.btn_cancel);
        Button backButton = findViewById(R.id.btn_backspace);
        EditText password = findViewById(R.id.password_layout);

        enteredSymbols = "";
        password.setText(enteredSymbols);

        AuthorizationMap map = new AuthorizationMap();

        Button[] buttons0to9 = {button0,button1,button2,button3,button4,button5,button6,button7,button8,button9};

        for (int i = 0; i < buttons0to9.length; i++) {
            final int number = i;
            buttons0to9[number].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enteredSymbols += String.valueOf(number);
                    password.setText(enteredSymbols);
                }
            });
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredSymbols = "";
                password.setText(enteredSymbols);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredSymbols = enteredSymbols.substring(0, enteredSymbols.length() - 1);
                password.setText(enteredSymbols);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (Map.Entry<String, String> entry : map.authCodes.entrySet()) {
                    if (enteredSymbols.equals(entry.getValue())) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                                    MediaType mediaType = MediaType.parse("application/json");
                                    RequestBody body = RequestBody.create("{\"code\":\"" + entry.getValue() + "\"}", mediaType);
                                    Request request = new Request.Builder()
                                            .url("http://stage.ruparts.ru/api/user/login_by_code")
                                            .method("POST", body)
                                            .addHeader("X-App-Secret", "0c6420f4f5c20fb7b26adffbc59eff3b")
                                            .addHeader("Content-Type", "application/json")
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    if (response.code() != 200) {
                                        Toast.makeText(AuthorizationActivity.this, "Сервер не может обработать запрос", Toast.LENGTH_LONG).show();
                                        enteredSymbols = "";
                                        password.setText(enteredSymbols);
                                        Intent intent = new Intent(AuthorizationActivity.this, AuthorizationActivity.class);
                                        startActivity(intent);
                                    }
                                    assert response.body() != null;
                                    String responseString = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseString);
                                    token = jsonObject.getString("token");
                                    saveToken();
                                    saveLibrary();

                                    Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                    } else if (enteredSymbols.length() == 6 && !enteredSymbols.equals(entry.getValue())) {
                        Toast.makeText(AuthorizationActivity.this, "Неверный пароль", Toast.LENGTH_LONG).show();
                        enteredSymbols = "";
                        password.setText(enteredSymbols);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void saveLibrary() {

        final JSONObject[] jsonObject = {null};
        libraryMaps = new LibraryMaps();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    LibraryRequest libraryRequest = new LibraryRequest();
                    libraryRequest.libraryRequestId = "325ege324ll23el42uicc";
                    libraryRequest.libraryRequestAction = "app.task.library";
                    libraryRequest.libraryRequestData = null;
                    final String mapsRequest = objectMapper.writeValueAsString(libraryRequest);

                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mapsRequest, mediaType);
                    Request request = new Request.Builder()
                            .url("http://stage.ruparts.ru/api/endpoint?XDEBUG_TRIGGER=0")
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.code() != 200) {
                        Toast.makeText(AuthorizationActivity.this, "Сервер не может загрузить библиотеку", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AuthorizationActivity.this, AuthorizationActivity.class);
                        startActivity(intent);
                    }
                    assert response.body() != null;
                    String responseString = response.body().string();

                    jsonObject[0] = new JSONObject(responseString);

//                    libraryMaps.task_types = null;
//                    libraryMaps.user_roles = null;
//                    libraryMaps.user_roles_editable = null;
//                    libraryMaps.implementer = null;
//                    libraryMaps.status = null;
//                    libraryMaps.id_reference_type = null;

                    String task_types = jsonObject[0].getJSONObject("data").getJSONObject("task_types").toString();
                    libraryMaps.taskTypes = objectMapper.readValue(task_types, HashMap.class);
                    String user_roles = jsonObject[0].getJSONObject("data").getJSONObject("user_roles").toString();
                    libraryMaps.userRoles = objectMapper.readValue(user_roles, HashMap.class);
                    String user_roles_editable = jsonObject[0].getJSONObject("data").getJSONObject("user_roles_editable").toString();
                    libraryMaps.userRolesEditable = objectMapper.readValue(user_roles_editable, HashMap.class);
                    String implementer = jsonObject[0].getJSONObject("data").getJSONObject("implementer").toString();
                    libraryMaps.implementer = objectMapper.readValue(implementer, HashMap.class);
                    String status = jsonObject[0].getJSONObject("data").getJSONObject("status").toString();
                    libraryMaps.status = objectMapper.readValue(status, HashMap.class);
                    String id_reference_type = jsonObject[0].getJSONObject("data").getJSONObject("id_reference_type").toString();
                    libraryMaps.idReferenceType = objectMapper.readValue(id_reference_type, HashMap.class);

                    SharedPreferences libraryTaskTypes = getSharedPreferences("SharedlibraryTaskTypes", MODE_PRIVATE);
                    SharedPreferences.Editor libraryTaskTypesEditor = libraryTaskTypes.edit();
                    for (String s : libraryMaps.taskTypes.keySet()) {
                        libraryTaskTypesEditor.putString(s, libraryMaps.taskTypes.get(s));
                    }
                    libraryTaskTypesEditor.commit();

                    SharedPreferences libraryUserRoles = getSharedPreferences("SharedlibraryUserRoles", MODE_PRIVATE);
                    SharedPreferences.Editor libraryUserRolesEditor = libraryUserRoles.edit();
                    for (String s : libraryMaps.userRoles.keySet()) {
                        libraryUserRolesEditor.putString(s, libraryMaps.userRoles.get(s));
                    }
                    libraryUserRolesEditor.commit();

                    SharedPreferences libraryUserRolesEditable = getSharedPreferences("SharedlibraryUserRolesEditable", MODE_PRIVATE);
                    SharedPreferences.Editor libraryUserRolesEditableEditor = libraryUserRolesEditable.edit();
                    for (String s : libraryMaps.userRolesEditable.keySet()) {
                        libraryUserRolesEditableEditor.putString(s, libraryMaps.userRolesEditable.get(s));
                    }
                    libraryUserRolesEditableEditor.commit();

                    SharedPreferences libraryImplementer = getSharedPreferences("SharedlibraryImplementer", MODE_PRIVATE);
                    SharedPreferences.Editor libraryImplementerEditor = libraryImplementer.edit();
                    for (String s : libraryMaps.implementer.keySet()) {
                        libraryImplementerEditor.putString(s, libraryMaps.implementer.get(s));
                    }
                    libraryImplementerEditor.commit();

                    SharedPreferences libraryStatus = getSharedPreferences("SharedlibraryStatus", MODE_PRIVATE);
                    SharedPreferences.Editor libraryStatusEditor = libraryStatus.edit();
                    for (String s : libraryMaps.status.keySet()) {
                        libraryStatusEditor.putString(s, libraryMaps.status.get(s));
                    }
                    libraryStatusEditor.commit();

                    SharedPreferences libraryIdReferenceType = getSharedPreferences("SharedlibraryIdReferenceType", MODE_PRIVATE);
                    SharedPreferences.Editor libraryIdReferenceTypeEditor = libraryIdReferenceType.edit();
                    for (String s : libraryMaps.idReferenceType.keySet()) {
                        libraryIdReferenceTypeEditor.putString(s, libraryMaps.idReferenceType.get(s));
                    }
                    libraryIdReferenceTypeEditor.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public void saveToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

}


