package com.example.appstore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.R;

import io.github.muddz.styleabletoast.StyleableToast;

public class Register extends AppCompatActivity {
    private EditText txtEmail, txtPass, txtRePass;
    private Button btnRegister , btnBackLogin;
    private ProgressDialog progressDialog;
    CallApi callApiUser = new CallApi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                        callApiUser.createUser(email, pass, new ApiCallback() {
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
