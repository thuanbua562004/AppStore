package com.example.appstore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appstore.Model.User;
import com.example.appstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.muddz.styleabletoast.StyleableToast;

public class Register extends AppCompatActivity {
    private EditText txtEmail, txtPass, txtRePass;
    private Button btnRegister , btnBackLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        anhxa();
        handler();
    }

    private void handler() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                String email = txtEmail.getText().toString().trim();
                String pass = txtPass.getText().toString().trim();
                String rePass = txtRePass.getText().toString().trim();

                if (!email.isEmpty() && !pass.isEmpty() && !rePass.isEmpty()) {
                    if (pass.equals(rePass)) {
                        mAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String uid = user.getUid();
                                            writeNewUser(uid,"","","","");
                                            // Sign in success, update UI with the signed-in user's information
                                            StyleableToast.makeText(Register.this, "Đăng Kí Thành Công!", Toast.LENGTH_LONG, R.style.success).show();
                                            hideProgressDialog();
                                            startActivity(new Intent(Register.this, Login.class));
                                        } else {
                                            Log.i("TAG1", "onComplete: "+ task.toString());
                                            StyleableToast.makeText(Register.this, "Đăng Kí Thất Bại. Tài Khoản Đã Tồn Tại!", Toast.LENGTH_LONG, R.style.fail).show();
                                            hideProgressDialog();
                                        }
                                    }
                                });
                    } else {
                        hideProgressDialog();
                        StyleableToast.makeText(Register.this, "Mật Khẩu Không Trùng !", Toast.LENGTH_LONG, R.style.fail).show();
                    }
                } else {
                    hideProgressDialog();
                    StyleableToast.makeText(Register.this, "Vui Lòng  Điền Đầy Đủ Thông Tin !", Toast.LENGTH_LONG, R.style.fail).show();                }
            }
        });

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this , Login.class));
            }
        });
    }
    public void writeNewUser(String userId, String name, String adress,String dateBirth , String phoneNumber) {
        User user = new User(name,adress,dateBirth,phoneNumber);
        mDatabase.child("users").child(userId).setValue(user);
        mDatabase.child("cart").setValue(userId);
    }

    private void anhxa() {
        txtEmail = findViewById(R.id.edtEmail);
        txtPass = findViewById(R.id.edtPassWord);
        txtRePass = findViewById(R.id.edtRePassWord);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackLogin = findViewById(R.id.btnBackLogin);
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dialog from being dismissed by back button
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
