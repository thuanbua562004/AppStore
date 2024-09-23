package com.example.appstore.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Adapter.CartAdapter;
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.Cart;
import com.example.appstore.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    List<Cart> listCart  ;
    CallApi callApi = new CallApi();
    CartAdapter cartAdapter ;
    RecyclerView recyclerView;
    TextView txtToltalPrice ,txtTRong;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    Button btnBuy  ;
    ImageView btnBack ;
    LinearLayout thanhtoan ;
    int total = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        txtToltalPrice =findViewById(R.id.txtToltalPrice);
        thanhtoan = findViewById(R.id.thanhtoan);
        anhxa();
        listCart = new ArrayList<>();
        cartAdapter = new CartAdapter(this,listCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
        buyProduct();
        back();
        getCart();
    }

    public void getCart(){
        callApi.getCart(new ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONArray jsonDetails = jsonObject.getJSONArray("details");

                        for (int j = 0; j < jsonDetails.length(); j++) {
                            JSONObject jsonDetail = jsonDetails.getJSONObject(j);
                            String id_product = jsonDetail.getString("_id");
                            String nameProduct = jsonDetail.getString("nameProduct");
                            int number = jsonDetail.getInt("number");
                            String size = jsonDetail.getString("size");
                            String color = jsonDetail.getString("color");
                            String img = jsonDetail.getString("imgProduct");
                            int price = jsonDetail.getInt("price");
                            Cart cart = new Cart(nameProduct, id_product, number, size, color, img, price);

                            listCart.add(cart);
                        }
                    }
                } catch (JSONException e) {
                    Log.i("Cart", "error: "+e);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cartAdapter.notifyDataSetChanged();
                        checkCart();

                    }
                });

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void buyProduct() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listCart", (Serializable) listCart);
                bundle.putSerializable("totalPrice",total );
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void checkCart() {
        if (listCart == null || listCart.isEmpty()) {
            Log.i("checkCart", "Giỏ hàng trống hoặc chưa được khởi tạo.");
            txtTRong.setVisibility(View.VISIBLE);
            txtTRong.setText("Giỏ Hàng Trống");
            thanhtoan.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < listCart.size(); i++) {
                Cart cart = listCart.get(i);
                total += cart.getPrice();
            }
                txtToltalPrice.setText("Tạm tính: " + vn.format(total)+"d");
        }
    }



    private void anhxa() {
        recyclerView = findViewById(R.id.rcvCart);
        btnBuy =findViewById(R.id.btnCheckout);
        txtTRong =findViewById(R.id.txtIsemty);
        btnBack =findViewById(R.id.backBtn);
    }
    //////BACK
    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    }