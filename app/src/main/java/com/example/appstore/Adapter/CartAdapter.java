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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context mContext;
    private List<Cart> listCart;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String key_product;

    public CartAdapter(Context mContext, List<Cart> listCart) {
        this.mContext = mContext;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (position >= listCart.size() || position < 0) {
            Log.i("TAG1", "onBindViewHolder: Position out of bounds - " + position);
            return;
        }
        Cart cart = listCart.get(position);
        Glide.with(holder.itemView.getContext())
                .load(cart.getImgProduct())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.imgProduct);
        holder.txtNamePro.setText(cart.getNameProduct());
        holder.txtPhanloai.setText("Phân Loại: " + cart.getColor() + " / " + cart.getSize());
        holder.txtPrice.setText(String.valueOf(vn.format(cart.getPrice())) + " VND");
        holder.txtToltalNumber.setText(String.valueOf(cart.getNumber()));
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cart.getNumber();
                quantity++;
                cart.setNumber(quantity);
                notifyDataSetChanged();
                updateNumber(quantity, String.valueOf(cart.getId_product()), cart.getColor(), cart.getSize());
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int quantity = cart.getNumber();
                if (quantity == 1) {
                    return;
                }
                quantity--;
                cart.setNumber(quantity);
                notifyDataSetChanged();
                updateNumber(quantity, String.valueOf(cart.getId_product()), cart.getColor(), cart.getSize());
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delteProduct(String.valueOf(cart.getId_product()), cart.getColor(), cart.getSize());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listCart.size(); // Return the size of the list
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct, btnBack;
        private TextView txtNamePro, txtPhanloai, txtPrice;
        ImageButton btnPlus, btnMinus, btnDelete;
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
            btnDelete = itemView.findViewById(R.id.delete);
        }

    }

    public void updateNumber(int number, String id, String color, String size) {
        key_product = id + "_" + color + "_" + size;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = database.getReference("cart/" + user.getUid() + "/" + key_product + "/");
        databaseReference.child("number").setValue(number)
                .addOnSuccessListener(aVoid -> {
                    // Update successful
                    System.out.println("Item list updated successfully.");
                })
                .addOnFailureListener(e -> {
                    // Update failed
                    System.out.println("Failed to update item list: " + e);
                });
    }

    public void delteProduct(String id, String color, String size) {
        key_product = id + "_" + color + "_" + size;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("cart")
                .child(user.getUid())
                .child(key_product);
        databaseReference.removeValue()
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }
}

