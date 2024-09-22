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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
                            list.add(product);
                        } catch (JSONException e) {
                            Log.e("ListPro", "Error parsing JSON object at index " + i + ": " + e.getMessage());
                        }
                    }

                  if(productAdapter!=null){
                      getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              productAdapter.notifyDataSetChanged();
                          }
                      });
                  }
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

}
