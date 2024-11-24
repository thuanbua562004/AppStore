package com.example.appstore.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Adapter.AdapterHistoryBuy;
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.Cart;
import com.example.appstore.Model.HistoryBuy;
import com.example.appstore.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryBuyActivity extends AppCompatActivity {
    List<HistoryBuy> buyList;
    AdapterHistoryBuy adapterHistoryBuy;
    RecyclerView recyclerView;
    String idhistory;
    ImageButton btnBack;
    CallApi callApi;
    List<Cart> cartList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_buy);

        btnBack = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.rcvhHistoryBuy);
        callApi = new CallApi();

        buyList = new ArrayList<>();
        adapterHistoryBuy = new AdapterHistoryBuy(buyList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapterHistoryBuy);

        back();

        getId();

        getHistoryBuy();
    }

    private void getHistoryBuy() {
        callApi.getHistoryBuy(idhistory, new ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("History", "onSuccess: " + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String orderId = jsonObject.getString("_id");
                        int totalPrice = jsonObject.getInt("totalPrice");
                        String phone = jsonObject.getString("phone");
                        String date = jsonObject.getString("date");
                        String methodPay = jsonObject.getString("methodPayload");
                        String adress = jsonObject.getString("adress");

                        // Tạo danh sách giỏ hàng tạm thời cho từng đơn hàng
                        List<Cart> tempCartList = new ArrayList<>();

                        JSONArray listProduct = jsonObject.getJSONArray("listProduct");
                        for (int j = 0; j < listProduct.length(); j++) {
                            JSONObject product = listProduct.getJSONObject(j);
                            String productName = product.getString("nameProduct");
                            String productColor = product.getString("color");
                            int productPrice = product.getInt("price");
                            int productQuantity = product.getInt("number");
                            String productSize = product.getString("size");

                            Cart listBuy = new Cart(productName, productQuantity, productSize, productPrice);
                            tempCartList.add(listBuy);
                        }

                        // Tạo đối tượng HistoryBuy và thêm vào buyList
                        HistoryBuy historyBuy = new HistoryBuy(orderId, totalPrice, tempCartList, adress, phone, date, methodPay);
                        buyList.add(historyBuy);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterHistoryBuy.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    Log.e("JSON Error", "Error processing order data: " + e.getMessage());
                    e.printStackTrace(); // Print stack trace for more detail
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("API Error", "Error fetching history: " + errorMessage);
            }
        });
    }

    public void getId() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppStore", MODE_PRIVATE);
        idhistory = sharedPreferences.getString("id", "");
    }

    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
