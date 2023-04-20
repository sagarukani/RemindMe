package com.example.remindme.home;

import static android.content.Context.NOTIFICATION_SERVICE;

import static com.example.remindme.NotificationClass.channelID;
import static com.example.remindme.NotificationClass.messageExtra;
import static com.example.remindme.NotificationClass.notificationID;
import static com.example.remindme.NotificationClass.titleExtra;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.remindme.HomeViewModal;
import com.example.remindme.MyPreferences;
import com.example.remindme.Notes;
import com.example.remindme.NotificationClass;
import com.example.remindme.R;
import com.example.remindme.horizontalcalendar.HorizontalCalendar;
import com.example.remindme.horizontalcalendar.utils.HorizontalCalendarListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class HomeFragment extends Fragment implements ReminderAdapter.ItemClickListener{

    private HorizontalCalendar horizontalCalendar;
    private LinearLayout llAddReminder;
    private HomeViewModal viewModal;
    private Date date = Calendar.getInstance().getTime();
    ReminderAdapter adapter;
    List<Notes> noteList = new ArrayList<>();
    RecyclerView rvNotes;
    TextView tvNoData;
    MyPreferences myPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myPreferences  = MyPreferences.getPreferences(getContext());

        llAddReminder = view.findViewById(R.id.llAddReminder);
        rvNotes = view.findViewById(R.id.rvNotes);
        tvNoData = view.findViewById(R.id.tvNoData);
        llAddReminder.setOnClickListener(v -> {
            showDialog(getContext());
        });

        viewModal = ViewModelProviders.of(this).get(HomeViewModal.class);

        viewModal.getAllCourses().observe(getViewLifecycleOwner(), notes -> {
            Log.d("TAG", new Gson().toJson(notes));
            noteList = notes;
            adapter.AddAllList(noteList);
            if(adapter.getAllList().size()==0){
                tvNoData.setVisibility(View.VISIBLE);
                rvNotes.setVisibility(View.GONE);
            }else {
                tvNoData.setVisibility(View.GONE);
                rvNotes.setVisibility(View.VISIBLE);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        adapter = new ReminderAdapter();
        rvNotes.setHasFixedSize(true);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotes.setAdapter(adapter);
        adapter.addItemClickListener(this);
    }

    private void showDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.dialog_add_reminder);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        LinearLayout llTime = dialog.findViewById(R.id.llTime);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        TextView tvTime = dialog.findViewById(R.id.tvTime);
        EditText etTitle = dialog.findViewById(R.id.etTitle);
        EditText etDetails = dialog.findViewById(R.id.etDetails);

        ivClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        llTime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(v.getContext(), (timePicker, selectedHour, selectedMinute) -> {
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());
                    final Date dateObj = sdf.parse(selectedHour + ":" + selectedMinute);
                    tvTime.setText(new SimpleDateFormat("K:mm a", Locale.getDefault()).format(dateObj));
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
            }, hour, minute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        btnAdd.setOnClickListener(v -> {
            if (!etTitle.getText().toString().equals("") && !etDetails.getText().toString().equals("") && !tvTime.getText().toString().equals("")) {
                Notes notes = new Notes();
                notes.setTitle(etTitle.getText().toString().trim());
                notes.setDetails(etDetails.getText().toString().trim());
                notes.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date));
                notes.setTime(tvTime.getText().toString().trim());
                notes.setDone(false);
                viewModal.insert(notes);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(myPreferences.isNotification()){
                        scheduleNotification(notes);
                    }
                }
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
            }

        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scheduleNotification(Notes notes) {
        Intent intent = new Intent(getContext().getApplicationContext(), NotificationClass.class);
        String title = notes.getTitle();
        String message = notes.getDetails();
        intent.putExtra(titleExtra, title);
        intent.putExtra(messageExtra, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext().getApplicationContext(),
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Long time = getTime(notes.getDate() + " " + notes.getTime());
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
    }

    private Long getTime(String s) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy K:mm a", Locale.getDefault());
            Date date = format.parse(s);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Log.d("TIME", new Gson().toJson(date.getTime()));
            return calendar.getTimeInMillis();
        } catch (Exception e) {
            Calendar calendar = Calendar.getInstance();
            return calendar.getTimeInMillis();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = "Notification Channel";
        String desc = "Shows reminder notification";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(channelID, name, importance);
        channel.setDescription(desc);
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .textSize(14f, 24f, 14f)
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .end()
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar selectedDate, int position) {
                date = selectedDate.getTime();
                Log.d("DATE", new Gson().toJson(selectedDate.getTime()));
            }

        });


        return rootView;
    }

    @Override
    public void onItemClick(View v,int position, Notes notes) {
        if(v.getId()==R.id.ivDelete){
            Log.d("TAG","delete");
            viewModal.delete(notes);
        }else if(v.getId()==R.id.ivCheck){
            Log.d("TAG","check");
            notes.setDone(!notes.getDone());
            viewModal.insert(notes);
        }
    }
}