package com.app.bookstylist.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<com.app.bookstylist.home.Shop> mListShop;
    private Context mContext;
    public ShopAdapter(List<com.app.bookstylist.home.Shop> mListShop, Context mContext) {
        this.mListShop = mListShop;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        com.app.bookstylist.home.Shop shop = mListShop.get(position);
        if (shop==null){return;

        }
        holder.tvName.setText(shop.getName());
        holder.tvAddress.setText(shop.getAddress());
        Glide.with(mContext).load(shop.getImage()).into(holder.Image);



    }

    @Override
    public int getItemCount() {
        if (mListShop!=null) {
        return mListShop.size() ;
        }
        return 0;
    }

    public  class ShopViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvAddress;
        private ImageView Image;


        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.name);
            tvAddress=itemView.findViewById(R.id.address);
            Image = itemView.findViewById(R.id.rImage);



        }
    }
}
