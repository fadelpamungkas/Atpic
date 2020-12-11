package com.android.atpic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.atpic.adapter.CartAdapter;
import com.android.atpic.model.Product;
import com.android.atpic.model.Users;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    public final static String EXTRA_PRODUCT= "extra_product";
    public final static String EXTRA_USERS= "extra_users";

    TextView name, email, credit, subtotal, taxes, total;
    RecyclerView recyclerView;
    Button btnPay;

    ArrayList<Product> productList;
    Product product;
    Users users;
    CartAdapter adapter;
    long price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        product = getIntent().getParcelableExtra(EXTRA_PRODUCT);
        users = getIntent().getParcelableExtra(EXTRA_USERS);

        adapter = new CartAdapter(this);

        productList = new ArrayList<Product>();

        name = findViewById(R.id.tv_buyerName);
        email = findViewById(R.id.tv_buyerEmail);
        credit = findViewById(R.id.tv_credit);
        recyclerView = findViewById(R.id.rv_product);
        subtotal = findViewById(R.id.tv_subtotal);
        taxes = findViewById(R.id.tv_taxes);
        total = findViewById(R.id.tv_total);
        btnPay = findViewById(R.id.btn_pay);

        productList.add(product);
        adapter.setProductList(productList);
        price = 0;

        for (Product p : productList){
            price += product.getPrice();
        }

        name.setText(users.getName());
        email.setText(users.getEmail());
        credit.setText(String.valueOf(users.getCredit()));
        subtotal.setText(String.valueOf("Rp" + price));
        taxes.setText("Rp10000");
        total.setText(String.valueOf("Rp" + (price + 10000)));

        recyclerView.setAdapter(adapter);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentActivity.this, SuccessActivity.class));
            }
        });
    }
}