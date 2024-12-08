package com.example.navigationdrawer;


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

import com.example.navigationdrawer.helperclasses.AuthorizationMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthorizationActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button0;
    private Button cancelButton;
    private Button backButton;
    private EditText password;
    private String createPassword;
    private String token;
    private AuthorizationMap map;


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

        button1 = findViewById(R.id.btn_1);
        button2 = findViewById(R.id.btn_2);
        button3 = findViewById(R.id.btn_3);
        button4 = findViewById(R.id.btn_4);
        button5 = findViewById(R.id.btn_5);
        button6 = findViewById(R.id.btn_6);
        button7 = findViewById(R.id.btn_7);
        button8 = findViewById(R.id.btn_8);
        button9 = findViewById(R.id.btn_9);
        button0 = findViewById(R.id.btn_0);
        cancelButton = findViewById(R.id.btn_cancel);
        backButton = findViewById(R.id.btn_backspace);
        password = findViewById(R.id.password_layout);

        createPassword = "";
        password.setText(createPassword);

        map = new AuthorizationMap();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "1";
                password.setText(createPassword);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "2";
                password.setText(createPassword);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "3";
                password.setText(createPassword);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "4";
                password.setText(createPassword);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "5";
                password.setText(createPassword);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "6";
                password.setText(createPassword);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "7";
                password.setText(createPassword);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "8";
                password.setText(createPassword);
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "9";
                password.setText(createPassword);
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword += "0";
                password.setText(createPassword);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword = "";
                password.setText(createPassword);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassword = createPassword.substring(0, createPassword.length() - 1);
                password.setText(createPassword);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (Map.Entry<String, String> entry : map.authCodes.entrySet()) {
                    if (createPassword.equals(entry.getValue())) {
                        createPassword = "";
                        password.setText(createPassword);
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
                                        createPassword = "";
                                        password.setText(createPassword);
                                        Intent intent = new Intent(AuthorizationActivity.this, AuthorizationActivity.class);
                                        startActivity(intent);
                                    }
                                    String responseString = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseString);
                                    token = jsonObject.getString("token");
                                    saveToken();
                                    Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                    } else if (createPassword.length() == 6 && !createPassword.equals(entry.getValue())) {
                        Toast.makeText(AuthorizationActivity.this, "Неверный пароль", Toast.LENGTH_LONG).show();
                        createPassword = "";
                        password.setText(createPassword);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void saveToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

}


