package com.example.appstore.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.R;
import com.google.firebase.database.DatabaseReference;

import io.github.muddz.styleabletoast.StyleableToast;

public class UpdateProfile extends AppCompatActivity {
    Button btnUpdate  ;
    ImageButton btnChangeImg ;
    ImageView back ;
    EditText edtName , edtPhoneNumber ,edtDiaChi ,edtNgaySinh ;
    private DatabaseReference mDatabase;
    private static final int REQUEST_CODE_PICK_IMAGE = 2;
    CallApi apiUser = new CallApi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
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
            }
        });
    }
    public void checkUpdate (){
        String nameProfile = edtName.getText().toString().trim();
        String address = edtDiaChi.getText().toString().trim();
        String dateBirth = edtNgaySinh.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("AppStore",MODE_PRIVATE);
        String user = sharedPreferences.getString("id","");
        Log.i("updateUser", "checkUpdate: " + user);
            String name = sharedPreferences.getString("name","");
            String phone = sharedPreferences.getString("phoneNumber","");
            String address1 = sharedPreferences.getString("adress","");
            String date = sharedPreferences.getString("date","");
            if (nameProfile.equals("") && address.equals("") && dateBirth.equals("") && phoneNumber.equals("")) {
                StyleableToast.makeText(UpdateProfile.this, "Vui Lòng Điền Tên && Số Điện Thoại && Địa Chỉ!", Toast.LENGTH_LONG, R.style.success).show();

            } else {
                if (nameProfile.isEmpty()) {
                    nameProfile = name;
                }
                if (address.isEmpty()) {
                    address = address1;
                }
                if (dateBirth.isEmpty()) {
                    dateBirth = date;
                }
                if (phoneNumber.isEmpty()) {
                    phoneNumber = phone;
                }
                session(address,nameProfile,phoneNumber,dateBirth);
                apiUser.updateUser(user, nameProfile, address, dateBirth, phoneNumber, new ApiCallback() {
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
        edtNgaySinh = findViewById(R.id.txtnamsinh);
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
}
