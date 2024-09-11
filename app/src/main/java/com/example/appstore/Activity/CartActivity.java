package com.example.appstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Adapter.CartAdapter;
import com.example.appstore.Model.Cart;
import com.example.appstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    List<Cart> listCart  ;

    CartAdapter cartAdapter ;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String id_cart;
    TextView txtToltalPrice ,txtTRong;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    Button btnBuy  ;
    ImageView btnBack ;
    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        txtToltalPrice =findViewById(R.id.txtToltalPrice);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        id_cart = user.getUid();
        listCart = new ArrayList<>();
        getCart();
        anhxa();
        cartAdapter = new CartAdapter(this,listCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
        buyProduct();
        back();
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

    private void anhxa() {
        recyclerView = findViewById(R.id.rcvCart);
        btnBuy =findViewById(R.id.btnCheckout);
        txtTRong =findViewById(R.id.txtIsemty);
        btnBack =findViewById(R.id.backBtn);
    }

    private void getCart() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(id_cart);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCart.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    // Loại trừ nút 'items' (nếu có)
                    if (!productSnapshot.getKey().equals("items")) {
                        Cart cart = productSnapshot.getValue(Cart.class);
                        if (cart != null) {
                            listCart.add(cart);
                            Log.i("cart", "Added to list: " + cart.toString());
                        } else {
                            Log.e("FirebaseData", "Cart object is null for key: " + productSnapshot.getKey());
                        }
                    }
                }
                 total = 0;
                for (Cart cart : listCart) {
                    total += cart.getPrice() * cart.getNumber();
                }
                txtToltalPrice.setText("Tổng Tiền:  " + vn.format(total) + " VND");
                cartAdapter.notifyDataSetChanged();
                if (total==0){
                    btnBuy.setVisibility(View.GONE);
                    txtToltalPrice.setVisibility(View.GONE);
                    txtTRong.setText("Giỏ Hàng Trống!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "DatabaseError: " + databaseError.getMessage());
            }
        });
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