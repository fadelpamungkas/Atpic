package com.android.atpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.atpic.adapter.DotIndicatorPager2Adapter;
import com.android.atpic.model.Product;
import com.android.atpic.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ProductActivity extends AppCompatActivity {

    DotsIndicator dotsIndicator;
    ViewPager2 viewPager;
    DotIndicatorPager2Adapter adapter;
    LottieAnimationView cart;
    Product product;
    TextView name, price, sales, author, created, category, description;
    Button btnBuy;
    int flag = 0;

    Users users;
    FirebaseAuth mAuth;
    DatabaseReference database;

    public final static String EXTRA_PARCEL = "extra_parcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        database = FirebaseDatabase.getInstance().getReference();
        users = new Users();
        mAuth = FirebaseAuth.getInstance();

        product = getIntent().getParcelableExtra(EXTRA_PARCEL);

        cart = findViewById(R.id.lav_cart);

        dotsIndicator = findViewById(R.id.dots_indicator);
        viewPager = findViewById(R.id.view_pager2);
        btnBuy = findViewById(R.id.btn_buyProduct);

        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        sales = findViewById(R.id.product_sales);
        author = findViewById(R.id.product_author);
        created = findViewById(R.id.product_created);
        category = findViewById(R.id.product_category);
        description = findViewById(R.id.product_description);

        name.setText(product.getName());
        price.setText(String.valueOf("Rp" + product.getPrice()));
        sales.setText(String.valueOf(product.getSold() + " sold"));
        author.setText(product.getId_user());
        created.setText(String.valueOf(product.getUpload_date()));
        category.setText(product.getId_category());
        description.setText(product.getDesc());

        adapter = new DotIndicatorPager2Adapter();
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager);

        database.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = snapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_PRODUCT, product);
                intent.putExtra(PaymentActivity.EXTRA_USERS, users);
                startActivity(intent);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cart.setProgress(0);
//                cart.pauseAnimation();
//                cart.playAnimation();

                if (flag == 0) {
                    cart.setMinAndMaxProgress(0f, 0.8f); //Here, calculation is done on the basis of start and stop frame divided by the total number of frames
                    cart.setProgress(0);
                    cart.playAnimation();
                    flag = 1;
                    Toast.makeText(ProductActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

                } else {
                    cart.reverseAnimationSpeed();
                    cart.playAnimation();
                    flag = 0;
                    Toast.makeText(ProductActivity.this, "Removed from cart", Toast.LENGTH_SHORT).show();
                    //---- Your code here------
                }
            }
        });


    }
}