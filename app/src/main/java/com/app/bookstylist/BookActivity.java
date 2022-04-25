package com.app.bookstylist;

import static com.app.bookstylist.detail.ServiceAdapter.withLargeIntegers;

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
import com.app.bookstylist.bookstylist_interface.IClickServiceBook;
import com.app.bookstylist.databinding.ActivityBookBinding;
import com.app.bookstylist.shop.Service;
import com.app.bookstylist.shop.ShopModal;
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
    private List<Service> listService;
    private ServiceAdapter adapter;
    private Integer totalBill = 0;

    private LocalTime timeBook;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private MaterialAlertDialogBuilder war;
    private ShopModal shopModal;


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
        adapter = new ServiceAdapter(listService, this, new IClickServiceBook() {
            @Override
            public void onClickTimeListener(Service service) {
                adđBilling(service);
            }
        });
        rcvService.setAdapter(adapter);
        rcvService.setLayoutManager(linearLayoutManager);

        getShop(Integer.valueOf(intent.getStringExtra("id")));
        getService(intent.getStringExtra("service choose"));

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
                if(dateBook==null){
                    war = new MaterialAlertDialogBuilder(BookActivity.this,R.style.MaterialAlertDialog_Theme);
                    war.setTitle("Thông báo");
                    war.setMessage("Vui lòng chọn ngày trước.");
                    war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dateBook = null;
                            binding.txtday.setText("Vui lòng chọn");
                            binding.txttime.setText("Vui lòng chọn");
                        }
                    });
                    war.show();
                }else{
                    materialTimePicker.show(getSupportFragmentManager(), "Time Pick");
                }

            }
        });

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
                        war = new MaterialAlertDialogBuilder(BookActivity.this,R.style.MaterialAlertDialog_Theme);
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
                        war = new MaterialAlertDialogBuilder(BookActivity.this, R.style.MaterialAlertDialog_Theme);
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
                        war = new MaterialAlertDialogBuilder(BookActivity.this, R.style.MaterialAlertDialog_Theme);
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
                        war = new MaterialAlertDialogBuilder(BookActivity.this, R.style.MaterialAlertDialog_Theme);
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
                    war = new MaterialAlertDialogBuilder(BookActivity.this, R.style.MaterialAlertDialog_Theme);
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
                    war = new MaterialAlertDialogBuilder(BookActivity.this, R.style.MaterialAlertDialog_Theme);
                    war.setTitle("Thông báo");
                    war.setMessage("Vui lòng kiểm tra bạn đã chọn dịch vụ chưa.");
                    war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    war.show();
                }else if(dateBook == null || timeBook == null){
                    war = new MaterialAlertDialogBuilder(BookActivity.this, R.style.MaterialAlertDialog_Theme);
                    war.setTitle("Thông báo");
                    war.setMessage("Vui lòng đặt lịch trong phạm vi từ 7:00 AM đến 19:00 PM. Để chúng tôi có thể phục vụ cho bạn. ");
                    war.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            binding.txttime.setText("Vui lòng chọn");
                            binding.txtday.setText("Vui lòng chọn");
                        }
                    });
                    war.show();
                }else {
                    String service = "";
                    LocalDateTime time = LocalDateTime.of(dateBook.getYear(),dateBook.getMonth(),dateBook.getDayOfMonth(),timeBook.getHour(),timeBook.getMinute());
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

                    if(adapter.getSelectedService().size()>1){
                        for(Service z : adapter.getSelectedService()){
                            if(z.getName() == adapter.getSelectedService().get(0).getName()){
                                service = z.getName();
                            }else{
                                service = service + ", "+z.getName();
                            }
                        }
                    }else{
                        service = adapter.getSelectedService().get(0).getName();
                    }

                    bookingNow(String.valueOf(shopModal.getId()),service, time.format(dateTimeFormatter));
                }
            }
        });




    }



    private void adđBilling(Service service) {
        if(service.isChecked()){
            totalBill = totalBill + service.getPrice();
        }else {
            totalBill = totalBill - service.getPrice();
        }
        binding.billService.setText(withLargeIntegers(totalBill)+" VNĐ");
    }

    private void getShop(Integer shopId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Shop");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ShopModal check = dataSnapshot.getValue(ShopModal.class);
                    if(check.getId() == shopId){
                        shopModal = check;
                    }
                }
                binding.nameShop.setText(shopModal.getName());
                binding.addressShop.setText(shopModal.getAddress());
                Glide.with(getApplicationContext()).load(shopModal.getImage()).into(binding.imageShop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    Service serviceCheck = dataSnapshot.getValue(Service.class);
                    if(serviceCheck.getShopId() == shopModal.getId()){
                        if(serviceCheck.getName().equals(service)){
                            totalBill = serviceCheck.getPrice();
                            serviceCheck.setCheckSelect(true);

                        }
                        listService.add(serviceCheck);
                    }
                }
                binding.btnBook.setVisibility(View.VISIBLE);
                binding.billService.setText(withLargeIntegers(totalBill)+ " VNĐ");
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bookingNow(String IdShop, String Service, String Time){

        war = new MaterialAlertDialogBuilder(BookActivity.this, R.style.MaterialAlertDialog_Theme);
        war.setTitle("Đặt lịch thành công");
        war.setMessage("Cảm ơn đã sử dụng dịch vụ của chúng thôi");
        war.setCancelable(false);
        war.setPositiveButton("Quay lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(BookActivity.this, DashboardUserActivity.class));
            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books").push();
        HashMap<String, Object> book = new HashMap<>();
        book.put("uid", firebaseAuth.getUid());
        book.put("sid", IdShop);
        book.put("service", Service);
        book.put("time", Time);
        book.put("complete", "Đợi xác nhận");
        book.put("price",totalBill);
        book.put("bid", ref.getKey());
        ref.setValue(book);
        war.show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                war = new MaterialAlertDialogBuilder(BookActivity.this, R.style.MaterialAlertDialog_Theme);
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
        Intent intent = new Intent(BookActivity.this, ShopActivity.class);
        intent.putExtra("name",shopModal.getName());
        intent.putExtra("address", shopModal.getAddress());
        intent.putExtra("id",String.valueOf(shopModal.getId()));
        intent.putExtra("img",shopModal.getImage());
        intent.putExtra("rate",String.valueOf(shopModal.getRating()));
        intent.putExtra("comment",String.valueOf(shopModal.getComment()));
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}

