package com.project.agroworld.ui.payment;

import static com.project.agroworld.utils.Constants.GOOGLE_PAY_REQ_CODE;
import static com.project.agroworld.utils.Constants.NAME;
import static com.project.agroworld.utils.Constants.TRANSACTION_NOTE;
import static com.project.agroworld.utils.Constants.UPI_ID;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityPaymentDetailsBinding;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.Permissions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class PaymentDetailsActivity extends AppCompatActivity {

    public static final String GOOGLE_PAY_PKG_NAME = "com.google.android.apps.nbu.paisa.user";
    private ActivityPaymentDetailsBinding binding;
    private ArrayList<ProductModel> productCartList = new ArrayList<>();
    String uniqueID = UUID.randomUUID().toString();
    String address;
    String totalAmount;
    StringBuilder stringBuilder = new StringBuilder();
    String status;
    Uri uri;

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

        binding.btnProceed.setOnClickListener(v -> {
            String[] amount = binding.tvTotalPayAmt.getText().toString().split(" ");
            String filterAmount = amount[1];
            if (!filterAmount.isEmpty()) {
                uri = getUpiPaymentUri(NAME, UPI_ID, TRANSACTION_NOTE, filterAmount);
                Log.d("payURI", uri.toString());
                payWithGPay();
            }
        });
    }

    private void payWithGPay() {
        if (isPackageInstalled(this, GOOGLE_PAY_PKG_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PKG_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQ_CODE);
        } else {
            Toast.makeText(this, "Please Install Google Pay", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resCode, Intent data) {
        super.onActivityResult(requestCode, resCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }
        if ((RESULT_OK == requestCode) && status.equals("success")) {
            Toast.makeText(this, "Transactions Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Transactions Failed", Toast.LENGTH_SHORT).show();
        }

        if ((RESULT_OK == requestCode)) {
            if (data != null) {
                String trxt = data.getStringExtra("response");
                Log.e("UPI", "onActivityResult: " + trxt);
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add(trxt);
                upiPaymentDataOperation(dataList);
            } else {
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        } else {
            //when user simply back without payment
            Log.e("UPI", "onActivityResult: " + "Return data is null");
            ArrayList<String> dataList = new ArrayList<>();
            dataList.add("nothing");
            upiPaymentDataOperation(dataList);
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (Permissions.checkConnection(this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successful: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isPackageInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        Log.d("uniqueID", uniqueID);
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tr", uniqueID)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", "2")
                .appendQueryParameter("cu", "INR")
                .build();
    }
}