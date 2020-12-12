package com.android.atpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.atpic.adapter.CartAdapter;
import com.android.atpic.model.Product;
import com.android.atpic.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    public final static String EXTRA_PRODUCT= "extra_product";
    public final static String EXTRA_USERS= "extra_users";

    TextView name, email, credit, subtotal, taxes, total;
    RecyclerView recyclerView;
    Button btnPay, btnBack;

    ArrayList<Product> productList;
    Product product;
    Users users;
    CartAdapter adapter;
    long price;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        productList = new ArrayList<Product>();
        users = new Users();
        productList = getIntent().getParcelableArrayListExtra(EXTRA_PRODUCT);
        users = getIntent().getParcelableExtra(EXTRA_USERS);

        database = FirebaseDatabase.getInstance().getReference();

        adapter = new CartAdapter(this);

        name = findViewById(R.id.tv_buyerName);
        email = findViewById(R.id.tv_buyerEmail);
        credit = findViewById(R.id.tv_credit);
        recyclerView = findViewById(R.id.rv_product);
        subtotal = findViewById(R.id.tv_subtotal);
        taxes = findViewById(R.id.tv_taxes);
        total = findViewById(R.id.tv_total);
        btnPay = findViewById(R.id.btn_pay);
        btnBack = findViewById(R.id.iconBack);

        adapter.setProductList(productList);
        recyclerView.setAdapter(adapter);
        price = 0;

        for (Product p : productList){
            price += p.getPrice();
        }

        database.child("users").child(users.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = snapshot.getValue(Users.class);

                name.setText(users.getName());
                email.setText(users.getEmail());
                credit.setText(String.valueOf("Rp" + users.getCredit()));
                subtotal.setText(String.valueOf("Rp" + price));
                taxes.setText("Rp10000");
                total.setText(String.valueOf("Rp" + (price + 10000)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (users.getCredit() < (price+10000)){
                    Toast.makeText(PaymentActivity.this, "Not enough credit", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaymentActivity.this, TopUpActivity.class);
                    intent.putExtra(TopUpActivity.EXTRA_USERS, users);
                    startActivity(intent);
                } else{
                    for (Product list : productList){
                        list.setSold(list.getSold() + 1);
                        database.child("product").child(list.getId()).setValue(list);
                    }
                    users.setCredit(users.getCredit() - (price+10000));
                    database.child("users").child(users.getId()).setValue(users);
                    startActivity(new Intent(PaymentActivity.this, SuccessActivity.class));
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}