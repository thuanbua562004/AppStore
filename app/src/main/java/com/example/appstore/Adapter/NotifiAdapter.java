package com.example.appstore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appstore.Model.Notifi;
import com.example.appstore.R;

import java.util.List;

public class NotifiAdapter extends  RecyclerView.Adapter<NotifiAdapter.NotifiHolder>{
    List<Notifi> list;
    Notifi notifi;
    private Context mcontext;

    public NotifiAdapter(List<Notifi> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public NotifiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_notifi,parent,false);
        return  new NotifiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiHolder holder, int position) {
        if (position >= list.size() || position < 0) {
            Log.i("TAG1", "onBindViewHolder: Position out of bounds - " + position);
            return;
        }
       notifi =list.get(position);
        holder.txtInfo.setText(notifi.getInfoNoti());
        holder.txtTitle.setText(notifi.getTitle());
        Glide.with(mcontext)
                .load(notifi.getImg1())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(holder.img);
    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public class NotifiHolder extends RecyclerView.ViewHolder {
        ImageView img ;
        TextView txtTitle ,txtInfo;
        public NotifiHolder(@NonNull View itemView) {
            super(itemView);
            img =itemView.findViewById(R.id.imgNoti);
            txtTitle =itemView.findViewById(R.id.txtTileNoti);
            txtInfo =itemView.findViewById(R.id.txtInfoNoti);

        }
    }
}
