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

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> mListService;
    private Context mContext;
    public ServiceAdapter(List<Service> mListService,Context mContext) {
        this.mListService = mListService;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service,parent,false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = mListService.get(position);
        if (service==null){return;

        }
        holder.tvNameService.setText(service.getName());
        Glide.with(mContext).load(service.getImage()).into(holder.Image);



    }

    @Override
    public int getItemCount() {
        if (mListService!=null) {
            return mListService.size() ;
        }
        return 0;
    }

    public  class ServiceViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNameService;
        private ImageView Image;



        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameService=itemView.findViewById(R.id.nameService);
            Image =itemView.findViewById(R.id.iconService);




        }
    }
}
