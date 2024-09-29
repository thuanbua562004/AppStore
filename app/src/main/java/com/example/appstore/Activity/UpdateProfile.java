package com.example.appstore.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import io.github.muddz.styleabletoast.StyleableToast;

public class UpdateProfile extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    Button btnUpdate  ;
    ImageView btnChangeImg ;
    ImageButton button;
    ImageView back ;
    EditText edtName , edtPhoneNumber ,edtDiaChi  ,edtDate;
    private DatabaseReference mDatabase;
    private static final int REQUEST_CODE_PICK_IMAGE = 2;
    CallApi apiUser = new CallApi();
    Uri uri ;
    FirebaseUser user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();

        anhxa();
        handler();
    }

    private void handler() {
        btnChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate();

                if(uri != null && !uri.equals("")){
                    String  uid = user.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users/" + uid);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                }


            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog  = new DatePickerDialog(UpdateProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }
    public void checkUpdate (){


        String nameProfile = edtName.getText().toString().trim();
        String address = edtDiaChi.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String getDate = edtDate.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("AppStore",MODE_PRIVATE);
        String user = sharedPreferences.getString("id","");
        Log.i("updateUser", "checkUpdate: " + user);
            String name = sharedPreferences.getString("name","");
            String phone = sharedPreferences.getString("phoneNumber","");
            String address1 = sharedPreferences.getString("adress","");
            String date = sharedPreferences.getString("date","");
            if (nameProfile.equals("") && address.equals("") && edtDate.equals("") && phoneNumber.equals("")) {
                StyleableToast.makeText(UpdateProfile.this, "Vui Lòng Điền Tên && Số Điện Thoại && Địa Chỉ!", Toast.LENGTH_LONG, R.style.success).show();

            } else {
                if (nameProfile.isEmpty()) {
                    nameProfile = name;
                }
                if (address.isEmpty()) {
                    address = address1;
                }
                if (getDate.isEmpty()) {
                    getDate = date;
                }
                if (phoneNumber.isEmpty()) {
                    phoneNumber = phone;
                }
                session(address,nameProfile,phoneNumber,getDate);
                apiUser.updateUser(user, nameProfile, address, getDate, phoneNumber, new ApiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("updateUser", "onSuccess: "+ response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StyleableToast.makeText(UpdateProfile.this, "Succesfully!", Toast.LENGTH_LONG, R.style.success).show();
                                startActivity(new Intent(UpdateProfile.this, MainActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.i("updateUser", "onSuccess: "+ errorMessage);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StyleableToast.makeText(UpdateProfile.this, "Fail!", Toast.LENGTH_LONG, R.style.success).show();
                            }
                        });
                    }
                });

            }
        }
    private void anhxa() {
        btnUpdate = findViewById(R.id.update);
        btnChangeImg = findViewById(R.id.openpicture);
        back =findViewById(R.id.back);
        edtName =findViewById(R.id.txtname);
        edtPhoneNumber =findViewById(R.id.phonenumber);
        edtDiaChi = findViewById(R.id.adress);
        edtDate = (EditText) findViewById(R.id.date);
         button = findViewById(R.id.edtDate);
    }
    private  void session ( String adress ,String name ,String phoneNumber, String dateBirth){
        SharedPreferences sharedPreferences = getSharedPreferences("AppStore", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("adress", adress);
        editor.putString("name",name);
        editor.putString("phoneNumber",phoneNumber);
        editor.putString("date", dateBirth);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            if (uri != null) {
                try {
                    ImageView imageView = findViewById(R.id.openpicture);
                    Glide.with(this)
                            .load(uri)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
