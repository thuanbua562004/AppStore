package com.example.appstore.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appstore.Model.Product;
import com.example.appstore.Activity.ProducDetail;
import com.example.appstore.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter  extends  RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private List<Product>  listProduct;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    LinearLayout linearLayout;
    private Context mContext;
     int position_id;
    public ProductAdapter(Context mContext ,List<Product> listProduct) {
        this.mContext = mContext;;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Product product = listProduct.get(0);
        Log.i("TAG1", "onCreateViewHolder: "+ product.getName());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (position >= listProduct.size() || position < 0) {
            Log.i("TAG1", "onBindViewHolder: Position out of bounds - " + position);
            return;
        }
        Product product = listProduct.get(position);
        if (product == null) {
            Log.i("TAG1", "onBindViewHolder: Product is null at position " + position);
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(product.getImg1())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.imgProduct);

        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText(vn.format(product.getPrice()) + " VND");
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailProduct(product);
            }
        });
    }
    @Override
    public int getItemCount() {
        return listProduct.size();  // Ensure this returns the correct size of the list
    }

    public  class  ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct ;
        private TextView nameProduct ;
        private  TextView priceProduct;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgproduct);
            nameProduct  = itemView.findViewById(R.id.nameproduct);
            priceProduct = itemView.findViewById(R.id.priceproduct);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
    private void getDetailProduct(Product product) {
        Intent intent = new Intent(mContext, ProducDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product" ,product);
        bundle.putSerializable("id_product" ,product.getId_product());

        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
