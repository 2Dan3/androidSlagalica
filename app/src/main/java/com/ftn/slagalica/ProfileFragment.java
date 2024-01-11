package com.ftn.slagalica;

import static com.ftn.slagalica.util.AuthHandler.FIREBASE_URL;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.data.model.User;
import com.ftn.slagalica.util.AuthHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private Button btnSavePic;
    private Uri selectedImageUri;
    private User loggedUserDatabaseData;
    private FirebaseAuth fbAuth;

    private TextView tvUsername,
    tvEmail,
    tvGamesPlayed,
    tvGamesWon,
    tvWhoKnowsPoints,
    tvConnectTwoPoints,
    tvAssociationsPoints,
    tvJumperPoints,
    tvStepByStepPoints,
    tvMyNumberPoints;

    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AuthBearer loggedUserCacheData = AuthHandler.Login.getLoggedPlayerCache(getActivity());

        fbAuth = FirebaseAuth.getInstance();

        imgProfile = view.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(imgView -> chooseImage());

        btnSavePic = view.findViewById(R.id.btnSavePic);
        btnSavePic.setOnClickListener(btnSave -> saveProfilePic());

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(logoutBtn -> {
            ( (MainActivity) getActivity() ).logout();
        });

        tvUsername = view.findViewById(R.id.textViewUsernameProfile);
        tvUsername.setText(loggedUserCacheData.getUsername());

        tvEmail = view.findViewById(R.id.textViewEmailProfile);
        tvEmail.setText(loggedUserCacheData.getEmail());

        tvGamesPlayed = view.findViewById(R.id.textViewGamesPlayedProfile);
        tvGamesWon = view.findViewById(R.id.textViewWinrateProfile);
        tvWhoKnowsPoints = view.findViewById(R.id.tvProfileWhoKnowsPoints);
        tvConnectTwoPoints = view.findViewById(R.id.tvProfileConnectTwoPoints);
        tvAssociationsPoints = view.findViewById(R.id.tvProfileAssociationsPoints);
        tvJumperPoints = view.findViewById(R.id.tvProfileJumperPoints);
        tvStepByStepPoints = view.findViewById(R.id.tvProfileStepByStepPoints);
        tvMyNumberPoints = view.findViewById(R.id.tvProfileMyNumberPoints);

        loadLoggedUserFromDatabase(loggedUserCacheData);
    }

    private void loadLoggedUserFromDatabase(AuthBearer loggedUserCacheData) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("users");
        Query loggedUserFinder = usersRef.orderByChild("username").equalTo(loggedUserCacheData.getUsername());

        loggedUserFinder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loggedUserDatabaseData = snapshot.child(loggedUserCacheData.getUsername()).getValue(User.class);

                    tvUsername.setText(loggedUserDatabaseData.getUsername());
                    tvEmail.setText(loggedUserDatabaseData.getEmail());

                    if (loggedUserDatabaseData.getPicture() != null)
                        imgProfile.setImageURI(Uri.parse(loggedUserDatabaseData.getPicture()));

                    int loggedPlayerWinrate = loggedUserDatabaseData.getPlayed() == 0 || loggedUserDatabaseData.getWon() == 0 ? 0 : (loggedUserDatabaseData.getWon() * 100) / loggedUserDatabaseData.getPlayed();

                    tvGamesPlayed.setText(String.valueOf(loggedUserDatabaseData.getPlayed()));
                    tvGamesWon.setText(String.valueOf(loggedPlayerWinrate));
                    tvWhoKnowsPoints.setText(String.valueOf(loggedUserDatabaseData.getPointsWhoKnows()));
                    tvConnectTwoPoints.setText(String.valueOf(loggedUserDatabaseData.getPointsConnectTwo()));
                    tvAssociationsPoints.setText(String.valueOf(loggedUserDatabaseData.getPointsAssociations()));
                    tvJumperPoints.setText(String.valueOf(loggedUserDatabaseData.getPointsJumper()));
                    tvStepByStepPoints.setText(String.valueOf(loggedUserDatabaseData.getPointsStepByStep()));
                    tvMyNumberPoints.setText(String.valueOf(loggedUserDatabaseData.getPointsMyNumber()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void chooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, getString(R.string.on_profile_pic_choosing)), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 200) {
                Uri selectedImgURI = data.getData();
                if (selectedImgURI != null) {
                    imgProfile.setImageURI(selectedImgURI);
                    this.selectedImageUri = selectedImgURI;
                    this.btnSavePic.setEnabled(true);
                }
            }

        }
    }

    private void saveProfilePic() {
//       Set new profile pic for Logged User
        btnSavePic.setEnabled(false);
//        loggedUserCacheData.setImageURI(this.selectedImageUri.toString());
        loggedUserDatabaseData.setPicture(selectedImageUri.toString());

        fbAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                .setPhotoUri(selectedImageUri)
                .build())
                .addOnCompleteListener(
                        task1 -> {
                            if (task1.isSuccessful()) {

                                DatabaseReference usersRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("users");
                                usersRef.child(loggedUserDatabaseData.getUsername()).child("picture").setValue(selectedImageUri.toString())
                                        .addOnCompleteListener(
                                                task2 -> {
                                                    Toast.makeText(getActivity(), R.string.on_profile_pic_save, Toast.LENGTH_SHORT).show();
                                                }
                                        );
                            }
                        }
                );
    }

}