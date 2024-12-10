package com.example.appstore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appstore.Model.Cart;
import com.example.appstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.CartViewHolder> {
    private Context mContext;
    private List<Cart> listCart;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth ;
    FirebaseUser user;
    String key_product;
    public PayAdapter(Context mContext, List<Cart> listCart) {
        this.mContext = mContext;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pay, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (position >= listCart.size() || position < 0) {
            Log.i("TAG1", "onBindViewHolder: Position out of bounds - " + position);
            return;
        }
        Cart cart = listCart.get(position);
        String img =  cart.getImgProduct();
        String imgUrl = img.replace("5000", "10.0.2.2:5000");
        String i = imgUrl.replace("localhost:","");
        Glide.with(holder.itemView.getContext())
                .load(i)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.imgProduct);
        holder.txtNamePro.setText(cart.getNameProduct());
        holder.txtPhanloai.setText("Phan loai: " + cart.getColor() + " / " + cart.getSize());
        holder.txtPrice.setText(String.valueOf(vn.format(cart.getPrice())) + " VND");
        holder.txtToltalNumber.setText("So Luong: "+String.valueOf(cart.getNumber()));
    }

    @Override
    public int getItemCount() {
        return listCart.size(); // Return the size of the list
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct,btnBack;
        private TextView txtNamePro, txtPhanloai, txtPrice  ;
        ImageButton btnPlus , btnMinus  ,btnDelete;
        final TextView txtToltalNumber;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgproduct);
            txtNamePro = itemView.findViewById(R.id.txtName);
            txtPhanloai = itemView.findViewById(R.id.txtPhanloai);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtToltalNumber = itemView.findViewById(R.id.numberPro);
            btnPlus = itemView.findViewById(R.id.btnplus);
            btnMinus = itemView.findViewById(R.id.btnminus);
            btnDelete =itemView.findViewById(R.id.delete);
        }

    }
}

