package com.ftn.slagalica.ui.login;

import static com.ftn.slagalica.util.AuthHandler.Login.FILE_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.slagalica.MainActivity;
import com.ftn.slagalica.R;
import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.util.ICallbackCarrier;
import com.ftn.slagalica.util.IThemeHandler;
import com.ftn.slagalica.util.AuthHandler;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements IThemeHandler, ICallbackCarrier {

    SharedPreferences sharedPreferences;

    AuthHandler loginHandler;
    //    private Button loginBtn;
    private EditText usernameOrEmailField;
    private EditText passwordField;
    private Button loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setupTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        loginHandler = new AuthHandler();

        sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        usernameOrEmailField = findViewById(R.id.usernameLogin);
        passwordField = findViewById(R.id.passwordLogin);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> authenticate(view));
        findViewById(R.id.toRegisterBtn).setOnClickListener(view -> toRegister(view));
    }

    private void authenticate(View loginBtn) {
        loginBtn.setEnabled(false);
        String usernameInput = usernameOrEmailField.getText().toString();
        usernameInput = usernameInput.isEmpty() ? "" : usernameInput.trim();

        String passwordInput = passwordField.getText().toString();
        FirebaseUser foundPlayer = AuthHandler.Login.execute(usernameInput, passwordInput, this, this,true);
//        AuthBearer foundPlayer = AuthHandler.Login.execute(usernameInput, passwordInput, this, true);


    }

    private void toRegister(View v){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    @Override
    public void onResult(FirebaseUser foundPlayer) {
        if (foundPlayer == null) {
            Toast.makeText(LoginActivity.this, "Neispravni kredencijali", Toast.LENGTH_SHORT).show();
            usernameOrEmailField.setText(null);
//            usernameOrEmailField.requestFocus();
            passwordField.setText(null);
            loginBtn.setEnabled(true);
        }else {
            Intent iMainActivity = new Intent(LoginActivity.this, MainActivity.class);
//            iMainActivity.putExtra(USERNAME, foundPlayer.getUsername());
//            iMainActivity.putExtra(EMAIL, foundPlayer.getEmail());
//            iMainActivity.putExtra("picture", foundPlayer.getImageURI());
            Toast.makeText(LoginActivity.this, "Dobro do\u0161li, " + foundPlayer.getDisplayName(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(LoginActivity.this, "Dobro do\u0161li, " + foundPlayer.getUsername(), Toast.LENGTH_SHORT).show();
            startActivity(iMainActivity);
            finish();
        }
    }
}