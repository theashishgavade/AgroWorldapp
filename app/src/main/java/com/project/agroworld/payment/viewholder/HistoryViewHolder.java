package com.project.agroworld.payment.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.databinding.TransactionItemLayoutBinding;
import com.project.agroworld.payment.adapter.HistoryListener;
import com.project.agroworld.payment.model.PaymentModel;
import com.project.agroworld.utils.Constants;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    TransactionItemLayoutBinding binding;

    public HistoryViewHolder(TransactionItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindHistoryData(PaymentModel paymentModel, HistoryListener listener) {
        binding.tvTransTitle.setText(paymentModel.getProductName());
        binding.tvTransID.setText("Id: " + paymentModel.getPaymentID());
        binding.tvTransStatus.setText("Status: " + paymentModel.getPaymentStatus());
        binding.tvTransPrice.setText(" " + paymentModel.getProductPrice());
        Constants.bindImage(binding.ivHistoryProduct, paymentModel.getProductImage(), binding.ivHistoryProduct);
        binding.crdHistoryBanner.setOnClickListener(v -> {
            listener.onTransactionRemovedClick(paymentModel);
        });
    }
}
