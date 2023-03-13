package com.project.agroworld.ui.payment.model;

public class PaymentModel {

    private String productName;
    private String productPrice;
    private String paymentStatus;
    private String paymentID;
    private String paymentData;
    private String orderID;

    public PaymentModel(String productName, String productPrice, String paymentStatus, String paymentID, String orderID) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.paymentStatus = paymentStatus;
        this.paymentID = paymentID;
        this.orderID = orderID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(String paymentData) {
        this.paymentData = paymentData;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
