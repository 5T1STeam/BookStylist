package com.app.bookstylist.detail;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.BookActivity;
import com.app.bookstylist.FullPicActivity;
import com.app.bookstylist.R;
import com.app.bookstylist.ShopActivity;
import com.app.bookstylist.shop.Rates;
import com.app.bookstylist.shop.Service;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Rates> ratesList;
    private Context context;

    public CommentAdapter(List<Rates> ratesList, Context context) {
        this.ratesList = ratesList;
        this.context = context;
    }

    public CommentAdapter() {
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder viewHolder, int position) {
        Rates rates = ratesList.get(position);
        if (rates==null){return; }
        viewHolder.tv_name.setText(rates.getName());
        viewHolder.tv_date.setText(rates.getCreatedate());
        viewHolder.tv_content.setText(rates.getDesc());
        viewHolder.ratingBar.setRating(rates.getRate());
        Picasso.get().load(ratesList.get(position).getProfileImage()).into(viewHolder.avatar);
        Picasso.get().load(ratesList.get(position).getImg()).into(viewHolder.img_cmt);
        viewHolder.img_cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullPicActivity.class);
                intent.putExtra("img",ratesList.get(position).getImg());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (ratesList!=null) {
            return ratesList.size() ;
        }
        return 0;
    }

    public  class CommentViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name, tv_date, tv_content;
        private ImageView avatar;
        private RatingBar ratingBar;
        private ImageView img_cmt;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name_user_comment);
            tv_date = itemView.findViewById(R.id.tv_date_user_comment);
            tv_content = itemView.findViewById(R.id.tv_user_content_comment);
            avatar = itemView.findViewById(R.id.img_user_comment);
            ratingBar = itemView.findViewById(R.id.ratingBar_user_comment);
            img_cmt = itemView.findViewById(R.id.img_comment);
        }
    }
}
