package com.example.appstore.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import io.github.muddz.styleabletoast.StyleableToast;

public class FogotPass extends AppCompatActivity {
    EditText edtEmail  ;
    Button btnResset , btnBack ;
    FirebaseAuth mAuth;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_pass);
        anhxa();
        handler();
        mAuth = FirebaseAuth.getInstance();
    }

    private void handler() {
        btnResset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email = edtEmail.getText().toString().trim();
                if(!email.isEmpty()){
                    if(email.contains("@gmail.com")){
                        ResetPass();
                    }else {
                        StyleableToast.makeText(FogotPass.this, "Email Không Đúng Định Dạng !", Toast.LENGTH_LONG, R.style.fail).show();

                    }
                }else{
                    StyleableToast.makeText(FogotPass.this, "Vui Lòng  Điền Đầy Đủ Thông Tin !", Toast.LENGTH_LONG, R.style.fail).show();

                }

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public  void ResetPass (){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG1", "Email sent.");
                            StyleableToast.makeText(FogotPass.this, "Kiểm Tra Email Để Thay Đổi Mật Khẩu!", Toast.LENGTH_LONG, R.style.success).show();
                        }else {
                            StyleableToast.makeText(FogotPass.this, "Tài Khoản Chưa Được Đăng Kí", Toast.LENGTH_LONG, R.style.fail).show();
                        }
                    }
                });
    }


    private void anhxa() {
        edtEmail =findViewById(R.id.edtForgotPasswordEmail);
        btnResset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnForgotPasswordBack);
    }
}