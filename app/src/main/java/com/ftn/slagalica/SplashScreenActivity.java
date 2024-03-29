package com.ftn.slagalica;

import static com.ftn.slagalica.util.AuthHandler.Login.FILE_NAME;
import static com.ftn.slagalica.util.AuthHandler.Login.PASSWORD;
import static com.ftn.slagalica.util.AuthHandler.Login.USERNAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.util.ICallbackCarrier;
import com.ftn.slagalica.util.IThemeHandler;
import com.ftn.slagalica.util.AuthHandler;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity implements IThemeHandler, ICallbackCarrier {

    public static final int SPLASH_TIMEOUT = 1300;
    private Intent iMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        iMainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);

        if (sharedPreferences.contains(USERNAME)) {

            String savedUsernameOrEmail = sharedPreferences.getString(USERNAME, "");
            String savedPassword = sharedPreferences.getString(PASSWORD, "");

            FirebaseUser foundPlayer = AuthHandler.Login.execute(savedUsernameOrEmail, savedPassword, this, this, false);
//            AuthBearer foundPlayer = AuthHandler.Login.execute(savedUsernameOrEmail, savedPassword, this, false);
        }else{
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(iMainActivity);
                    finish();
                }
            }, SPLASH_TIMEOUT);
        }
    }

    @Override
    public void onResult(FirebaseUser foundPlayer) {
        if (foundPlayer != null) {
//                iMainActivity.putExtra(USERNAME, foundPlayer.getUsername());
//                iMainActivity.putExtra(EMAIL, foundPlayer.getEmail());
//                iMainActivity.putExtra("picture", foundPlayer.getImageURI());
            Toast.makeText(SplashScreenActivity.this, "Dobro do\u0161li, " + foundPlayer.getDisplayName(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(SplashScreenActivity.this, "Dobro do\u0161li, " + foundPlayer.getUsername(), Toast.LENGTH_SHORT).show();
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