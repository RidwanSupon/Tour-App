package com.example.tourapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourapp.Activity.DetailActivity;
import com.example.tourapp.Domain.TripItem;
import com.example.tourapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private Context context;
    private List<TripItem> itemList;

    public TripAdapter(Context context, List<TripItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripItem item = itemList.get(position);

        // Bind data
        holder.tvTitle.setText(item.getTitle());
        holder.tvDateTime.setText(item.getDateTour() + " - " + item.getTimeTour());
        holder.tvBedRoom.setText("Beds: " + item.getBed());
        holder.tvDistanceDuration.setText(item.getDistance() + " | " + item.getDuration());

        // Launch DetailActivity on click
        holder.btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);

            // Pass all the needed fields into the Intent
            intent.putExtra("title",           item.getTitle());
            intent.putExtra("description",     item.getDescription());
            intent.putExtra("address",         item.getAddress());
            intent.putExtra("bedCount",        item.getBed());
            intent.putExtra("duration",        item.getDuration());
            intent.putExtra("price",           item.getPrice());
            intent.putExtra("tourGuideName",   item.getTourGuideName());
            intent.putExtra("score",           item.getScore());
            intent.putExtra("pic",             item.getPic());
            intent.putExtra("tourGuidePic",    item.getTourGuidePic());
            intent.putExtra("tourGuidePhone",  item.getTourGuidePhone());
            intent.putExtra("timeTour",        item.getTimeTour());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDateTime, tvBedRoom, tvDistanceDuration;
        Button btnBookNow;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle             = itemView.findViewById(R.id.tvTitle);
            tvDateTime          = itemView.findViewById(R.id.tvDateTime);
            tvBedRoom           = itemView.findViewById(R.id.tvBedRoom);
            tvDistanceDuration  = itemView.findViewById(R.id.tvDistanceDuration);
            btnBookNow          = itemView.findViewById(R.id.btnBookNow);
        }
    }
}
