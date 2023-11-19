package com.ftn.slagalica.ui.login;

import static com.ftn.slagalica.util.LoginHandler.Login.EMAIL;
import static com.ftn.slagalica.util.LoginHandler.Login.FILE_NAME;
import static com.ftn.slagalica.util.LoginHandler.Login.USERNAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.slagalica.MainActivity;
import com.ftn.slagalica.R;
import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.util.LoginHandler;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    LoginHandler loginHandler;
    //    private Button loginBtn;
    private EditText usernameOrEmailField;
    private EditText passwordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        loginHandler = new LoginHandler();

        sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        usernameOrEmailField = findViewById(R.id.usernameLogin);
        passwordField = findViewById(R.id.passwordLogin);

        findViewById(R.id.loginBtn).setOnClickListener(view -> authenticate(view));
        findViewById(R.id.toRegisterBtn).setOnClickListener(view -> toRegister(view));
    }

    private void authenticate(View view) {
        String usernameInput = usernameOrEmailField.getText().toString().trim();
        String passwordInput = passwordField.getText().toString();
        AuthBearer foundPlayer = LoginHandler.Login.execute(usernameInput, passwordInput, sharedPreferences, true);

        if (foundPlayer == null) {
            Toast.makeText(LoginActivity.this, "Neispravni kredencijali", Toast.LENGTH_SHORT).show();
            usernameOrEmailField.setText(null);
            usernameOrEmailField.requestFocus();
            passwordField.setText(null);
        }else {
            Intent iMainActivity = new Intent(LoginActivity.this, MainActivity.class);
            iMainActivity.putExtra(USERNAME, foundPlayer.getUsername());
            iMainActivity.putExtra(EMAIL, foundPlayer.getEmail());
            iMainActivity.putExtra("picture", foundPlayer.getImageURI());
            Toast.makeText(LoginActivity.this, "Dobro do\u0161li, " + foundPlayer.getUsername(), Toast.LENGTH_SHORT).show();
            startActivity(iMainActivity);
            finish();
        }
    }

    private void toRegister(View v){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}