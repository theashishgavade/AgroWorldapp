package com.project.agroworldapp.payment.adapter;

import com.project.agroworldapp.payment.model.PaymentModel;

public interface HistoryListener {
    void onTransactionRemovedClick(PaymentModel paymentModel);
}
