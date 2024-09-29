package com.example.appstore.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Adapter.ProductAdapter;
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.Product;
import com.example.appstore.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    List<Product> listProduct ;
    RecyclerView recyclerView;
    TextView txtKq ;
    ProductAdapter productAdapter;
    CallApi callApi ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txtKq = findViewById(R.id.txtKq);
        recyclerView = findViewById(R.id.rcvSearch);
        listProduct = new ArrayList<>();
        callApi = new CallApi();
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
        callApi.getListProduct(new ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("ListPro", "onSuccess: " + response);
                try {

                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String, String>>() {}.getType();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        try {
                            String id_pro = jsonObject.getString("_id");
                            JSONObject jsonDetail = jsonObject.getJSONObject("details");
                            String name = jsonDetail.getString("name");
                            Integer price = Integer.valueOf(jsonDetail.getString("price"));
                            String img1 = jsonDetail.getString("img1");
                            String info = jsonDetail.getString("info");

                            JSONObject colorObject = jsonDetail.getJSONObject("color");
                            JSONObject sizeObject = jsonDetail.getJSONObject("size");

                            Map<String, String> colorMap = gson.fromJson(colorObject.toString(), type);
                            Map<String, String> sizeMap = gson.fromJson(sizeObject.toString(), type);

                            Product product = new Product(colorMap, sizeMap, price, name, "Ao", img1, "", info,id_pro);
                            if(name.toLowerCase().contains(keyword)){
                                listProduct.add(product);
                            }
                        } catch (JSONException e) {
                            Log.e("ListPro", "Error parsing JSON object at index " + i + ": " + e.getMessage());
                        }
                    }
                        productAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("ListPro", "Error parsing JSON response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("product", "onDataChange: " + errorMessage);
                // Optionally show an error message to the user
            }
        });

    }

    private void showKeySearchMenu(String keyword) {

    }


}