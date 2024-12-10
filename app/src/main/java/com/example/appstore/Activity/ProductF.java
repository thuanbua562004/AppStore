package com.example.appstore.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Adapter.ProductAdapter;
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.Product;
import com.example.appstore.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductF extends Fragment {

    private RecyclerView recyclerView;
    private List<Product> list;
    private ProductAdapter productAdapter;
    CallApi callApiPro = new CallApi();
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.rcvProduct);

        list = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter(getContext(), list);
        recyclerView.setAdapter(productAdapter);
        getListProduct();

        return view;
    }

    public void getListProduct() {
        callApiPro.getListProduct(new ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("ListPro", "onSuccess: " + response);
                Map<String, String> colorMap = new HashMap<>();
                Map<String, String> sizeMap = new HashMap<>();
                sizeMap.put("L","M");
                sizeMap.put("L","M");
                String img1 = "";
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_pro = jsonObject.getString("_id");
                        JSONObject jsonDetail = jsonObject.getJSONObject("details");
                        Log.i("1", "onSuccess: "+ jsonDetail);
                        String name = jsonDetail.optString("name", "Unknown Name");
                        int price = jsonDetail.optInt("price", 0);
                        String info = jsonDetail.optString("info", "No Info");

                        // Parse imgForColor and extract img1
                        JSONArray imgForColorArray = jsonDetail.optJSONArray("imgForColor");
                        JSONObject colorVariant1 = imgForColorArray.getJSONObject(0);
                        img1 =colorVariant1.getString("imageUrl");
                        Log.i("1", "onSuccess: " + img1);
                        for (int j = 0; j < imgForColorArray.length(); j++) {
                            JSONObject colorVariant = imgForColorArray.getJSONObject(j);

                            Log.i("1", "onSuccess: "+ colorVariant);
                            colorMap.put(String.valueOf(j),colorVariant.getString("color"));
                        }
                        Product product = new Product(colorMap, sizeMap, price, name, "Ao", img1, "", info, id_pro);
                        list.add(product);
                    }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        productAdapter.notifyDataSetChanged();
                    }
                });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("ListPro", "API Error: " + errorMessage);
            }
        });
    }

}
