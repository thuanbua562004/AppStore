package com.example.appstore.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appstore.Model.User;
import com.example.appstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.muddz.styleabletoast.StyleableToast;

public class UsersF extends Fragment {
    TextView txtName, email, phonenumber, txtaddress, txtdate;
    ImageView imgUser, imgCart, imghistory, imgUpdate;
    Button btnUpdateProfile, imgLogout;
    DatabaseReference mPostReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        mPostReference = FirebaseDatabase.getInstance().getReference();
        anhxa(view);
        handler(view);
        getUserInFirebase();
        return view;
    }

    private void getUserInFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    String name = user.getName();
                    String phone = user.getPhoneNumber();
                    String address = user.getAddress();
                    String date = user.getDateBirth();
                    Log.i("TAG1", "onDataChange: " + user.toString());

                    txtName.setText(name != null && !name.isEmpty() ? name : "Name Profile");
                    phonenumber.setText(phone != null && !phone.isEmpty() ? phone : "PhoneNumber Null");
                    txtaddress.setText(address != null && !address.isEmpty() ? address : "Address Null");
                    txtdate.setText(date != null && !date.isEmpty() ? date : "Date Null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });

        for (com.google.firebase.auth.UserInfo profile : user.getProviderData()) {
            Uri photoUrl = profile.getPhotoUrl();
            String emailuser = profile.getEmail();

            email.setText(emailuser != null && !emailuser.isEmpty() ? emailuser : "NO Valid");

            Glide.with(getContext())
                    .load(photoUrl != null ? photoUrl : R.drawable.user)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imgUser);
        }
    }

    private void handler(View view) {
        imgLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            StyleableToast.makeText(getContext(), "Đăng Xuất!", Toast.LENGTH_LONG, R.style.success).show();
            startActivity(new Intent(getContext(), Splash.class));
            getActivity().finishAffinity();
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
