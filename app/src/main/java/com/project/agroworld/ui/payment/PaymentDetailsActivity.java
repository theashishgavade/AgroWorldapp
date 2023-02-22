package com.project.agroworld.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityPaymentDetailsBinding;
import com.project.agroworld.ui.shopping.model.ProductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentDetailsActivity extends AppCompatActivity {
    private ActivityPaymentDetailsBinding binding;
    private ArrayList<ProductModel> productCartList = new ArrayList<>();
    String address;
    String totalAmount;
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_details);
        Intent intent = getIntent();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        productCartList = (ArrayList<ProductModel>) intent.getSerializableExtra("productItemList");
        address = intent.getStringExtra("address");
        totalAmount = intent.getStringExtra("totalAmount");

        for (int product = 0; product < productCartList.size(); product++) {
            stringBuilder.append(productCartList.get(product).getTitle()).append(" ₹ ").append(productCartList.get(product).getPrice()).append(" Q ").append(productCartList.get(product).getQuantity()).append("\n");
        }
        binding.tvProductNameAndPrice.setText("Product list- \n" + stringBuilder);
        binding.tvAddress.setText("Address- " + address);
        binding.tvPaymentDate.setText("Date- " + date);
        binding.tvTotalPayAmt.setText("Total- " + totalAmount);

    }
}