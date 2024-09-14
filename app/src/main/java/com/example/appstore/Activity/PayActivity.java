package com.example.appstore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Adapter.PayAdapter;
import com.example.appstore.Api.CreateOrder;
import com.example.appstore.Model.Cart;
import com.example.appstore.Model.HistoryBuy;
import com.example.appstore.Model.User;
import com.example.appstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.github.muddz.styleabletoast.StyleableToast;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PayActivity extends AppCompatActivity {
    private List<Cart> list;
    private RecyclerView rcvBuy;
    private PayAdapter payAdapter;
    private int totalPrice = 0;
    private TextView txtTotal, txtTotalTienhang;
    private Locale localeVN = new Locale("vi", "VN");
    private NumberFormat vn = NumberFormat.getInstance(localeVN);
    private RadioGroup radioGroup;
    private Button checkOut;
    ImageView imageBack;
    private HistoryBuy historyBuy;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    EditText edtphoneNumber ,edtadress ;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_avtivity);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(553, Environment.SANDBOX);
        // bind components with ids

        initializeViews();
        getListBuy();
        infoClient();
        setupRecyclerView();
        setPrice();
        setupCheckoutButton();
        back();
    }
    private void initializeViews() {
        rcvBuy = findViewById(R.id.rcvbuy);
        txtTotalTienhang = findViewById(R.id.totalFeeTxt);
        txtTotal = findViewById(R.id.totalTxt);
        radioGroup = findViewById(R.id.radioMethodPay);
        checkOut = findViewById(R.id.btnCheckout);
        imageBack = findViewById(R.id.backBtn);
        edtphoneNumber = findViewById(R.id.editTextPhoneNumber);
        edtadress =findViewById(R.id.adress);
    }

    private void setupRecyclerView() {
        rcvBuy.setLayoutManager(new GridLayoutManager(this, 1));
        payAdapter = new PayAdapter(this, list);
        rcvBuy.setAdapter(payAdapter);
    }

    private void getListBuy() {
        Intent intent = getIntent();
        if (intent == null) return;

        list = (List<Cart>) intent.getSerializableExtra("listCart");
        totalPrice = (int) intent.getSerializableExtra("totalPrice");

        Log.i("list", "getListBuy: " + list);
        if (payAdapter != null) {
            payAdapter.notifyDataSetChanged();
        }
    }
    public void infoClient (){
        String id = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef =database.getReference("users/"+id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String phone = user.getPhoneNumber();
                String adress = user.getAddress();
                edtadress.setText(adress);
                edtphoneNumber.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setPrice() {
        txtTotalTienhang.setText(vn.format(totalPrice) + " VND");
        txtTotal.setText(vn.format(totalPrice + 25000) + " VND");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupCheckoutButton() {
        checkOut.setOnClickListener(v -> {
         if(edtadress.getText().toString().trim().equals("") ||edtphoneNumber.getText().toString().trim().equals("") ){
             StyleableToast.makeText(PayActivity.this, "Vui lòng  không để  trắng thông tin!", Toast.LENGTH_LONG, R.style.success).show();
             return;
         }
            showProgressDialog();
            String methodPay = getSelectedPaymentMethod();
            if (methodPay.equals("Qua Ngân Hàng")) {
                getPayMoMo();
            } else if (methodPay.equals("Tiền mặt")) {
                saveHistoryBuy("Tiền mặt");
                startActivity(new Intent(PayActivity.this ,PaySuccess.class));
                finish();
                StyleableToast.makeText(PayActivity.this, "Đặt Hàng Thành Công!", Toast.LENGTH_LONG, R.style.success).show();

            }
        });
    }

    private String getSelectedPaymentMethod() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            hideProgressDialog();
            StyleableToast.makeText(PayActivity.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_LONG, R.style.success).show();
            return "";
        } else {
            RadioButton radioSelect = findViewById(selectedId);
            return radioSelect.getText().toString();
        }
    }

    private void getPayMoMo() {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(String.valueOf(totalPrice+25000));
            String code = data.getString("returncode");
            StyleableToast.makeText(PayActivity.this, "Đang Chuyển Hướng ! ", Toast.LENGTH_LONG, R.style.success).show();

            if (code.equals("1")) {
                String token = data.getString("zptranstoken");
                ZaloPaySDK.getInstance().payOrder(PayActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        StyleableToast.makeText(PayActivity.this, "Thanh Toán Thành Công!", Toast.LENGTH_LONG, R.style.success).show();
                        saveHistoryBuy("Qua Ngân Hàng");
                        startActivity(new Intent(PayActivity.this ,PaySuccess.class));
                        finish();
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        StyleableToast.makeText(PayActivity.this, "Thanh Toán Thất Bại!", Toast.LENGTH_LONG, R.style.fail).show();
                        hideProgressDialog();
                        startActivity(new Intent(PayActivity.this ,PayFail.class));
                        finish();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        StyleableToast.makeText(PayActivity.this, "Thanh Toán Thất Bại!", Toast.LENGTH_LONG, R.style.fail).show();
                        hideProgressDialog();
                        startActivity(new Intent(PayActivity.this ,PayFail.class));
                        finish();
                    }
                });
            }


        } catch (Exception e) {
            hideProgressDialog();
            e.printStackTrace();
        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveHistoryBuy(String methodPay) {
        if (user == null) return;

        Random random = new Random();
        int key = random.nextInt();
        String id = user.getUid() + "_" + key;
        String adress ,phoneNumber ;
        adress = edtadress.getText().toString();
        phoneNumber = edtphoneNumber.getText().toString();

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dateTimeString = currentDateTime.format(formatter);

        historyBuy = new HistoryBuy(user.getDisplayName(), totalPrice+25000, list, adress,phoneNumber,dateTimeString ,methodPay);
        mDatabase.child("history_buy").child(id).setValue(historyBuy);
        mDatabase.child("cart").child(user.getUid()).removeValue();
        hideProgressDialog();
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

    private void back() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}