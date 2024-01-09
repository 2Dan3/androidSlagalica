package com.ftn.slagalica;

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
import com.ftn.slagalica.util.AuthHandler;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private Button btnSavePic;
    private Uri selectedImageUri;

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

        AuthBearer loggedUser = AuthHandler.Login.getLoggedPlayerAuth(getActivity());

        imgProfile = view.findViewById(R.id.imgProfile);

//        Todo : uncomment to set real ProfilePic, when we start using smaller size URI icons
//        imgProfile.setImageURI(Uri.parse(loggedUser.getImageURI()));

        imgProfile.setOnClickListener(imgView -> {
            chooseImage();
        });

        btnSavePic = view.findViewById(R.id.btnSavePic);
        btnSavePic.setOnClickListener(btnSave -> {
//       Set new profile pic for Logged User in session
            btnSave.setEnabled(false);
            loggedUser.setImageURI(this.selectedImageUri.toString());

            Toast.makeText(getActivity(), R.string.on_profile_pic_save, Toast.LENGTH_SHORT).show();

//       Todo : save new profile Pic to Database
//                      ...


        });

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(logoutBtn -> {
            ( (MainActivity) getActivity() ).logout();
        });

        TextView tvUsername = view.findViewById(R.id.textViewUsernameProfile);
        tvUsername.setText(loggedUser.getUsername());

        TextView tvEmail = view.findViewById(R.id.textViewEmailProfile);
        tvEmail.setText(loggedUser.getEmail());
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

}