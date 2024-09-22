package com.example.appstore.Activity;

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

import com.example.appstore.Adapter.NotifiAdapter;
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.Notifi;
import com.example.appstore.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotifiF extends Fragment {
    private DatabaseReference mDatabase;
    private List<Notifi> listNotifi;
    private NotifiAdapter notifiAdapter;
    private RecyclerView recyclerView;
    CallApi apiNotifi = new CallApi();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifi, container, false);
        recyclerView = view.findViewById(R.id.rcvNoti);
        listNotifi = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        notifiAdapter = new NotifiAdapter(listNotifi, getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(notifiAdapter);
        getNotifi();
        return view;
    }
    public void getNotifi (){
        apiNotifi.getListNotifi(new ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("_id");
                        JSONObject jsonDetails = jsonObject.getJSONObject("details");
                        String title ,info , img ,date ;
                        Date date1 ;
                        title = jsonDetails.getString("title");
                        info = jsonDetails.getString("infoNoti");
                        img = jsonDetails.getString("img1");
                        date = jsonDetails.getString("dateCreate");
                        Log.i("Notifi", "onSuccess: " + title + info + img + date);
                        Notifi notifi = new Notifi(title,info,img,"",date);
                        listNotifi.add(notifi);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifiAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onError(String errorMessage) {
                          Log.i("Notifi", "Error: " + errorMessage);
            }
        });
    }
}
