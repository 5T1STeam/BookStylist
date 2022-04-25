package com.app.bookstylist.detail;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.BookActivity;
import com.app.bookstylist.R;
import com.app.bookstylist.shop.Service;

import java.text.DecimalFormat;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> mListService;
    public ServiceAdapter(List<Service> mListService) {
        this.mListService = mListService;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_detail,parent,false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = mListService.get(position);
        if (service==null){return; }
        holder.tv_name.setText(service.getName());
        holder.tv_price.setText( withLargeIntegers(service.getPrice()));
        holder.tv_time.setText(service.getTime() +" phút");
        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chuyển id shop và id services đặt lịch
                Intent intent = new Intent(view.getContext(), BookActivity.class);
                intent.putExtra("id",String.valueOf(service.getShopId()));
                intent.putExtra("service choose", service.getName());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListService!=null) {
            return mListService.size() ;
        }
        return 0;
    }

    public  class ServiceViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name, tv_price, tv_time;
        private Button btn_book;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_price=itemView.findViewById(R.id.tv_price);
            tv_time=itemView.findViewById(R.id.tv_time);
            btn_book = itemView.findViewById(R.id.btn_book);

        }
    }
    public static String withLargeIntegers(double value) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(value);
    }
}
