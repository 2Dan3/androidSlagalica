package com.ftn.slagalica.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ftn.slagalica.data.model.AuthBearer;

public class LoginHandler {

    public static class Login {
        public static final String FILE_NAME = "login";
        public static final String USERNAME = "usernameslagalica";
        public static final String PASSWORD = "passwordslagalica";
        public static final String EMAIL = "emailslagalica";

        public static AuthBearer execute(String usernameOrEmailCredential, String passwordCredential, Activity callingActivity, boolean rememberMe) {
            AuthBearer foundPlayer = matchPlayerCredentials(usernameOrEmailCredential, passwordCredential);

            if (foundPlayer != null && rememberMe) {

                SharedPreferences sharedPreferences = callingActivity.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPreferences.edit();

                spEditor.putString(USERNAME, foundPlayer.getUsername());
                spEditor.putString(PASSWORD, foundPlayer.getPassword());
                spEditor.putString(EMAIL, foundPlayer.getEmail());
                spEditor.putString("picture", foundPlayer.getImageURI());
                spEditor.commit();
            }
            return foundPlayer;
        }

        public static AuthBearer getLoggedPlayerAuth(Activity callingActivity) {
            SharedPreferences sharedPreferences = callingActivity.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            String loggedUsername = sharedPreferences.getString(USERNAME, "");
            if (loggedUsername.equals(""))
                return null;

            return new AuthBearer(loggedUsername, sharedPreferences.getString(EMAIL, ""), sharedPreferences.getString(PASSWORD, ""), sharedPreferences.getString("picture", ""));
        }

        public static void forgetMe(SharedPreferences sharedPreferences) {

            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.remove(USERNAME);
            spEditor.remove(PASSWORD);
            spEditor.remove(EMAIL);
            spEditor.remove("picture");
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
