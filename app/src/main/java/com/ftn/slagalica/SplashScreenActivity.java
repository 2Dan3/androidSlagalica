package com.ftn.slagalica;

import static com.ftn.slagalica.util.LoginHandler.Login.EMAIL;
import static com.ftn.slagalica.util.LoginHandler.Login.FILE_NAME;
import static com.ftn.slagalica.util.LoginHandler.Login.PASSWORD;
import static com.ftn.slagalica.util.LoginHandler.Login.USERNAME;
import static com.ftn.slagalica.util.LoginHandler.Login.execute;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.ui.login.LoginActivity;
import com.ftn.slagalica.util.LoginHandler;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    public static final int SPLASH_TIMEOUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        Intent iMainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);

        if (sharedPreferences.contains(USERNAME)) {

            String savedUsernameOrEmail = sharedPreferences.getString(USERNAME, "");
            String savedPassword = sharedPreferences.getString(PASSWORD, "");

//            usernameOrEmailField.setText(savedUsernameOrEmail);
//            passwordField.setText(savedPassword);
            AuthBearer foundPlayer = LoginHandler.Login.execute(savedUsernameOrEmail, savedPassword, sharedPreferences, false);
            if (foundPlayer != null) {
                iMainActivity.putExtra(USERNAME, foundPlayer.getUsername());
                iMainActivity.putExtra(EMAIL, foundPlayer.getEmail());
                iMainActivity.putExtra("picture", foundPlayer.getImageURI());
                Toast.makeText(SplashScreenActivity.this, "Dobro do\u0161li, " + foundPlayer.getUsername(), Toast.LENGTH_SHORT).show();
            }
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(iMainActivity);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}