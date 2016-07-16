package com.taxiapp.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.taxiapp.net.NetResponse;
import com.taxiapp.net.WebSession;
import com.taxiapp.vendor.app.R;

public class CustomerBillActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bill);

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (!getIntent().hasExtra("bill")) {
            ((TextView) findViewById(R.id.bill_header)).setText("Bill could not be generated!");
            return;
        }
        NetResponse bill = WebSession.gson.fromJson(getIntent().getStringExtra("bill"), NetResponse.class);

//        ((TextView) findViewById(R.id.advance_amount_paid)).setText("" + bill.getAdvance_amount_paid());
//        ((TextView) findViewById(R.id.bill_total_amt)).setText("" + bill.getBill_total_amt());
//        ((TextView) findViewById(R.id.billed_days)).setText("" + bill.getBilled_days());
        ((TextView) findViewById(R.id.billed_hrs)).setText("" + bill.getBilled_hrs());
        ((TextView) findViewById(R.id.billed_kms)).setText("" + bill.getBilled_kms());
        ((TextView) findViewById(R.id.cashtocollect)).setText("" + bill.getCashtocollect());


    }
}
