package com.project.agroworld.ui.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityPaymentDetailsBinding;
import com.project.agroworld.ui.payment.model.PaymentModel;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.viewmodel.AgroViewModel;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentDetailsActivity extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {

    ActivityPaymentDetailsBinding binding;
    private ArrayList<ProductModel> productCartList = new ArrayList<>();
    String address;
    String totalAmount;
    StringBuilder stringBuilder = new StringBuilder();
    private AlertDialog.Builder alertDialogBuilder;
    private DatabaseReference firebaseDatabase;
    private AgroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_details);
        viewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        viewModel.init();
        Checkout.preload(getApplicationContext());
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

        alertDialogBuilder = new AlertDialog.Builder(PaymentDetailsActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });

        viewModel.checkLoadingStatus().observe(this, s -> {
            Constants.showToast(this, s);
        });

        binding.btnProceed.setOnClickListener(v -> {
            startPayment();
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout checkout = new Checkout();
        checkout.setKeyID(Constants.RAZORPAY_KEY_ID);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Agro World");
            options.put("description", "Seeds shopping");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", Constants.APP_ICON_LINK);
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "bhavesh.patil0325@gmail.com");
            preFill.put("contact", "8591347448");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try {
            uploadTransactionDetailToFirebase(paymentData);
            showAlertMessageWithStatus(paymentData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            uploadTransactionDetailToFirebase(paymentData);
            showAlertMessageWithStatus(paymentData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            uploadTransactionDetailToFirebase(paymentData);
            showAlertMessageWithStatus(paymentData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void uploadTransactionDetailToFirebase(PaymentData paymentData) throws JSONException {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("transaction");
        PaymentModel paymentModel;
        for (int product = 0; product < productCartList.size(); product++) {
            paymentModel = new PaymentModel(productCartList.get(product).getTitle(),
                    String.valueOf(productCartList.get(product).getPrice()),
                    paymentData.getData().get("_silent").toString(),
                    paymentData.getPaymentId(),
                    paymentData.getExternalWallet()
            );
            viewModel.uploadTransaction(paymentModel, productCartList.get(product).getTitle());
        }
    }

    public void showAlertMessageWithStatus(PaymentData paymentData) throws JSONException {
        boolean isSuccess = (boolean) paymentData.getData().get("_silent");
        if (isSuccess) {
            alertDialogBuilder.setMessage("Payment successful :\nPayment ID: " + paymentData.getPaymentId() + "\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } else {
            alertDialogBuilder.setMessage("Payment failed :\nPayment ID: " + paymentData.getPaymentId() + "\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        }
    }
}