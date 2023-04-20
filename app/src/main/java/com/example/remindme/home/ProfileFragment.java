package com.example.remindme.home;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.remindme.LoginActivity;
import com.example.remindme.MyPreferences;
import com.example.remindme.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    ImageView ivProfile, ivEditProfile;
    EditText etName, etEmail, etAge, etNumber;
    SwitchCompat scNotification;
    Button btnLogin;
    MyPreferences myPreferences;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myPreferences = MyPreferences.getPreferences(getContext());

        ivEditProfile = view.findViewById(R.id.ivEditProfile);
        ivProfile = view.findViewById(R.id.ivProfile);
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etAge = view.findViewById(R.id.etAge);
        etNumber = view.findViewById(R.id.etNumber);
        scNotification = view.findViewById(R.id.scNotification);
        btnLogin = view.findViewById(R.id.btnLogin);

        etName.setText(myPreferences.getUserName());
        etEmail.setText(myPreferences.getEmail());
        etAge.setText(myPreferences.getAge());
        etNumber.setText(myPreferences.getMobileNumber());

        scNotification.setOnCheckedChangeListener(this);

        scNotification.setChecked(myPreferences.isNotification());

        Glide.with(ivProfile).load(myPreferences.getImage()).circleCrop().placeholder(R.drawable.demo_profile).into(ivProfile);

        btnLogin.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            myPreferences.clear();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        ivEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                    Glide.with(ivProfile).load(selectedImageUri).placeholder(R.drawable.demo_profile).circleCrop().into(ivProfile);

                    String path  = saveToCacheMemory(getActivity(),ivProfile.getDrawingCache());
                    Log.d("PATH",path);
                    myPreferences.setImage(path);
                }
            }
        }
    }

    public static String saveToCacheMemory(Activity activity, Bitmap bitmapImage){

        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

        ContextWrapper cw = new ContextWrapper(activity);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,mDateFormat.format(new Date())  + System.currentTimeMillis() + ".png");

        Log.d("TAG", "directory: " + directory);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.d("TAG", "bit exception: Success" );
        } catch (Exception e) {
            Log.d("TAG", "bit exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG", "io exce: " + e.getMessage());
            }
        }
        Log.d("TAG", "absolute path " + directory.getAbsolutePath());
        return mypath.getAbsolutePath();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myPreferences.setEmail(etEmail.getText().toString());
        myPreferences.setUserName(etName.getText().toString());
        myPreferences.setAge(etAge.getText().toString());
        myPreferences.setMobileNumber(etNumber.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        myPreferences.setIsNotification(isChecked);
    }
}