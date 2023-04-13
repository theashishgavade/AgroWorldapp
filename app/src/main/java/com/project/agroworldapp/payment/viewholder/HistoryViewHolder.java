package com.project.agroworldapp.payment.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.databinding.TransactionItemLayoutBinding;
import com.project.agroworldapp.payment.model.PaymentModel;
import com.project.agroworldapp.utils.Constants;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    final TransactionItemLayoutBinding binding;

    public HistoryViewHolder(TransactionItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindHistoryData(PaymentModel paymentModel) {
        binding.tvTransTitle.setText(paymentModel.getProductName());
        binding.tvTransID.setText("Id: " + paymentModel.getPaymentID());
        binding.tvTransStatus.setText("Status: " + paymentModel.getPaymentStatus());
        binding.tvTransPrice.setText(" " + paymentModel.getProductPrice());
        Constants.bindImage(binding.ivHistoryProduct, paymentModel.getProductImage(), binding.ivHistoryProduct);
    }
}
