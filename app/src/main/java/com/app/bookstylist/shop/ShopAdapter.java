package com.app.bookstylist.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;
import com.app.bookstylist.ShopActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> implements Filterable {

    private List<ShopModal> shopModals;
    private List<ShopModal> shopModalsOld;
    private Context context;

    public ShopAdapter(List<ShopModal> listShops, Context context) {
        this.shopModals = listShops;
        this.context = context;
        this.shopModalsOld = listShops;

    }

    @NonNull
    @Override
    public ShopAdapter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent,false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopModal shopModal = shopModals.get(position);
        if(shopModal ==null){
            return;
        }

        Glide.with(context).load(shopModal.getImage()).into(holder.imgShop);
        holder.nameShop.setText(shopModal.getName());
        holder.ratingBar.setRating(shopModal.getRating());
        holder.ratenum.setText(String.valueOf(shopModal.getRating()));
        holder.address.setText(shopModal.getAddress());
        holder.comment.setText(shopModal.getComment()+" Đánh giá");
        holder.imgShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopActivity.class);
                intent.putExtra("name",shopModal.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(shopModals !=  null){
            return shopModals.size();
        }
        return 0;
    }


    public class ShopViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgShop;
        private TextView nameShop, address, comment, ratenum;
        private RatingBar ratingBar;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            imgShop = itemView.findViewById(R.id.rImage);
            nameShop = itemView.findViewById(R.id.name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rateShop);
            address = itemView.findViewById(R.id.address);
            comment = itemView.findViewById(R.id.commentShop);
            ratenum = itemView.findViewById(R.id.rateNum);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if(strSearch.isEmpty()){
                    shopModals = shopModalsOld;
                }else{
                    List<ShopModal> list = new ArrayList<>();
                    for(ShopModal shopModal : shopModalsOld){
                        if(shopModal.getName().toLowerCase().contains(strSearch.toLowerCase())
                                || shopModal.getDesc().toLowerCase().contains(strSearch.toLowerCase())
                                || shopModal.getAddress().toLowerCase().contains((strSearch.toLowerCase()))){
                            list.add(shopModal);
                        }
                    }
                    shopModals = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = shopModals;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                shopModals = (List<ShopModal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
