package com.project.agroworld.payment.adapter;

import com.project.agroworld.payment.model.PaymentModel;

public interface HistoryListener {
    void onTransactionRemovedClick(PaymentModel paymentModel);
}
