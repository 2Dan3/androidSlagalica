package com.ftn.slagalica.util;

import static com.ftn.slagalica.util.AuthHandler.Login.EMAIL;
import static com.ftn.slagalica.util.AuthHandler.Login.PASSWORD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.data.model.User;
import com.ftn.slagalica.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthHandler {

    private static final String FIREBASE_URL = "https://slagalica-1b9c9-default-rtdb.europe-west1.firebasedatabase.app/";

    private static void rememberMe(String username, String email, String passwordCredential, Activity callingActivity) {
        SharedPreferences sharedPreferences = callingActivity.getSharedPreferences(Login.FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();

        spEditor.putString(Login.USERNAME, username);
        spEditor.putString(PASSWORD, passwordCredential);
        spEditor.putString(EMAIL, email);
        spEditor.putString("picture", "w/e");
        spEditor.commit();
    }

    public static class Login {

//        private static FirebaseAuth mAuth;
        public static final String FILE_NAME = "login";
        public static final String USERNAME = "usernameslagalica";
        public static final String PASSWORD = "passwordslagalica";
        public static final String EMAIL = "emailslagalica";

        public static FirebaseUser execute(String usernameOrEmailCredential, String passwordCredential, Activity callingActivity, ICallbackCarrier callbackCarrier, boolean rememberMe) {
            final FirebaseUser[] user = new FirebaseUser[1];
//
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
            mAuth.signInWithEmailAndPassword(usernameOrEmailCredential, passwordCredential)
                    .addOnCompleteListener(callingActivity,
                            task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "signInWithEmail:success");
                                    user[0] = mAuth.getCurrentUser();
//                                    Toast.makeText(callingActivity, user[0].toString(), Toast.LENGTH_SHORT).show();

                                    if (user[0] != null && rememberMe) {
                                        rememberMe(user[0].getDisplayName(), user[0].getEmail(), passwordCredential, callingActivity);
                                    }
                                }
//                                else {
//                                    // If sign in fails, display a message to the user.
////                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                    Toast.makeText(callingActivity, "Neispravni kredencijalii",
//                                            Toast.LENGTH_SHORT).show();
//                                }
                                callbackCarrier.onResult(user[0]);
                            }
                    );
//            AuthBearer foundUser = matchPlayerCredentials(usernameOrEmailCredential, passwordCredential);
//            if (foundUser != null && rememberMe) {
//                rememberMe(foundUser.getUsername(), foundUser.getEmail(), foundUser.getPassword(), callingActivity);
//            }
//            return foundUser;
            return user[0];
        }

        public boolean validateUsername(String username) {
            if (username.isEmpty())
                return false;
            return true;
        }
        public boolean validatePassword(String password) {
            if (password.isEmpty())
                return false;
            return true;
            }

        private void checkUser(String username, String password){
//            DatabaseReference ref = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("users");
//            Query findUser = ref.orderByChild("username").equalTo(username);
//
//            findUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        loginUsername.setError(null);
//                        String passwordFromDB = snapshot.child()
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }

        public static AuthBearer getLoggedPlayerCache(Activity callingActivity) {
            SharedPreferences sharedPreferences = callingActivity.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            String loggedUsername = sharedPreferences.getString(USERNAME, "");
            if (loggedUsername.equals(""))
                return null;

            return new AuthBearer(loggedUsername, sharedPreferences.getString(EMAIL, ""), sharedPreferences.getString(PASSWORD, ""), sharedPreferences.getString("picture", ""));
        }

        public static void forgetMe(SharedPreferences sharedPreferences) {
            FirebaseAuth.getInstance().signOut();

            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.remove(USERNAME);
            spEditor.remove(PASSWORD);
            spEditor.remove(EMAIL);
            spEditor.remove("picture");
            spEditor.commit();
        }

        private static AuthBearer matchPlayerCredentials(String usernameOrEMailCredential, String passwordCredential) {

            for (AuthBearer ab : credentialsOfPlayers() ) {
                if( (ab.getUsername().equalsIgnoreCase(usernameOrEMailCredential) || ab.getEmail().equalsIgnoreCase(usernameOrEMailCredential) ) && ab.getPassword().equals(passwordCredential) )
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

    public static class Register {
        //        private static FirebaseAuth mAuth;
        static FirebaseDatabase database;
        static DatabaseReference reference;

//        Todo : make it all transactional (if all does not persist - has to rollback changes)
        public static void execute(String username, String email, String password, String repeatedPassword, Activity callingActivity) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(callingActivity,
                task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

//                      Add username as well (after Email & Password creation)
                        user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build())
                                .addOnCompleteListener(
                                        task1 -> {
                                            if (task1.isSuccessful()) {
//                                                  Log.d(TAG, "User profile updated.");
                                                database = FirebaseDatabase.getInstance(FIREBASE_URL);
                                                reference = database.getReference("users");
                                                String uidKey = reference.push().getKey();
                                                User userToBeRegistered = new User(email, password, username);

                                                reference.child(uidKey).setValue(userToBeRegistered.toMap())
                                                        .addOnCompleteListener(
                                                                task2 -> {
                                                                    if (task2.isSuccessful()) {
                                                                        Toast.makeText(callingActivity, "Uspe\u0161no ste registrovani.", Toast.LENGTH_SHORT).show();
//                                            rememberMe(username, email, password, callingActivity);
                                                                        callingActivity.startActivity(new Intent(callingActivity, LoginActivity.class));
                                                                        callingActivity.finish();
                                                                    }
                                                                }
                                                        ).addOnFailureListener(
                                                                task3 -> {
                                                                    Toast.makeText(callingActivity, "Gre\u0161ka pri registrovanju. Poku\u0161ajte ponovo kasnije.", Toast.LENGTH_SHORT).show();
                                                                }
                                                        );
                                            }
                                        }
                                );

//            Toast.makeText(callingActivity, "Zavrsio gadjanje baze", Toast.LENGTH_SHORT).show();


//                        reference.child(username).setValue(userToBeRegistered);

//                        Map<String, Object> userUpdates = new HashMap<>();
//                        userUpdates.put("/users/" + uidKey, userToBeRegistered.toMap());

//                        mDatabase.updateChildren(userUpdates)
//                            .addOnCompleteListener(
//                                task2 -> {
//                                    if (task2.isSuccessful()) {
//                                        Toast.makeText(callingActivity, "Uspe\u0161no ste registrovani. Prijavljujemo Vas...", Toast.LENGTH_SHORT).show();
//                                        rememberMe(username, email, password, callingActivity);
//                                        callingActivity.startActivity(new Intent(callingActivity, MainActivity.class));
//                                    }else {
//                                        Toast.makeText(callingActivity, "Gre\u0161ka pri registrovanju. Poku\u0161aj ponovo kasnijee...",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            );
//                            .addOnFailureListener(
//                                task3 -> {
//                                    Toast.makeText(callingActivity, "Gre\u0161ka pri registrovanju. Poku\u0161aj ponovo kasnijeee...",
//                                                Toast.LENGTH_SHORT).show();
//                                }
//                            );


//                    } else {
//                        // If sign in fails, display a message to the user.
////                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                        Toast.makeText(callingActivity, "Gre\u0161ka pri registrovanju. Poku\u0161aj ponovo kasnije...",
//                                Toast.LENGTH_SHORT).show();
//                    }
                    }
                }
            );
        }
    }
}
