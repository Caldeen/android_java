package com.example.am;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import helpers.RetrofitClient;
import helpers.SharedPrefsHandler;
import services.UserService;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import data.classes.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ImageView loginImageView;
    private ImageView registerImageView;
    private TextInputEditText passwordTextField;
    private TextInputEditText usernameTextField;
    private ImageView settingsImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        SharedPrefsHandler.loadTheme(this);
        setContentView(R.layout.login_screen);
        loginImageView = findViewById(R.id.imageView2);
        registerImageView = findViewById(R.id.imageView);
        passwordTextField = findViewById(R.id.passwordTextField);
        usernameTextField = findViewById(R.id.usernameTextField);
        settingsImageView = findViewById(R.id.settingsImageView);
        loginImageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (usernameTextField.getText().toString().matches("")) {
                    usernameTextField.setError(getResources().getString(R.string.fieldRequired));
                    return;
                }
                if (passwordTextField.getText().toString().matches("")) {
                    passwordTextField.setError(getResources().getString(R.string.fieldRequired));
                    return;
                }
                UserService userService = RetrofitClient.getRetrofit().create(UserService.class);
                Call<User> call = userService.login(new User(usernameTextField.getText().toString(), passwordTextField.getText().toString()));
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() != 200) {
                            Log.println(Log.ERROR, "info", "failed Login");
                            usernameTextField.setError(getResources().getString(R.string.failedLoginError));
                            return;
                        }
                        SharedPrefsHandler.setToken(LoginActivity.this,response.headers().get("SESSION"));
                        SharedPrefsHandler.logPrefs(LoginActivity.this);
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
        });
        registerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(nextScreen);
            }
        });
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(nextScreen);
            }
        });
    }

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        Log.println(Log.INFO, "info", "restarted");
    }
}
