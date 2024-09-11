package com.example.appstore.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Adapter.AdapterHistoryBuy;
import com.example.appstore.Model.HistoryBuy;
import com.example.appstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryBuyActivity extends AppCompatActivity {
    List<HistoryBuy> buyList ;
    AdapterHistoryBuy adapterHistoryBuy ;
    RecyclerView recyclerView ;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String idhistory ;
    ImageButton btnBack ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_buy);
        btnBack =findViewById(R.id.backBtn);
        back();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        idhistory = user.getUid();


        getHistoryBuy();
        buyList = new ArrayList<HistoryBuy>();
        adapterHistoryBuy = new AdapterHistoryBuy(buyList ,this);
        RecyclerView recyclerView = findViewById(R.id.rcvhHistoryBuy);
        recyclerView.setLayoutManager(new GridLayoutManager( this,1));
        recyclerView.setAdapter(adapterHistoryBuy);
    }

    private void getHistoryBuy() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("history_buy");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                buyList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        if (productSnapshot.toString().contains(idhistory)) {
                            HistoryBuy historyBuy = productSnapshot.getValue(HistoryBuy.class);
                            buyList.add(historyBuy);
                            Log.i("cart", "Added to list: " + historyBuy.toString());
                        } else {
                            Log.e("FirebaseData", "Cart object is null for key: " + productSnapshot.getKey());
                        }
                    }
                    adapterHistoryBuy.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "DatabaseError: " + databaseError.getMessage());
            }
        });
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