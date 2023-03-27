package com.project.agroworld.payment.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.databinding.TransactionItemLayoutBinding;
import com.project.agroworld.payment.model.PaymentModel;
import com.project.agroworld.payment.viewholder.HistoryViewHolder;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private final HistoryListener listener;
    private final List<PaymentModel> paymentModelList;

    public HistoryAdapter(List<PaymentModel> paymentModelList, HistoryListener clickListener) {
        this.paymentModelList = paymentModelList;
        this.listener = clickListener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(TransactionItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        PaymentModel paymentModel = paymentModelList.get(position);
        holder.bindHistoryData(paymentModel, listener);
    }

    @Override
    public int getItemCount() {
        return paymentModelList.size();
    }
}
