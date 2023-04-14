package com.project.agroworldapp.payment.activities;

import android.os.Bundle;
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
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityPaymentHistoryBinding;
import com.project.agroworldapp.payment.adapter.HistoryAdapter;
import com.project.agroworldapp.payment.model.PaymentModel;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;

import java.util.ArrayList;
import java.util.Objects;

public class PaymentHistoryActivity extends AppCompatActivity {

    private final ArrayList<PaymentModel> paymentModelArrayList = new ArrayList<>();
    ActivityPaymentHistoryBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
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
        initializeAgroWorldViewModel();
        if (Permissions.checkConnection(this)) {
            binding.tvNoDataFoundErr.setVisibility(View.GONE);
            getTransactionHistoryList(Constants.plainStringEmail(Objects.requireNonNull(user.getEmail())));
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
        agroViewModel.getTransactionList(email);
        agroViewModel.observePaymentHistoryLiveData.observe(this, paymentModelList -> {
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
        HistoryAdapter historyAdapter = new HistoryAdapter(paymentModelArrayList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(historyAdapter);
    }

    public void initializeAgroWorldViewModel() {
        AgroWorldRepositoryImpl agroWorldRepository = new AgroWorldRepositoryImpl();
        agroViewModel = ViewModelProviders.of(this, new AgroWorldViewModelFactory(agroWorldRepository, this)).get(AgroViewModel.class);
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
}