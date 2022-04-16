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

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.CateViewHolder> {
    private List<com.app.bookstylist.home.Cate> mListCate;
    private Context mContext;
    public CateAdapter(List<com.app.bookstylist.home.Cate> mListCate, Context mContext) {
       this.mListCate = mListCate;
        this.mContext = mContext;
    }



    @NonNull
    @Override

    public CateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cate,parent,false);
        return new CateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CateViewHolder holder, int position) {
        com.app.bookstylist.home.Cate cate    = mListCate.get(position);
        if (cate==null){return;

        }
        holder.tvName.setText(cate.getName());
        Glide.with(mContext).load(cate.getImage()).into(holder.Image);
    }

    @Override
    public int getItemCount() {
        if (mListCate!=null) {
            return mListCate.size() ;
        }
        return 0;
    }

    public class CateViewHolder extends RecyclerView.ViewHolder{


        private TextView tvName;
        private ImageView Image;

        public CateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.nameCate);
            Image = itemView.findViewById(R.id.imageCate);
        }
    }
}
