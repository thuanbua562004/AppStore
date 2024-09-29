package com.example.appstore.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.appstore.Interface.CartChangeListener;
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

public class CartActivity extends AppCompatActivity implements CartChangeListener {
    List<Cart> listCart;
    CallApi callApi = new CallApi();
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    TextView txtToltalPrice, txtTRong;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    Button btnBuy;
    ImageView btnBack;
    LinearLayout thanhtoan;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Ánh xạ các thành phần giao diện
        anhxa();

        listCart = new ArrayList<>();
        cartAdapter = new CartAdapter(this, listCart, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        buyProduct();
        back();

        SharedPreferences sharedPreferences = getSharedPreferences("AppStore", MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", "");
        getCart(userId);
    }

    // Lấy dữ liệu giỏ hàng từ API
    public void getCart(String userID) {
        callApi.getCart(userID, new ApiCallback() {
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
                            listCart.add(cart);  // Thêm sản phẩm vào listCart
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Cart", "Error: " + e.getMessage());
                }

                // Cập nhật giao diện trên luồng UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cartAdapter.notifyDataSetChanged();  // Thông báo cho Adapter để cập nhật dữ liệu
                        checkCart();  // Kiểm tra giỏ hàng trống hay không và tính tổng tiền
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Cart", "API Error: " + errorMessage);
            }
        });
    }
    private void checkCart() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listCart == null || listCart.isEmpty()) {
                    Log.i("checkCart", "Giỏ hàng trống hoặc chưa được khởi tạo.");
                    txtTRong.setVisibility(View.VISIBLE);
                    txtTRong.setText("Giỏ Hàng Trống");
                    thanhtoan.setVisibility(View.GONE);
                } else {
                    total = 0;  // Reset total before calculation
                    for (int i = 0; i < listCart.size(); i++) {
                        Cart cart = listCart.get(i);
                        total += cart.getPrice() * cart.getNumber(); // Calculate total based on quantity
                    }
                    txtToltalPrice.setText("Tạm tính: " + vn.format(total) + " VND");
                    txtTRong.setVisibility(View.GONE); // Hide empty cart message
                    thanhtoan.setVisibility(View.VISIBLE); // Show checkout button
                }
            }
        });
    }


    // Phương thức xử lý khi nhấn nút mua hàng
    private void buyProduct() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listCart", (Serializable) listCart);  // Truyền danh sách giỏ hàng
                bundle.putSerializable("totalPrice", total);  // Truyền tổng tiền
                intent.putExtras(bundle);
                startActivity(intent);  // Chuyển sang màn hình thanh toán
            }
        });
    }

    // Ánh xạ các thành phần giao diện
    private void anhxa() {
        recyclerView = findViewById(R.id.rcvCart);
        btnBuy = findViewById(R.id.btnCheckout);
        txtTRong = findViewById(R.id.txtIsemty);
        btnBack = findViewById(R.id.backBtn);
        txtToltalPrice = findViewById(R.id.txtToltalPrice);
        thanhtoan = findViewById(R.id.thanhtoan);
    }

    // Phương thức xử lý khi nhấn nút quay lại
    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Cập nhật giỏ hàng khi có thay đổi
    @Override
    public void onCartChanged() {
        checkCart();  // Gọi lại checkCart để tính toán lại tổng và cập nhật giao diện
    }
}
