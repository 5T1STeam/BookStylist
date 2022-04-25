package com.app.bookstylist.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;
import com.app.bookstylist.ShopActivity;
import com.app.bookstylist.shop.PictureAdapter;
import com.app.bookstylist.shop.Rates;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ReviewFragment extends Fragment {

    public ReviewFragment( int shopId) {
        this.shopId = shopId;
    }

    int shopId;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ArrayList<Rates> ratesList;
    ArrayList<Rates> commentList;
    TextView ratingNum, quantityRating;
    RatingBar ratingBar;
    ProgressBar star1, star2, star3,star4, star5;
    public ReviewFragment() {
        // Required empty public constructor
    }
    private void getRating() {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Rates");
        myRef.orderByChild("shopid").equalTo(shopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum =0;
                float[] pb = {0,0,0,0,0};
                for(DataSnapshot unit : snapshot.getChildren()){
                   ratesList.add(unit.getValue(Rates.class));
                }
                int quantity = ratesList.size();
                for (Rates item: ratesList) {
                    sum += item.getRate();
                    switch (item.getRate()){
                        case 1:
                            pb[0] += 1;
                            break;
                        case 2:
                            pb[1] += 1;
                            break;
                        case 3:
                            pb[2] += 1;
                            break;
                        case 4:
                            pb[3] += 1;
                            break;
                        case 5:
                            pb[4] += 1;
                            break;
                    }
                }
                float ave = (float)sum/quantity;
                DecimalFormat df = new DecimalFormat("#.#");
                df.format(ave);

                quantityRating.setText(String.valueOf(quantity));
                if(quantity==0){
                    ratingNum.setText("5");
                    ratingBar.setRating(5);
                }
                else {
                    ratingNum.setText(String.valueOf( ave));
                    ratingBar.setRating((float) ave);
                }
                for (int i = 0; i<5;i++ ){
                    pb[i] = (pb[i]/quantity)*100;
                }
                star1.setProgress((int) pb[0]);
                star2.setProgress((int) pb[1]);
                star3.setProgress((int) pb[2]);
                star4.setProgress((int) pb[3]);
                star5.setProgress((int) pb[4]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getComment() {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Rates");
        myRef.orderByChild("shopid").equalTo(shopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot unit : snapshot.getChildren()){
                    commentList.add( unit.getValue(Rates.class));
                }
                for (Rates item: commentList) {
                    DatabaseReference reference = database.getReference("Users");
                    reference.orderByChild("uid").equalTo(item.getUserid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                               item.setName(snapshot1.child("name").getValue().toString());
                               item.setProfileImage(snapshot1.child("profileImage").getValue().toString());
                            }
                            commentAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        recyclerView = view.findViewById(R.id.rcv_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentList = new ArrayList<>();
        ratesList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ratingNum =view.findViewById(R.id.ratingNum);
        ratingBar=view.findViewById(R.id.ratingBarFrag);
        quantityRating=view.findViewById(R.id.ratingQuantity);
        star1=view.findViewById(R.id.pb1);
        star2=view.findViewById(R.id.pb2);
        star3=view.findViewById(R.id.pb3);
        star4=view.findViewById(R.id.pb4);
        star5=view.findViewById(R.id.pb5);
        getRating();
        getComment();

        return view;
    }
}