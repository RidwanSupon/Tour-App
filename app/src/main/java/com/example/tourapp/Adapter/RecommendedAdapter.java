package com.example.tourapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourapp.Domain.RecommendedItem;
import com.example.tourapp.Activity.DetailActivity;
// DetailActivity ইমপোর্ট করতে ভুলবেন না
import com.example.tourapp.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder> {

    private Context context;
    private List<RecommendedItem> itemList;

    public RecommendedAdapter(Context context, List<RecommendedItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecommendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_recommended, parent, false);
        return new RecommendedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedViewHolder holder, int position) {
        RecommendedItem item = itemList.get(position);

        holder.titleTxt.setText(item.getTitle());
        holder.addressTxt.setText(item.getAddress());
        holder.priceTxt.setText("৳" + item.getPrice());
        holder.durationTxt.setText(item.getDuration());
        holder.bedCountTxt.setText("Beds: " + item.getBed());
        holder.tourGuideNameTxt.setText("Guide: " + item.getTourGuideName());
        holder.scoreTxt.setText(String.format("%.1f", item.getScore()));

        Glide.with(context).load(item.getPic()).into(holder.pic);
        Glide.with(context).load(item.getTourGuidePic()).into(holder.tourGuidePic);

        // ক্লিক হ্যান্ডলার যোগ
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);

            // ডাটা পাস করো ইন্টেন্টে
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("address", item.getAddress());
            intent.putExtra("bedCount", item.getBed());
            intent.putExtra("duration", item.getDuration());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("tourGuideName", item.getTourGuideName());
            intent.putExtra("score", item.getScore());
            intent.putExtra("pic", item.getPic());
            intent.putExtra("tourGuidePic", item.getTourGuidePic());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class RecommendedViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView pic, tourGuidePic;
        TextView titleTxt, addressTxt, priceTxt, durationTxt, bedCountTxt, tourGuideNameTxt, scoreTxt;

        public RecommendedViewHolder(@NonNull View itemView) {
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
        }
    }
}
