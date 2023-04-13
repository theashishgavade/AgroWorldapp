package com.project.agroworldapp.taskmanager.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityAddTaskBinding;
import com.project.agroworldapp.db.FarmerModel;
import com.project.agroworldapp.taskmanager.model.DateModel;
import com.project.agroworldapp.taskmanager.model.TimeModel;
import com.project.agroworldapp.taskmanager.receiver.EventReceiver;
import com.project.agroworldapp.taskmanager.viewmodel.FarmerViewModel;
import com.project.agroworldapp.utils.Constants;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    String timeToNotify;
    int maxIDCount;
    private ActivityAddTaskBinding binding;
    private FarmerViewModel viewModel;
    private TimeModel timeModel;
    private DateModel dateModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_task);
        Intent intent = getIntent();
        maxIDCount = intent.getIntExtra("maxIDCount", maxIDCount);
        viewModel = new ViewModelProvider(this).get(FarmerViewModel.class);
        binding.ivSelectTime.setOnClickListener(v -> showTimerPickerDialog());
        binding.ivSelectDate.setOnClickListener(v -> showDatePickerDialog());
        binding.tvDate.setOnClickListener(v -> showDatePickerDialog());
        binding.tvTime.setOnClickListener(v -> showTimerPickerDialog());

        binding.ivPriority.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, binding.ivPriority);
            popupMenu.getMenuInflater().inflate(R.menu.priority_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                binding.tvPriority.setText(menuItem.getTitle());
                Constants.showToast(AddTaskActivity.this, menuItem.getTitle().toString());
                return true;
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
                return;
            }
            if (date.isEmpty()) {
                binding.tvDate.setError(getString(R.string.this_field_required));
                return;
            }
            if (task.isEmpty()) {
                binding.etRoutine.setError(getString(R.string.this_field_required));
                return;
            }
            if (desc.isEmpty()) {
                binding.etDecs.setError(getString(R.string.this_field_required));
                return;
            }
            FarmerModel farmerModel = new FarmerModel();
            farmerModel.setTask(task);
            farmerModel.setDesc(desc);
            farmerModel.setDate(date);
            farmerModel.setTime(time);
            viewModel.insert(farmerModel);
            setTaskRemainder(task, desc);
            Constants.showToast(AddTaskActivity.this, getString(R.string.routine_added));
            finish();
        });
    }

    public String formatTime(int hour, int minute) {
        String time;
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
        calendar.set(Calendar.MONTH, dateModel.getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dateModel.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, timeModel.getHour());
        calendar.set(Calendar.MINUTE, timeModel.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), EventReceiver.class);
        intent.setAction("com.project.agroworld");
        intent.putExtra("task", task);
        intent.putExtra("desc", desc);
        intent.putExtra("date", binding.tvDate.getText().toString());
        intent.putExtra("time", binding.tvTime.getText().toString());
        intent.putExtra("maxIDCount", maxIDCount);
        intent.putExtra("setNotify", "SetNotification");
        printLog(calendar.toString());
        printLog("updatingTaskMaxID " + maxIDCount);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, maxIDCount, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void showTimerPickerDialog() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
            printLog("selectedTime " + selectedHour + ":" + selectedMinute);
            timeToNotify = selectedHour + ":" + selectedMinute;
            timeModel = new TimeModel(selectedHour, selectedMinute, timeToNotify);
            binding.tvTime.setText(formatTime(selectedHour, selectedMinute));
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, day1) -> {
            dateModel = new DateModel(year1, month1 + 1, day1);
            printLog("selectedDate " + day1 + "-" + month1 + "-" + year1);
            binding.tvDate.setText(day1 + "-" + (month1 + 1) + "-" + year1);
        }, year, month, day);
        datePickerDialog.show();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void printLog(String message) {
        Log.d("AddTaskActivity", message);
    }
}