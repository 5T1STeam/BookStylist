package com.app.bookstylist.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.bookstylist.R;
import com.app.bookstylist.shop.Rates;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailFragment extends Fragment {

    public DetailFragment( int shopId) {
        this.shopId = shopId;
    }

    int shopId;
    private TextView bt_xemThem;
    private  TextView tv_dsc, tv_phone;
    private Button call;
    boolean isHide = true;
    String[] id = new String[14];
    TextView[] textViews = new TextView[14];

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        int temp;
        for (int i =0; i<14; i++){
            if(i<7){
                id[i] = "open"+(i+2);
            }else {
                id[i] = "close"+(i-5);
            }
            temp = getResources().getIdentifier(id[i],"id", getActivity().getPackageName());
            textViews[i] = view.findViewById(temp);
        }

        bt_xemThem = view.findViewById(R.id.bt_xemThem);
        tv_dsc = view.findViewById(R.id.dsc_shop);
        tv_phone = view.findViewById(R.id.phone_number);
        call = view.findViewById(R.id.bt_call_shop);

        bt_xemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHide){
                    tv_dsc.setMaxLines(Integer.MAX_VALUE);
                    bt_xemThem.setText("Thu gọn");
                }
                else {
                    tv_dsc.setMaxLines(3);
                    bt_xemThem.setText("Xem thêm");
                }
                isHide = !isHide;
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv_phone.getText()));
                getActivity().startActivity(intentDial);
            }
        });
        getDetail();
        return view;
    }

    private void getDetail(){
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Shop/"+shopId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_dsc.setText(snapshot.child("desc").getValue().toString());
                tv_phone.setText(snapshot.child("phone").getValue().toString());
                if(snapshot.child("calendar").exists()){
                    String path;
                    for (int i =0; i<14; i++){
                        if(i<7){
                            path = (i+2)+"O";
                        }
                        else {
                            path = (i-5)+"C";
                        }
                        String value = snapshot.child("calendar").child(path).getValue().toString();
                        textViews[i].setText(value);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}