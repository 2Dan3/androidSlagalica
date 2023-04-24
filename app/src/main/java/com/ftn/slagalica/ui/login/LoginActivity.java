package com.ftn.slagalica.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.slagalica.MainActivity;
import com.ftn.slagalica.R;

public class LoginActivity extends AppCompatActivity {
//    private Button loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginBtn).setOnClickListener(view -> authenticate(view));
    }

    private void authenticate(View view) {
//        TODO credential check
//        if(credentials ...){
//            TODO upis nekog login tokena u sesiju?
//                          ...
        Toast.makeText(LoginActivity.this, "Prijava", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
//        } else{
//              ...
//        }
    }

}