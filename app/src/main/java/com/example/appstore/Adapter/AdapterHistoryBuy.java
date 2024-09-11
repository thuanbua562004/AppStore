package com.example.appstore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appstore.Model.HistoryBuy;
import com.example.appstore.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterHistoryBuy   extends  RecyclerView.Adapter<AdapterHistoryBuy.HistoryViewholder>{
    private List<HistoryBuy> buyList;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    private Context mContext;

    public AdapterHistoryBuy(List<HistoryBuy> buyList, Context mContext) {
        this.buyList = buyList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public HistoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryBuy historyBuy = buyList.get(0);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_buy,parent,false);
        return new HistoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewholder holder, int position) {
        if (position >= buyList.size() || position < 0) {
            Log.i("TAG1", "onBindViewHolder: Position out of bounds - " + position);
            return;
        }
        HistoryBuy historyBuy = buyList.get(position);

        holder.txtmethodpay.setText("Phương thức thanh toán :" + historyBuy.getMethodPay());
        holder.txtdiachi.setText("Địa Chỉ :"+historyBuy.getAdress());
        holder.txtdate.setText("Ngày mua :"+historyBuy.getDate());
        holder.txttongtien.setText("Tổng Tiền :"+vn.format(historyBuy.getTotalPrice()) + " VND");
        holder.txtstd.setText("STD:" + historyBuy.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return buyList.size();  // Ensure this returns the correct size of the list
    }

    public  class HistoryViewholder extends RecyclerView.ViewHolder {
            TextView txttongtien ,txtdiachi ,txtmethodpay ,txtdate ,txtstd;
        public HistoryViewholder(@NonNull View itemView) {
            super(itemView);
            txtmethodpay = itemView.findViewById(R.id.txtmadon);
            txtdiachi  = itemView.findViewById(R.id.txtdiachia);
            txttongtien = itemView.findViewById(R.id.txttongtien);
            txtdate = itemView.findViewById(R.id.txtdate);
            txtstd = itemView.findViewById(R.id.txtsdt);
        }
    }
}
