package com.app.bookstylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.bookstylist.book.BookModal;
import com.app.bookstylist.databinding.ActivityScheduleBinding;
import com.app.bookstylist.databinding.ActivityShopBinding;
import com.app.bookstylist.detail.AdapterSchedule;
import com.app.bookstylist.shop.ShopModal;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Schedule extends AppCompatActivity {
    private ActivityScheduleBinding binding;

    private FirebaseAuth firebaseAuth;

    private ArrayList<BookModal> bookModalArrayList;
    private AdapterSchedule adapterSchedule;

    private RecyclerView listSchedule;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        bookModalArrayList =new ArrayList<>();
        adapterSchedule = new AdapterSchedule(bookModalArrayList, Schedule.this);

        listSchedule = findViewById(R.id.listSChedule);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);

        listSchedule.setLayoutManager(flexboxLayoutManager);
        listSchedule.setAdapter(adapterSchedule);
        getlistBook();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Schedule.this,DashboardUserActivity.class));

            }
        });
    }

    private void getlistBook() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");

        reference.orderByChild("uid").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookModalArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BookModal bookModal = dataSnapshot.getValue(BookModal.class);
                    bookModalArrayList.add(bookModal);
                }
                for (BookModal item: bookModalArrayList) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Shop/"+item.getSid());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            item.setShopName(snapshot.child("name").getValue().toString());
                            item.setShopImg(snapshot.child("image").getValue().toString());
                            item.setRating(snapshot.child("rating").getValue().toString());
                            item.setComment(snapshot.child("comment").getValue().toString());
                            item.setAddress(snapshot.child("address").getValue().toString());
                            adapterSchedule.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Schedule.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}