package com.example.appstore.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.Cart;
import com.example.appstore.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context mContext;
    private List<Cart> listCart;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    CallApi callApi = new CallApi();
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
                String id = getUserId();
                callApi.updateCart(id, cart.getId_product(), cart.getNumber(), new ApiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("update", "onSuccess: " + response);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.i("update", "onSuccess: " + errorMessage);

                    }
                });

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
                String id = getUserId();
                callApi.updateCart(id, cart.getId_product(), cart.getNumber(), new ApiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("update", "onSuccess: " + response);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.i("update", "onSuccess: " + errorMessage);

                    }
                });
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getUserId();
                callApi.deleteItemCart(id, cart.getId_product(), new ApiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("update", "onSuccess: " + response);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.i("update", "onSuccess: " + errorMessage);

                    }
                });
                notifyDataSetChanged();
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
    public  String getUserId (){
        SharedPreferences pref = mContext.getSharedPreferences("AppStore",Context.MODE_PRIVATE);
        String userId = pref.getString("id","");
        return  userId;
    }
}

