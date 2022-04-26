package com.app.bookstylist.detail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;
import com.app.bookstylist.ShopActivity;
import com.app.bookstylist.book.BookModal;
import com.app.bookstylist.book.EditBookSchedule;
import com.app.bookstylist.databinding.RowScheduleListBinding;
import com.app.bookstylist.shop.PictureAdapter;
import com.app.bookstylist.shop.Rates;
import com.app.bookstylist.shop.ShopModal;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterSchedule extends RecyclerView.Adapter<AdapterSchedule.HolderSchedule> {
    private ArrayList<BookModal> bookModals;
    private Context context;


    public AdapterSchedule( ArrayList<BookModal> bookModals, Context context) {
        this.bookModals = bookModals;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule_list,parent,false);
        return new HolderSchedule(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSchedule holder, int position) {

        BookModal bookModal = bookModals.get(position);
        if (bookModal==null){return;

        }
        Picasso.get().load(bookModals.get(position).getShopImg()).into(holder.shopImage);

        holder.titleTv.setText(bookModal.getShopName());
        holder.date.setText(bookModal.getTime());
        holder.status.setText(bookModal.getComplete());
        holder.service.setText(bookModal.getService());

        holder.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromSchedule(context,bookModal.getBid(),bookModal);
            }
        });

        holder.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditBookSchedule.class);
                intent.putExtra("bId",bookModal.getBid());
                intent.putExtra("time",bookModal.getTime());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    public static void removeFromSchedule(Context context, String bid,BookModal bookModal){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        bid = bookModal.getBid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bid).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Remove from your schedule", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to remove from your schedule"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (bookModals!=null) {
            return bookModals.size() ;
        }
        return 0;
    }

    //View holder
    class HolderSchedule extends RecyclerView.ViewHolder{
        TextView titleTv,service, price, status,date;
        Button btnChange, btnCancle;
        ImageView shopImage;

        public HolderSchedule(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            service = itemView.findViewById(R.id.Service);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.Status);
            date = itemView.findViewById(R.id.Date);
            shopImage = itemView.findViewById(R.id.shopUrl);
            btnChange = itemView.findViewById(R.id.changeBtn);
            btnCancle = itemView.findViewById(R.id.Cancle);
        }
    }
}
