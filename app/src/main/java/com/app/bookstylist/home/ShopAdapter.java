package com.app.bookstylist.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;
import com.app.bookstylist.ShopActivity;
import com.app.bookstylist.shop.ShopModal;
import com.bumptech.glide.Glide;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<ShopModal> mListShop;
    private Context mContext;
    public ShopAdapter(List<ShopModal> mListShop, Context mContext) {
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
        ShopModal shopModal = mListShop.get(position);
        if (shopModal==null){return;

        }

        Glide.with(mContext).load(shopModal.getImage()).into(holder.Image);
        holder.tvName.setText(shopModal.getName());
        holder.ratingBar.setRating(shopModal.getRating());
        holder.tvRate.setText(String.valueOf(shopModal.getRating()));
        holder.tvAddress.setText(shopModal.getAddress());
        holder.tvComment.setText(shopModal.getComment()+" Đánh giá");
        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopActivity.class);
                intent.putExtra("name",shopModal.getName());
                intent.putExtra("service", shopModal.getService());
                intent.putExtra("address", shopModal.getAddress());
                intent.putExtra("id",String.valueOf(shopModal.getId()));
                intent.putExtra("img",shopModal.getImage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListShop!=null) {
        return mListShop.size() ;
        }
        return 0;
    }

    public  class ShopViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName, tvAddress, tvComment, tvRate;
        private ImageView Image;
        private RatingBar ratingBar;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.name);
            tvAddress=itemView.findViewById(R.id.address);
            Image = itemView.findViewById(R.id.rImage);
            tvComment = itemView.findViewById(R.id.commentShop);
            tvRate = itemView.findViewById(R.id.rateNum);
            ratingBar = itemView.findViewById(R.id.rateShop);
        }
    }
}
