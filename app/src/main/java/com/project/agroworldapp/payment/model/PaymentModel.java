package com.project.agroworldapp.payment.model;

public class PaymentModel {

    private String productName;

    private String productImage;
    private double productPrice;
    private String paymentStatus;
    private String paymentID;
    private String paymentData;

    public PaymentModel() {

    }

    public PaymentModel(String productName, String productImage, double productPrice, String paymentStatus, String paymentID) {
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.paymentStatus = paymentStatus;
        this.paymentID = paymentID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
