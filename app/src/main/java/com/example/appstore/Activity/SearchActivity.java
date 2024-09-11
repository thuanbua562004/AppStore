package com.example.appstore.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Adapter.ProductAdapter;
import com.example.appstore.Model.Product;
import com.example.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    List<Product> listProduct ;
    RecyclerView recyclerView;
    TextView txtKq ;
    ProductAdapter productAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txtKq = findViewById(R.id.txtKq);
        recyclerView = findViewById(R.id.rcvSearch);
        listProduct = new ArrayList<>();

        Intent intent = getIntent();
        String keySearch = intent.getStringExtra("keySearch");
        if (keySearch.equals("Ao")||keySearch.equals("Quan")||keySearch.equals("Vay")||keySearch.equals("Ao Khoac"))
        {
            showKeySearchMenu(keySearch);
        }else {
            showKeySearch(keySearch);
        }


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, listProduct);
        recyclerView.setAdapter(productAdapter);

    }


    private void showKeySearch(String keyword) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");

        String lowerKeyword = keyword.toLowerCase();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                boolean foundProduct = false;
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        if (product != null && product.getName() != null) {
                            if (product.getName().toLowerCase().contains(lowerKeyword)) {
                                listProduct.add(product);
                                foundProduct = true;
                            }
                        }
                    }
                    if (foundProduct) {
                        txtKq.setText("Tìm thấy sản phẩm có tên chứa: " + keyword);
                    } else {
                        txtKq.setText("Không tìm thấy sản phẩm có tên chứa: " + keyword);
                    }
                } else {
                    txtKq.setText("Không tìm thấy sản phẩm có tên chứa: " + keyword);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG1", "Lỗi khi lấy dữ liệu: " + error.getMessage());
            }
        });
    }

    private void showKeySearchMenu(String keyword) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");

        String lowerKeyword = keyword.toLowerCase();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                boolean foundProduct = false;
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        if (product != null && product.getName() != null) {
                            if (product.getType().toLowerCase().contains(lowerKeyword)) {
                                listProduct.add(product);
                                foundProduct = true;
                            }
                        }
                    }
                    if (foundProduct) {
                        txtKq.setText("Danh Muc: " + keyword);
                    } else {
                        txtKq.setText("Không tìm thấy sản phẩm trong Danh Muc: " + keyword);
                    }
                } else {
                    txtKq.setText("Không tìm thấy sản phẩm trong Danh Muc: " + keyword);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG1", "Lỗi khi lấy dữ liệu: " + error.getMessage());
            }
        });
    }


}