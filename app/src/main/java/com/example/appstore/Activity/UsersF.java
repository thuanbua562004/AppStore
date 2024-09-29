package com.example.appstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsersF extends Fragment {
    TextView txtName, email, phonenumber, txtaddress, txtdate;
    ImageView imgUser, imgCart, imghistory, imgUpdate;
    Button btnUpdateProfile, imgLogout;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        anhxa(view);
        handler(view);
        getUser();
        return view;
    }
    public void getUser(){
         SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppStore", Context.MODE_PRIVATE);
         String emailss = sharedPreferences.getString("email","");
        String name = sharedPreferences.getString("name","");
        String phone = sharedPreferences.getString("phoneNumber","");
        String address = sharedPreferences.getString("adress","");
        String date =sharedPreferences.getString("date","");
        try {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imgUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtName.setText(name != null && !name.isEmpty() ? name : "Name Profile");
        phonenumber.setText(phone != null && !phone.isEmpty() ? phone : "PhoneNumber Null");
        txtaddress.setText(address != null && !address.isEmpty() ? address : "Address Null");
        txtdate.setText(date != null && !date.isEmpty() ? date : "Date Null");
        email.setText(emailss);
    }


    private void handler(View view) {
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppStore", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getContext(), Splash.class));
                requireActivity().finishAffinity();
            }
        });

        imgCart.setOnClickListener(v -> startActivity(new Intent(getContext(), CartActivity.class)));
        imghistory.setOnClickListener(v -> startActivity(new Intent(getContext(), HistoryBuyActivity.class)));
        imgUpdate.setOnClickListener(v -> startActivity(new Intent(getContext(), UpdateProfile.class)));

    }

    private void anhxa(View view) {
        txtdate = view.findViewById(R.id.date);
        imgLogout = view.findViewById(R.id.imageLogout);
        txtName = view.findViewById(R.id.titleName);
        email = view.findViewById(R.id.email);
        txtaddress = view.findViewById(R.id.adress);
        phonenumber = view.findViewById(R.id.phonenumber);
        imgUser = view.findViewById(R.id.profileImg);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        imgCart = view.findViewById(R.id.imgCart);
        imghistory = view.findViewById(R.id.history);
        imgUpdate = view.findViewById(R.id.update);
    }
}
