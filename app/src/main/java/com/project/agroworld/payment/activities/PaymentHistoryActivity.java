package com.project.agroworld.payment.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityPaymentHistoryBinding;
import com.project.agroworld.payment.adapter.HistoryAdapter;
import com.project.agroworld.payment.model.PaymentModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;

import java.util.ArrayList;

public class PaymentHistoryActivity extends AppCompatActivity {

    private final ArrayList<PaymentModel> paymentModelArrayList = new ArrayList<>();
    ActivityPaymentHistoryBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    private HistoryAdapter historyAdapter;
    private AgroViewModel agroViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_history);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Transaction History");
        actionBar.setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        agroViewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        agroViewModel.init(this);

        if (Permissions.checkConnection(this)) {
            binding.tvNoDataFoundErr.setVisibility(View.GONE);
            getTransactionHistoryList(Constants.plainStringEmail(user.getEmail()));
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.shimmer.setVisibility(View.GONE);
            binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
            binding.tvNoDataFoundErr.setText(getString(R.string.check_internet_connection));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Permissions.checkConnection(this)) {
            binding.tvNoDataFoundErr.setVisibility(View.GONE);
            getTransactionHistoryList(Constants.plainStringEmail(user.getEmail()));
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.shimmer.setVisibility(View.GONE);
            binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
            binding.tvNoDataFoundErr.setText(getString(R.string.check_internet_connection));
        }
    }

    private void getTransactionHistoryList(String email) {
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        agroViewModel.getTransactionList(email).observe(this, paymentModelList -> {
            switch (paymentModelList.status) {
                case ERROR:
                    binding.shimmer.stopShimmer();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                    binding.tvNoDataFoundErr.setText(paymentModelList.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (paymentModelList.data != null) {
                        paymentModelArrayList.clear();
                        paymentModelArrayList.addAll(paymentModelList.data);
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                        binding.tvNoDataFoundErr.setText(getString(R.string.no_transaction_found));
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        historyAdapter = new HistoryAdapter(paymentModelArrayList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(historyAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.clearHistoryMn) {
            removeTransactionAlert();
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeTransactionAlert() {
        if (!paymentModelArrayList.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete history")
                    .setIcon(R.drawable.app_icon4)
                    .setMessage("Are you sure you want to clear history?")
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                        agroViewModel.clearAllHistory(Constants.plainStringEmail(user.getEmail()));
                    }).create().show();
        }else {
            Constants.showToast(this, "Nothing to delete");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(Constants.REQUEST_CODE);
    }
}