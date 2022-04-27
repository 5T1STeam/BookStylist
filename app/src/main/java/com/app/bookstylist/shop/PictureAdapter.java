package com.app.bookstylist.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.FullPicActivity;
import com.app.bookstylist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder>{
    ArrayList<Rates> ratesList;
    Context context;

    public PictureAdapter(ArrayList<Rates> ratesList, Context context) {
        this.ratesList = ratesList;
        this.context = context;
    }

    @NonNull
    @Override
    public PictureAdapter.PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_card,parent,false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureAdapter.PictureViewHolder holder, int position) {
        Rates rates = ratesList.get(position);
        if (rates==null){
            return;
        }
        Picasso.get().load(ratesList.get(position).getImg()).into(holder.image);
        holder.image.setOnClickListener(view -> {
            Intent intent = new Intent(context, FullPicActivity.class);
            intent.putExtra("img",rates.getImg());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (ratesList!=null) {
            return ratesList.size() ;
        }
        return 0;
    }
    public  class PictureViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            image =itemView.findViewById(R.id.item_img);

        }
    }
}
