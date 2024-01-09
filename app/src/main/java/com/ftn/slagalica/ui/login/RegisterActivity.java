package com.ftn.slagalica.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.slagalica.R;
import com.ftn.slagalica.util.IThemeHandler;
import com.ftn.slagalica.util.AuthHandler;

public class RegisterActivity extends AppCompatActivity implements IThemeHandler {
    EditText[] credentialsFields;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setupTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.registerBtn).setOnClickListener(view -> register(view));
        findViewById(R.id.toLoginBtn).setOnClickListener(view -> toLoginActivity(view));
    }

    private void register(View view) {
        view.setEnabled(false);

        credentialsFields = new EditText[] {
            findViewById(R.id.usernameRegister),
            findViewById(R.id.emailRegister),
            findViewById(R.id.passwordRegister),
            findViewById(R.id.repeatPasswordRegister)
        };

        for (EditText field : credentialsFields) {
            String txt = field.getText().toString();
            if ( "".equals(txt) || txt == null ){
                Toast.makeText(this, "Potrebno je uneti sve podatke.", Toast.LENGTH_SHORT).show();
                resetFields(view);
                return;
            }
        }
        if ( !credentialsFields[2].getText().toString().equals(credentialsFields[3].getText().toString()) ){
            Toast.makeText(this, "Lozinka i ponovljena lozinka se ne poklapaju.", Toast.LENGTH_SHORT).show();
            resetFields(view);
            return;
        }

        AuthHandler.Register.execute(credentialsFields[0].getText().toString(), credentialsFields[1].getText().toString(), credentialsFields[2].getText().toString(), credentialsFields[3].getText().toString(), this);

//        if( !credentialsExist ){
//                          ...

//        } else{
//              ...
//        }
    }

    private void resetFields(View view) {
        for (EditText field : credentialsFields) {
            field.setText("");
        }
        view.setEnabled(true);
    }

    public void toLoginActivity(View v) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

}