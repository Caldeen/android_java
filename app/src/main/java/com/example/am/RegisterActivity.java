package com.example.am;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import data.classes.User;
import helpers.RetrofitClient;
import helpers.SharedPrefsHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.UserService;

public class RegisterActivity extends AppCompatActivity {
    private Button confirmRegisterButton;
    private TextInputEditText usernameText;
    private TextInputEditText passwordText;
    private TextInputEditText firstNameText;
    private TextInputEditText lastNameText;
    private TextInputEditText birthDayText;
    private TextInputEditText birthmonthText;
    private TextInputEditText birthYearText;
    private TextInputEditText phoneNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPrefsHandler.loadTheme(this);
        setContentView(R.layout.register_activity);

        confirmRegisterButton = findViewById(R.id.confirmRegister);
        usernameText = findViewById(R.id.loginText);
        passwordText = findViewById(R.id.passRegisterText);
        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        birthDayText = findViewById(R.id.birthDayText);
        birthmonthText = findViewById(R.id.birthmonthText);
        birthYearText = findViewById(R.id.birthYearText);
        phoneNumberText = findViewById(R.id.phoneNumberText);


        confirmRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextFieldEmpty(usernameText)) {
                    usernameText.setError(getResources().getString(R.string.fieldRequired));
                    return;
                }
                if (isTextFieldEmpty(passwordText)) {
                    passwordText.setError(getResources().getString(R.string.fieldRequired));
                    return;
                }
                if (isTextFieldEmpty(phoneNumberText)) {
                    phoneNumberText.setError(getResources().getString(R.string.fieldRequired));
                    return;
                }
                if (!isTextFieldEmpty(birthDayText)) {
                    if (Integer.parseInt(birthDayText.getText().toString()) < 0
                            || Integer.parseInt(birthDayText.getText().toString()) > 31) {
                        birthDayText.setError(getResources().getString(R.string.wrongNumber));
                        return;
                    }
                }
                if (!isTextFieldEmpty(birthmonthText)) {
                    if (Integer.parseInt(birthmonthText.getText().toString()) < 0
                            || Integer.parseInt(birthmonthText.getText().toString()) > 12) {
                        birthmonthText.setError(getResources().getString(R.string.wrongNumber));
                        return;
                    }
                }
                if (!isTextFieldEmpty(birthYearText)) {
                    if (Integer.parseInt(birthYearText.getText().toString()) < 0
                            || Integer.parseInt(birthYearText.getText().toString()) > 2020) {
                        birthYearText.setError(getResources().getString(R.string.wrongNumber));
                        return;

                    }
                }
                addRegisterOnClickListener();
            }
        });
    }

    private boolean isTextFieldEmpty(TextInputEditText arg) {
        return arg.getText().toString().matches("");
    }

    private void addRegisterOnClickListener() {
        UserService userService = RetrofitClient.getRetrofit().create(UserService.class);
        Call<User> call = userService.register(new User(usernameText.getText().toString(),
                passwordText.getText().toString(), firstNameText.getText().toString(),
                toStringDate(birthYearText, birthmonthText, birthDayText)
                , lastNameText.getText().toString(), Integer.parseInt(phoneNumberText.getText().toString())));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() != 200) {
                    Log.println(Log.ERROR, "info", "failed Login");
                    usernameText.setError(getResources().getString(R.string.failedLoginError));
                    return;
                }
                SharedPrefsHandler.setToken(RegisterActivity.this, response.headers().get("SESSION"));
                SharedPrefsHandler.logPrefs(RegisterActivity.this);
                Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(nextScreen);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.println(Log.ERROR, "info", "failed Login" + t.getMessage());
                Intent nextScreen = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(nextScreen);
            }
        });
    }

    private String toStringDate(TextInputEditText birthYearText, TextInputEditText birthmonthText,
                                TextInputEditText birthDayText) {
        return birthYearText.getText().toString() + "-" +
                birthmonthText.getText().toString() + "-" +
                birthDayText.getText().toString();
    }
    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        Log.println(Log.INFO, "info", "restarted");
    }
}

