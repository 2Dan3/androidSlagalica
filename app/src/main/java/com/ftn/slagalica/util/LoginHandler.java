package com.ftn.slagalica.util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.ftn.slagalica.MainActivity;
import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.ui.login.LoginActivity;

public class LoginHandler {

    public static class Login {
        public static final String FILE_NAME = "login";
        public static final String USERNAME = "usernameslagalica";
        public static final String PASSWORD = "passwordslagalica";
        public static final String EMAIL = "emailslagalica";

        public static AuthBearer execute(String usernameOrEmailCredential, String passwordCredential, SharedPreferences sharedPreferences, boolean rememberMe) {
            AuthBearer foundPlayer = matchPlayerCredentials(usernameOrEmailCredential, passwordCredential);

            if (foundPlayer != null && rememberMe) {

                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putString(USERNAME, foundPlayer.getUsername());
                spEditor.putString(PASSWORD, foundPlayer.getPassword());
                spEditor.apply();
            }
            return foundPlayer;
        }

        public static void forgetMe(SharedPreferences sharedPreferences) {

            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.remove(USERNAME);
            spEditor.remove(PASSWORD);
            spEditor.commit();
        }

        private static AuthBearer matchPlayerCredentials(String usernameOrEMailCredential, String passwordCredential) {

            for (AuthBearer ab : credentialsOfPlayers() ) {
                if( (ab.getUsername().equals(usernameOrEMailCredential) || ab.getEmail().equalsIgnoreCase(usernameOrEMailCredential) ) && ab.getPassword().equals(passwordCredential) )
                    return ab;
            }
            return null;
        }

        private static AuthBearer[] credentialsOfPlayers() {
            AuthBearer[] foundPlayersCollection;
//        TODO Asynchronously try to match input credentials to an existing Player in real DB
//        Mock collection for testing :
            foundPlayersCollection = new AuthBearer[] {
                    new AuthBearer("Test", "test@gmail.com", "test", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/2w"),
                    new AuthBearer("randomUsername96", "nesto@gmail.com", "pass123", "data:image/png;base64,AAAAHGZ0eXBhdmlm")
            };
            return foundPlayersCollection;
        }
    }


}
