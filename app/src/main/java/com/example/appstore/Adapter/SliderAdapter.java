package com.example.appstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.appstore.Model.PhotoSlide;
import com.example.appstore.R;

import java.util.List;


public class SliderAdapter extends PagerAdapter {
    Context context ;
    List<PhotoSlide> listPhoto ;

    public SliderAdapter(Context context, List<PhotoSlide> listPhoto) {
        this.context = context;
        this.listPhoto = listPhoto;
    }

    @Override
    public int getCount() {
        if (listPhoto==null){
            return  0;
        }
        return  listPhoto.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_slide,container,false);
        ImageView imgView =view.findViewById(R.id.imgPhoto) ;
        PhotoSlide photoSlide = listPhoto.get(position);
        if(photoSlide!=null){
            Glide.with(context).load(photoSlide.getImgLink()).into(imgView);
        }
        container.addView(view);

        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
