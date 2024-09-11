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

import com.example.appstore.Adapter.NotifiAdapter;
import com.example.appstore.Model.Notifi;
import com.example.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotifiF extends Fragment {
    private DatabaseReference mDatabase;
    private List<Notifi> listNotifi;
    private NotifiAdapter notifiAdapter;
    private RecyclerView recyclerView;

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
        getNotiList();
        return view;
    }

    @SuppressLint("MissingInflatedId")
    private void getNotiList() {
        mDatabase.child("noti").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNotifi.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Notifi notifi = productSnapshot.getValue(Notifi.class);
                    if (notifi != null) {
                        listNotifi.add(notifi);
                        Log.i("notifi", "Added to list: " + notifi.toString());
                    } else {
                        Log.e("FirebaseData", "Notifi object is null for key: " + productSnapshot.getKey());
                    }
                }
                notifiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
}
