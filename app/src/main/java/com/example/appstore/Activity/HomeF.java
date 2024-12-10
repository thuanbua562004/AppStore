package com.example.appstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appstore.Adapter.ProductAdapter;
import com.example.appstore.Adapter.SliderAdapter;
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.PhotoSlide;
import com.example.appstore.Model.Product;
import com.example.appstore.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeF extends Fragment {
    RecyclerView rcvw;
    ImageView shirt, jacket, trouser, dress;
    List<Product> listProduct;
    private DatabaseReference mDatabase;
    private ProductAdapter productAdapter;
    ViewPager viewPager;
    PhotoSlide photoSlide;
    SliderAdapter sliderAdapter;
    List<PhotoSlide> listPhoto;
    EditText edtKeySearch;
    Timer timer;
    CallApi callApiPro = new CallApi();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listProduct = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        anhxa(view);
        rcvw.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(getContext(), listProduct);
        rcvw.setAdapter(productAdapter);
        getListProduct();
        getImgSlide();
        getSearch();
        handerMenuSearch();
        return view;
    }

    public void getImgSlide() {
        listPhoto = listPhoto();
        sliderAdapter = new SliderAdapter(getContext(), listPhoto);
        viewPager.setAdapter(sliderAdapter);
        if (listPhoto == null || viewPager == null || listPhoto.isEmpty()) {
            return;
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int current = viewPager.getCurrentItem();
                int totalsize = listPhoto.size() - 1;
                if (current < totalsize) {
                    current++;
                } else {
                    current = 0;
                }
                int finalCurrent = current;
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(finalCurrent);
                    }
                });
            }
        }, 500, 3000);
    }

    private List<PhotoSlide> listPhoto() {
        List<PhotoSlide> list = new ArrayList<>();
        list.add(new PhotoSlide(R.drawable.anh1));
        list.add(new PhotoSlide(R.drawable.anh2));
        list.add(new PhotoSlide(R.drawable.anh3));
        return list;
    }

    private void anhxa(View view) {
        rcvw = view.findViewById(R.id.rcvproductw);
        viewPager = view.findViewById(R.id.viewPager);
        edtKeySearch = view.findViewById(R.id.edtKeySearch);
        shirt = view.findViewById(R.id.shirt);
        dress = view.findViewById(R.id.dress);
        trouser = view.findViewById(R.id.trouser);
        jacket = view.findViewById(R.id.jacket);
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
                        listProduct.add(product);
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
                Log.e("product", "onDataChange: " + errorMessage);
                // Optionally show an error message to the user
            }
        });
    }


    public void getSearch() {
        edtKeySearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = edtKeySearch.getText().toString();
                    if (key != null && !key.isEmpty()) {
                        Intent myIntent = new Intent(getContext(), SearchActivity.class);
                        myIntent.putExtra("keySearch", key);
                        startActivity(myIntent);
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });
    }

    public void handerMenuSearch() {
        shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), SearchActivity.class);
                myIntent.putExtra("keySearch", "Ao");
                startActivity(myIntent);
            }
        });
        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), SearchActivity.class);
                myIntent.putExtra("keySearch", "Vay");
                startActivity(myIntent);
            }
        });
        trouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), SearchActivity.class);
                myIntent.putExtra("keySearch", "Quan");
                startActivity(myIntent);
            }
        });
        jacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), SearchActivity.class);
                myIntent.putExtra("keySearch", "Ao Khoac");
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
