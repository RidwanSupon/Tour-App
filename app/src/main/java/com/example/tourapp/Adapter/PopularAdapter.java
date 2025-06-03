package com.example.tourapp.Adapter;

import com.example.tourapp.Domain.PopularItem;
import com.example.tourapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private Context context;
    private List<PopularItem> itemList;

    public PopularAdapter(Context context, List<PopularItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_popular, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        PopularItem item = itemList.get(position);

        holder.titleTxt.setText(item.getTitle());
        holder.addressTxt.setText(item.getAddress());

        // Convert int price to string with currency symbol
        holder.priceTxt.setText("৳" + item.getPrice());

        holder.durationTxt.setText(item.getDuration());

        // bed count (int)
        holder.bedCountTxt.setText("Beds: " + item.getBed());

        holder.tourGuideNameTxt.setText("Guide: " + item.getTourGuideName());

        // Convert double score to string, rounded to 1 decimal place
        holder.scoreTxt.setText(String.format("%.1f", item.getScore()));

        Glide.with(context).load(item.getPic()).into(holder.pic);
        Glide.with(context).load(item.getTourGuidePic()).into(holder.tourGuidePic);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class PopularViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView pic, tourGuidePic;
        TextView titleTxt, addressTxt, priceTxt, durationTxt, bedCountTxt, tourGuideNameTxt, scoreTxt;
        ImageView imageView4, imageView5;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            durationTxt = itemView.findViewById(R.id.durationTxt);
            bedCountTxt = itemView.findViewById(R.id.bedCountTxt);
            tourGuideNameTxt = itemView.findViewById(R.id.tourGuideNameTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            tourGuidePic = itemView.findViewById(R.id.tourGuidePic);
            imageView4 = itemView.findViewById(R.id.imageView4);
            imageView5 = itemView.findViewById(R.id.imageView5);
        }
    }
}
