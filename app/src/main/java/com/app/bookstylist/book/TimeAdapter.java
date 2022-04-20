package com.app.bookstylist.book;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;
import com.app.bookstylist.bookstylist_interface.IClickServiceBook;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {

    private List<Time> timeList;
    private IClickServiceBook clickTimeBook;

    public TimeAdapter(){

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TimeAdapter( String dateChoose, IClickServiceBook clickTimeBook){
        LocalDateTime date = LocalDateTime.now();
        LocalTime rule = LocalTime.of(19,30);
        List<Time> arrayList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        String dateAftFormat = date.format(dateTimeFormatter);
        if(dateChoose != null && dateChoose.equals(dateAftFormat)){
            if(date.toLocalTime().compareTo(rule) < 0){
                LocalTime run;
                if (date.toLocalTime().getMinute() < 5) {
                    run = LocalTime.of(date.getHour(),0);
                }else if (date.toLocalTime().getMinute() > 35){
                    run = LocalTime.of(date.getHour() + 1,0);
                }else {
                    run = LocalTime.of(date.getHour(),30);
                }
                arrayList.add(new Time(run.format(timeFormatter)));
                while (run.compareTo(rule)>0){
                    run.plusMinutes(30);
                    arrayList.add(new Time(run.format(timeFormatter)));
                }
            }
        }else {
            arrayList.add(new Time("7:00"));
            arrayList.add(new Time("7:30"));
            arrayList.add(new Time("8:00"));
            arrayList.add(new Time("8:30"));
            arrayList.add(new Time("9:00"));
            arrayList.add(new Time("9:30"));
            arrayList.add(new Time("10:00"));
            arrayList.add(new Time("10:30"));
            arrayList.add(new Time("11:00"));
            arrayList.add(new Time("11:30"));
            arrayList.add(new Time("12:00"));
            arrayList.add(new Time("12:30"));
            arrayList.add(new Time("13:00"));
            arrayList.add(new Time("13:30"));
            arrayList.add(new Time("14:00"));
            arrayList.add(new Time("14:30"));
            arrayList.add(new Time("15:00"));
            arrayList.add(new Time("15:30"));
            arrayList.add(new Time("16:00"));
            arrayList.add(new Time("16:30"));
            arrayList.add(new Time("17:00"));
            arrayList.add(new Time("17:30"));
            arrayList.add(new Time("18:00"));
            arrayList.add(new Time("18:30"));
            arrayList.add(new Time("19:00"));
        }
        this.timeList = arrayList;
        this.clickTimeBook = clickTimeBook;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_service,parent,false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        Time time = timeList.get(position);
        if(time == null){
            return;
        }
        holder.txtTime.setText(time.getStrTime());
        holder.rowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTimeBook.onClickTimeListener(time);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(timeList != null){
            return timeList.size();
        }
        return 0;
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTime;
        private RelativeLayout rowTime;
        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
