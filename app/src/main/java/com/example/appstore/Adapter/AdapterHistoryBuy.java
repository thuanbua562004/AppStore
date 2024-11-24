package com.example.appstore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Model.Cart;
import com.example.appstore.Model.HistoryBuy;
import com.example.appstore.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterHistoryBuy extends RecyclerView.Adapter<AdapterHistoryBuy.HistoryViewholder> {

    private List<HistoryBuy> buyList;
    private Locale localeVN = new Locale("vi", "VN");
    private NumberFormat vn = NumberFormat.getInstance(localeVN);
    private Context mContext;

    public AdapterHistoryBuy(List<HistoryBuy> buyList, Context mContext) {
        this.buyList = buyList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HistoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_buy, parent, false);
        return new HistoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewholder holder, int position) {
        if (position >= buyList.size() || position < 0) {
            Log.i("TAG1", "onBindViewHolder: Position out of bounds - " + position);
            return;
        }

        HistoryBuy historyBuy = buyList.get(position);
        Log.i("historybuyl", "onBindViewHolder: " + historyBuy.toString());

        holder.txtmethodpay.setText("Phương thức thanh toán: " + historyBuy.getMethodPay());
        holder.txtdiachi.setText("Địa Chỉ: " + historyBuy.getAdress());
        holder.txtdate.setText("Ngày mua: " + historyBuy.getDate());
        holder.txttongtien.setText("Tổng Tiền: " + vn.format(historyBuy.getTotalPrice()) + " VND");
        holder.txtstd.setText("STD: " + historyBuy.getPhoneNumber());

        // Xóa tất cả các sản phẩm trước khi thêm vào
        holder.linearLayoutProducts.removeAllViews();

        List<Cart> cartList = historyBuy.getList();
        if (cartList != null && !cartList.isEmpty()) {
            for (Cart cart : cartList) {
                View productView = LayoutInflater.from(mContext).inflate(R.layout.item_product1xml, holder.linearLayoutProducts, false);
                TextView txtnameproduct = productView.findViewById(R.id.product_name);
                TextView txtpriceproduct = productView.findViewById(R.id.product_price);
                TextView txtquantityproduct = productView.findViewById(R.id.product_quantity);
                TextView txtsize = productView.findViewById(R.id.product_size);

                txtnameproduct.setText(cart.getNameProduct());
                txtpriceproduct.setText(String.valueOf(cart.getPrice()));
                txtquantityproduct.setText(String.valueOf(cart.getNumber()));
                txtsize.setText(cart.getSize());
                holder.linearLayoutProducts.addView(productView);
            }
        } else {
            TextView noProductsView = new TextView(mContext);
            noProductsView.setText("Không có sản phẩm");
            holder.linearLayoutProducts.addView(noProductsView);
        }
    }

    @Override
    public int getItemCount() {
        return buyList.size();
    }

    public class HistoryViewholder extends RecyclerView.ViewHolder {
        TextView txttongtien, txtdiachi, txtmethodpay, txtdate, txtstd;
        LinearLayout linearLayoutProducts;

        public HistoryViewholder(@NonNull View itemView) {
            super(itemView);
            txtmethodpay = itemView.findViewById(R.id.txtmadon);
            txtdiachi = itemView.findViewById(R.id.txtdiachia);
            txttongtien = itemView.findViewById(R.id.txttongtien);
            txtdate = itemView.findViewById(R.id.txtdate);
            txtstd = itemView.findViewById(R.id.txtsdt);
            linearLayoutProducts = itemView.findViewById(R.id.productList);
        }
    }
}
