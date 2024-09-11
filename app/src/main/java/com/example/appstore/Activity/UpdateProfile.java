package com.example.appstore.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.appstore.Model.User;
import com.example.appstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.muddz.styleabletoast.StyleableToast;

public class UpdateProfile extends AppCompatActivity {
    Button btnUpdate  ;
    ImageButton btnChangeImg ;
    ImageView back ;
    Bitmap bitmap ;
    EditText edtName , edtPhoneNumber ,edtDiaChi ,edtNgaySinh ;
    private DatabaseReference mDatabase;
    private static final int REQUEST_CODE_PICK_IMAGE = 2;
    Uri uri ;
    FirebaseUser user ;
    String uid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        anhxa();
        handler();
        user = FirebaseAuth.getInstance().getCurrentUser();

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
                checkUserIsNull();
                Log.i("uri", "uri" + uri);
                   UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                           .setPhotoUri(uri)
                           .build();

                   user.updateProfile(profileUpdates)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                   }
                               }
                           });
            }
        });
    }

    private void checkUserIsNull() {
        uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + uid);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameProfile = edtName.getText().toString().trim();
                String address = edtDiaChi.getText().toString().trim();
                String dateBirth = edtNgaySinh.getText().toString().trim();
                String phoneNumber = edtPhoneNumber.getText().toString().trim();

                User user = snapshot.getValue(User.class);
                if (user != null) {
                    String name = user.getName();
                    String phone = user.getPhoneNumber();
                    String address1 = user.getAddress();
                    String date = user.getDateBirth();
                    Log.i("TAG1", "onDataChange: " + user.toString());
                    if(nameProfile.equals("")&&address.equals("") &&dateBirth.equals("") && phoneNumber.equals("")){
                        StyleableToast.makeText(UpdateProfile.this, "Vui Lòng Điền Tên && Số Điện Thoại && Địa Chỉ!", Toast.LENGTH_LONG, R.style.success).show();

                    }else {
                        // Use database values if input fields are empty
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

                        // Proceed to write the new user data
                        writeNewUser(uid, nameProfile, address, dateBirth, phoneNumber);
                        myRef.removeEventListener(this);
                    }


                } else {
                    Log.e("TAG1", "User data is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log the error if the data retrieval is cancelled
                Log.e("TAG1", "Database error: " + error.getMessage());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
             uri = data.getData();
            if (uri != null) {
                try {
                    ImageButton imageView = findViewById(R.id.openpicture);
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

    public void writeNewUser(String userId, String name, String adress,String dateBirth , String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =database.getReference("users/"+userId);
        User user = new User(name,phoneNumber,adress,dateBirth);
        myRef.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                StyleableToast.makeText(UpdateProfile.this, "Thay Đổi Thành Công!", Toast.LENGTH_LONG, R.style.success).show();
            }
        });
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
}