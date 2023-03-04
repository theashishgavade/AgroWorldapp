package com.project.agroworld.ui.taskManager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityAddTaskBinding;
import com.project.agroworld.db.FarmerModel;
import com.project.agroworld.db.FarmerViewModel;
import com.project.agroworld.utils.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    private ActivityAddTaskBinding binding;
    private FarmerViewModel viewModel;
    private TimeModel timeModel;
    private DateModel dateModel;
    String timeToNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_task);
        viewModel = ViewModelProviders.of(this).get(FarmerViewModel.class);
        binding.ivSelectTime.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    timeToNotify = selectedHour + ":" + selectedMinute;
                    timeModel = new TimeModel(hour, minute, selectedHour);
                    binding.tvTime.setText(FormatTime(selectedHour, selectedMinute));
                }
            }, hour, minute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        binding.ivSelectDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    dateModel = new DateModel(year, month, day);
                    binding.tvDate.setText(day + "-" + (month + 1) + "-" + year);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        binding.ivPriority.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, binding.ivPriority);
            popupMenu.getMenuInflater().inflate(R.menu.priority_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    binding.tvPriority.setText(menuItem.getTitle());
                    Constants.showToast(AddTaskActivity.this, menuItem.getTitle().toString());
                    return true;
                }
            });
            popupMenu.show();
        });

        binding.btnTaskDone.setOnClickListener(v -> {
            String time = binding.tvTime.getText().toString();
            String date = binding.tvDate.getText().toString();
            String task = binding.etRoutine.getText().toString();
            String desc = binding.etDecs.getText().toString();
            if (time.isEmpty()) {
                binding.tvTime.setError(getString(R.string.this_field_required));
            }
            if (date.isEmpty()) {
                binding.tvDate.setError(getString(R.string.this_field_required));
            }
            if (task.isEmpty()) {
                binding.etRoutine.setError(getString(R.string.this_field_required));
            }
            if (desc.isEmpty()) {
                binding.etDecs.setError(getString(R.string.this_field_required));
            }

            if (!date.isEmpty() && !time.isEmpty() && !task.isEmpty() && !desc.isEmpty()) {
                FarmerModel farmerModel = new FarmerModel();
                farmerModel.setTask(task);
                farmerModel.setDesc(desc);
                farmerModel.setDate(date);
                farmerModel.setTime(time);
                viewModel.insert(farmerModel);
//                setTaskRemainder(task, desc);
                setAlarm(task, desc, date);
                Constants.showToast(AddTaskActivity.this, "Routine added successfully");
                finish();
            }
        });
    }

    private void setAlarm(String task, String desc, String date) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), EventReceiver.class);
        intent.putExtra("event", task);
        intent.putExtra("desc", desc);
        intent.putExtra("date", date);
        Log.d("date", date + "timeToNotify " + timeToNotify);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String datetime = date + " " + timeToNotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(datetime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finish();
    }

    public String FormatTime(int hour, int minute) {
        String time;
        time = "";
        String formattedMinute;
        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }
        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }
        return time;
    }

    private void setTaskRemainder(String task, String desc) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, dateModel.getYear());
        calendar.set(Calendar.MONTH, dateModel.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, dateModel.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, timeModel.getHour());
        calendar.set(Calendar.MINUTE, timeModel.getMinute());
        calendar.set(Calendar.AM_PM, Calendar.PM);
        Log.d("modelData", dateModel.getMonth() + " " + timeModel.getHour());
//        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent intent = new Intent(getApplicationContext(), EventReceiver.class);
        intent.putExtra("task", task);
        intent.putExtra("desc", desc);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 123, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}