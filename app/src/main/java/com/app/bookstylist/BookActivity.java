package com.app.bookstylist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.book.ServiceAdapter;
import com.app.bookstylist.book.ServiceCheck;
import com.app.bookstylist.databinding.ActivityBookBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class BookActivity extends AppCompatActivity {
    private LocalDateTime dateBook;
    private ActivityBookBinding binding;
    private RecyclerView  rcvService;
    private List<ServiceCheck> listService;
    private ServiceAdapter adapter;

    private LocalTime timeBook;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private MaterialAlertDialogBuilder war;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        Toolbar toolbar = binding.topAppBar;
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder calenderConstraints = new CalendarConstraints.Builder();
        calenderConstraints.setValidator(DateValidatorPointForward.now());

        builder.setTitleText("Chọn Ngày");
        builder.setTheme(R.style.datepicker);
        builder.setCalendarConstraints(calenderConstraints.build());

        final MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                                .setTitleText("Chọn giờ")

                                .setTheme(R.style.timepicker)
                                .build();
        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH : mm");


        rcvService = findViewById(R.id.recycleService);
        listService = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookActivity.this, LinearLayoutManager.HORIZONTAL, false);
        adapter = new ServiceAdapter(listService,this);
        rcvService.setAdapter(adapter);
        rcvService.setLayoutManager(linearLayoutManager);

        getService(intent.getStringExtra("service"));

        //Biến tạm


        binding.btnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(),"Date Pick");

            }
        });
        binding.btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialTimePicker.show(getSupportFragmentManager(), "Time Pick");
            }
        });
        binding.nameShop.setText(intent.getStringExtra("name"));
        binding.addressShop.setText(intent.getStringExtra("address"));


        Glide.with(getApplicationContext()).load(intent.getStringExtra("img")).into(binding.imageShop);
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                dateBook = LocalDateTime.ofInstant(Instant.ofEpochMilli(materialDatePicker.getSelection()), TimeZone.getDefault().toZoneId());
                binding.txtday.setText(dateBook.format(dateTimeFormatter));
            }
        });

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDateTime checkTime = LocalDateTime.now();
                if(dateBook.getMonth()==checkTime.getMonth()
                        && dateBook.getDayOfMonth() == checkTime.getDayOfMonth()
                        && dateBook.getYear() == checkTime.getYear()){
                    if(checkTime.getHour() >= 19 && checkTime.getMinute() >= 30  ){
                        // quá ngày
                        war = new MaterialAlertDialogBuilder(BookActivity.this);
                        war.setTitle("Thông báo");
                        war.setMessage("Vui lòng đặt lịch vào ngày hôm sau. Để chúng tôi có thể phục vụ cho bạn. ");
                        war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dateBook = null;
                                binding.txtday.setText("Vui lòng chọn lại");
                                binding.txttime.setText("Vui lòng chọn lại");
                            }
                        });
                        war.show();
                    }else if((materialTimePicker.getHour() == checkTime.getHour() && (materialTimePicker.getMinute() <= checkTime.getMinute()))
                            || materialTimePicker.getHour() < checkTime.getHour()){
                        //Cùng date nhưng nhỏ time
                        LocalTime messTime = LocalTime.of(0,0);

                        if(checkTime.getMinute()>=30){
                            messTime = LocalTime.of(checkTime.getHour()+1,0);
                        }else{
                            messTime = LocalTime.of(checkTime.getHour(),checkTime.getMinute());
                        }
                        String timeWar = timeFormatter.format(messTime);
                        war = new MaterialAlertDialogBuilder(BookActivity.this);
                        war.setTitle("Thông báo");
                        war.setMessage("Vui lòng đặt lịch trong phạm vi từ "+timeWar+" đến 19:00 PM. Để chúng tôi có thể phục vụ cho bạn. ");
                        war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                binding.txttime.setText("Vui lòng chọn lại");
                            }
                        });
                        war.show();
                    }else if( materialTimePicker.getHour() < 7){
                        war = new MaterialAlertDialogBuilder(BookActivity.this);
                        war.setTitle("Thông báo");
                        war.setMessage("Vui lòng đặt lịch trong phạm vi từ 7:00 AM đến 19:00 PM. Để chúng tôi có thể phục vụ cho bạn. ");
                        war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                binding.txttime.setText("Vui lòng chọn lại");
                            }
                        });
                        war.show();
                    } else if(materialTimePicker.getHour() > 19){
                        //Sau 19h
                        war = new MaterialAlertDialogBuilder(BookActivity.this);
                        war.setTitle("Thông báo");
                        war.setMessage("Vui lòng đặt lịch vào ngày hôm sau. Để chúng tôi có thể phục vụ cho bạn. ");
                        war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dateBook = null;
                                binding.txtday.setText("Vui lòng chọn lại");
                                binding.txttime.setText("Vui lòng chọn lại");
                            }
                        });
                        war.show();
                    } else {
                            timeBook = LocalTime.of(materialTimePicker.getHour(),materialTimePicker.getMinute());
                            binding.txttime.setText(timeFormatter.format(timeBook));
                    }

                } else if(materialTimePicker.getHour() > 19 || materialTimePicker.getHour() < 7){
                    war = new MaterialAlertDialogBuilder(BookActivity.this);
                    war.setTitle("Thông báo");
                    war.setMessage("Vui lòng đặt lịch trong phạm vi từ 7:00 AM đến 19:00 PM. Để chúng tôi có thể phục vụ cho bạn. ");
                    war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            binding.txttime.setText("Vui lòng chọn lại");
                        }
                    });
                    war.show();
                }else {
                    timeBook = LocalTime.of(materialTimePicker.getHour(),materialTimePicker.getMinute());
                    binding.txttime.setText(timeFormatter.format(timeBook));
                }
            }
        });

        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getSelectedService().size() == 0){
                    war = new MaterialAlertDialogBuilder(BookActivity.this);
                    war.setTitle("Thông báo");
                    war.setMessage("Vui lòng kiểm tra bạn đã chọn dịch vụ chưa.");
                    war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    war.show();
                }else if(dateBook == null || timeBook == null){
                    war = new MaterialAlertDialogBuilder(BookActivity.this);
                    war.setTitle("Thông báo");
                    war.setMessage("Vui lòng đặt lịch trong phạm vi từ 7:00 AM đến 19:00 PM. Để chúng tôi có thể phục vụ cho bạn. ");
                    war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            binding.txttime.setText("Vui lòng chọn lại");
                        }
                    });
                    war.show();
                }else {
                    String service = "";
                    LocalDateTime time = LocalDateTime.of(dateBook.getYear(),dateBook.getMonth(),dateBook.getDayOfMonth(),timeBook.getHour(),timeBook.getMinute());
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

                    if(adapter.getSelectedService().size()>1){
                        for(ServiceCheck z : adapter.getSelectedService()){
                            if(z.getName() == adapter.getSelectedService().get(0).getName()){
                                service = z.getName();
                            }else{
                                service = service + ", "+z.getName();
                            }
                        }
                    }else{
                        service = adapter.getSelectedService().get(0).getName();
                    }


                    bookingNow(intent.getStringExtra("id"),service, time.format(dateTimeFormatter));
                }
            }
        });

    }

    private void getService(String service) {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("services");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listService.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(service.contains(dataSnapshot.getKey())){
                        ServiceCheck service  = dataSnapshot.getValue(ServiceCheck.class);
                        listService.add(service);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bookingNow(String IdShop, String Service, String Time){

        progressDialog.setMessage("Vui lòng đợi trong giây lát ...");

        HashMap<String, Object> book = new HashMap<>();
        book.put("uid", firebaseAuth.getUid());
        book.put("sid", IdShop);
        book.put("service", Service);
        book.put("time", Time);
        book.put("complete", "Đợi xác nhận");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.push().setValue(book);
        progressDialog.show();
        startActivityForResult(new Intent(BookActivity.this, DashboardUserActivity.class),2);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                war = new MaterialAlertDialogBuilder(BookActivity.this);
                war.setTitle("Thông báo");
                war.setMessage("Đã có lỗi xảy ra. Vui lòng thử lại");
                war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                war.show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(BookActivity.this, DashboardUserActivity.class));
        return super.onOptionsItemSelected(item);
    }
}

