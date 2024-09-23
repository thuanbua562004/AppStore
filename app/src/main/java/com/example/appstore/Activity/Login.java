package com.example.appstore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.User;
import com.example.appstore.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.muddz.styleabletoast.StyleableToast;

public class Login extends AppCompatActivity {
    Button btnRegister , btnLogin ,btnForgot ;
    ImageButton btnLoginGG ;
    EditText edtEmail , edtPass ;
    String email ,pass ;
    FirebaseAuth  mAuth ;
    private DatabaseReference mDatabase;
    CallApi apiUser = new CallApi();

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        anhxa();
        handler();

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

    }
    private void handler() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this , Register.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmail.getText().toString().trim();
                pass =edtPass.getText().toString().trim();
                if(!email.isEmpty() || !pass.isEmpty()){
                    if(email.contains("@gmail.com")){
                        showProgressDialog();

                        apiUser.getUser(email, pass, new ApiCallback() {
                            @Override
                            public void onSuccess(String response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(response != null && response.length() == 0) {
                                            StyleableToast.makeText(Login.this, "Đăng Nhập Thất Bại. Vui Lòng Kiểm Tra Tài Khoản Hoặc Mật Khẩu !", Toast.LENGTH_LONG, R.style.fail).show();
                                            return;
                                        }
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String id = jsonObject.getString("_id");
                                            JSONObject jsonDetail = jsonObject.getJSONObject("details");
                                            String email = jsonDetail.getString("email");
                                            String password = jsonDetail.getString("password");
                                            String adress1 = jsonDetail.getString("address");
                                            String dateBirth = jsonDetail.getString("dateBirth") ;
                                            String name  = jsonDetail.getString("name");
                                            String phoneNumber = jsonDetail.getString("phoneNumber");
                                         /// goi va luu ss
                                            session(id,email,password,adress1,name,phoneNumber,dateBirth);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Log.i("TAG1", "run  : "+ response);
                                        StyleableToast.makeText(Login.this, "Đăng Nhập Thành Công !", Toast.LENGTH_LONG, R.style.success).show();
                                        hideProgressDialog();
                                        startActivity(new Intent(Login.this , MainActivity.class));
                                        finishAffinity();

                                    }
                                });
                            }

                            @Override
                            public void onError(String errorMessage) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideProgressDialog();
                                        StyleableToast.makeText(Login.this, "Đăng Nhập Thất Bại. Vui Lòng Kiểm Tra Tài Khoản Hoặc Mật Khẩu !", Toast.LENGTH_LONG, R.style.fail).show();                            }
                                });
                            }
                        });

                    }else {
                        StyleableToast.makeText(Login.this, "Email Không Đúng Định Dạng !", Toast.LENGTH_LONG, R.style.fail).show();
                    }
                }else {
                    StyleableToast.makeText(Login.this, "Vui Lòng  Điền Đày Đủ Thông Tin !", Toast.LENGTH_LONG, R.style.fail).show();
                }
            }
        });


        //// Login bang GG
        btnLoginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(Login.this, result -> {
                            try {
                                startIntentSenderForResult(
                                        result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                        null, 0, 0, 0);
                            } catch (Exception e) {
                                Log.e("TAG1", "Could not start One Tap UI: " + e.getLocalizedMessage());
                                StyleableToast.makeText(Login.this, "Vui Lòng Đăng Nhập Tài Khoản Google Vào Thiết Bị!", Toast.LENGTH_LONG, R.style.fail).show();
                            }
                        })
                        .addOnFailureListener(Login.this, e -> {
                            // Handle error
                            Log.e("TAG1", "One Tap sign-in failed: " + e.getLocalizedMessage());
                            StyleableToast.makeText(Login.this, "Vui Lòng Đăng Nhập Tài Khoản Google Vào Thiết Bị !", Toast.LENGTH_LONG, R.style.fail).show();
                        });
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this , FogotPass.class));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = googleCredential.getGoogleIdToken();
                if (idToken != null) {
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    mAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    Log.d("TAG1", "signInWithCredential:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String uid = user.getUid();
                                    //////THEM
                                    updateUI(user);
                                } else {
                                    Log.w("TAG1", "signInWithCredential:failure", task.getException());
                                    updateUI(null);
                                }
                            });
                }
            } catch (ApiException e) {
                // Handle error
                Log.e("TAG1", "One Tap sign-in failed: " + e.getLocalizedMessage());
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in
            Log.d("TAG1", "User signed in: " + user.toString());
            startActivity(new Intent(Login.this , MainActivity.class));
            StyleableToast.makeText(Login.this, "Đăng Nhập Thành Công !", Toast.LENGTH_LONG, R.style.success).show();
            finishAffinity();
        } else {
            Log.d("TAG1", "User is signed out");
            StyleableToast.makeText(Login.this, "Đăng Nhập Thất Bại", Toast.LENGTH_LONG, R.style.fail).show();
        }
    }
    public void writeNewUser(String userId, String name, String adress,String dateBirth , String phoneNumber) {
        User user = new User(name,adress,dateBirth,phoneNumber);
        mDatabase.child("users").child(userId).setValue(user);
    }
    private void anhxa() {
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassWord);
        btnLoginGG = findViewById(R.id.loginWithGG);
        btnForgot = findViewById(R.id.btnforgot);
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
    private  void session (String id ,String email, String password, String adress ,String name ,String phoneNumber, String dateBirth){
        SharedPreferences sharedPreferences = getSharedPreferences("AppStore", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("email",email);
        editor.putString("password",password);
        editor.putString("adress", adress);
        editor.putString("name",name);
        editor.putString("phoneNumber",phoneNumber);
        editor.putString("date", dateBirth);
        editor.apply();
    }
}
