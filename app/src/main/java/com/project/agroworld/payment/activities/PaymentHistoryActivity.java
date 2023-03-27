package com.project.agroworld.payment.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityPaymentHistoryBinding;
import com.project.agroworld.payment.adapter.HistoryAdapter;
import com.project.agroworld.payment.adapter.HistoryListener;
import com.project.agroworld.payment.model.PaymentModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;

import java.util.ArrayList;

public class PaymentHistoryActivity extends AppCompatActivity implements HistoryListener {

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
        Log.d("customProgressCycle", "PaymentHistoryActivity- onCreate");
        agroViewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        agroViewModel.init(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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
                        binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                        binding.tvNoDataFoundErr.setText(getString(R.string.no_transaction_found));
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        historyAdapter = new HistoryAdapter(paymentModelArrayList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(historyAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(Constants.REQUEST_CODE);
    }

    @Override
    public void onTransactionRemovedClick(PaymentModel paymentModel) {

    }
}