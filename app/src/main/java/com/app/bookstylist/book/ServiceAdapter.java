package com.app.bookstylist.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MultiViewHolder> {

    private Context context;
    private List<ServiceCheck> allService;

    public ServiceAdapter(List<ServiceCheck> allService, Context context){
        this.allService = allService;
        this.context = context;
    }

    public void setAllService(List<ServiceCheck> allService){
        this.allService = new ArrayList<>();
        this.allService = allService;
        notifyDataSetChanged();
    }

    public List<ServiceCheck> getSelectedService(){
        List<ServiceCheck> list = new ArrayList<>();
        for (int i=0; i< allService.size(); i++ ){
            if(allService.get(i).isChecked()){
                list.add(allService.get(i));
            }
        }
        return list;
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_service,parent,false);
        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        holder.bind(allService.get(position));
    }

    @Override
    public int getItemCount() {
        if(allService!=null){
            return allService.size();
        }
        return 0;
    }

    public class MultiViewHolder extends RecyclerView.ViewHolder{
        private TextView txtService;
        private ImageView imageCheck;
        public MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            txtService = itemView.findViewById(R.id.select_ser);
            imageCheck = itemView.findViewById(R.id.imgSer);
        }

        public void bind(final ServiceCheck serviceCheck){
            imageCheck.setVisibility(serviceCheck.isChecked() ? View.VISIBLE : View.GONE);
            txtService.setText(serviceCheck.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serviceCheck.setCheckSelect(!serviceCheck.isChecked());
                    imageCheck.setVisibility(serviceCheck.isChecked()? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    public List<ServiceCheck> getall(){return allService;}


}
