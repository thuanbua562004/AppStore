package com.example.appstore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.github.muddz.styleabletoast.StyleableToast;

public class Register extends AppCompatActivity {
    private EditText txtEmail, txtPass, txtRePass;
    private Button btnRegister , btnBackLogin;
    private ProgressDialog progressDialog;
    CallApi callApiUser = new CallApi();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        anhxa();
        handler();
    }

    private void handler() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String pass = txtPass.getText().toString().trim();
                String rePass = txtRePass.getText().toString().trim();
                if(pass.length()<6){
                    StyleableToast.makeText(Register.this, "Yeu Cau Do Dai Mat Khau Bang 6.", Toast.LENGTH_LONG, R.style.fail).show();
                    return;
                }
                if (!email.isEmpty() && !pass.isEmpty() && !rePass.isEmpty()) {
                    if (pass.equals(rePass)) {
                        showProgressDialog();
                        mAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        hideProgressDialog();
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            String id = user.getUid();
                                            String email = user.getEmail();
                                            createUserMonggo(id,pass,email);
                                        } else {
                                            hideProgressDialog();
                                            StyleableToast.makeText(Register.this, "Đăng Kí Thất Bại! Vui Lòng Thử Lại.", Toast.LENGTH_LONG, R.style.fail).show();
                                        }
                                    }
                                });
                    } else {
                        StyleableToast.makeText(Register.this, "Mật Khẩu Không Trùng!", Toast.LENGTH_LONG, R.style.fail).show();
                    }
                } else {
                    StyleableToast.makeText(Register.this, "Vui Lòng Điền Đầy Đủ Thông Tin!", Toast.LENGTH_LONG, R.style.fail).show();
                }
            }
        });


        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this , Login.class));
            }
        });
    }
    public void createUserMonggo(String id,String pass ,String email){
        callApiUser.createUser(email, pass,id, new ApiCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StyleableToast.makeText(Register.this, "Đăng Kí Thành Công!", Toast.LENGTH_LONG, R.style.success).show();
                        hideProgressDialog();
                        startActivity(new Intent(Register.this, Login.class));
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StyleableToast.makeText(Register.this, errorMessage, Toast.LENGTH_LONG, R.style.fail).show();
                        hideProgressDialog();
                    }
                });

            }
        });
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
