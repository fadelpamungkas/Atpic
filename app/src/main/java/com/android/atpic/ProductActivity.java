package com.android.atpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.atpic.adapter.DotIndicatorAdapter;
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

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    DotsIndicator dotsIndicator;
    ViewPager2 viewPager;
    DotIndicatorAdapter adapter;
    LottieAnimationView cart;
    Product product;
    Users productAuthor;
    String authorName;
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

        String photoURL = CustomClass.removeLastChar(product.getPhotoURL());
        String[] strs = photoURL.split(",");

        adapter = new DotIndicatorAdapter(this);
        adapter.setStrs(strs);
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager);

//        cartStatus(flag);

        database.child("users").child(product.getId_user()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productAuthor = snapshot.getValue(Users.class);

                if (productAuthor != null){
                    author.setText(productAuthor.getName());
                    name.setText(product.getName());
                    price.setText(String.valueOf("Rp" + product.getPrice()));
                    sales.setText(String.valueOf(product.getSold() + " sold"));
                    created.setText(String.valueOf(product.getUpload_date()));
                    category.setText(product.getId_category());
                    description.setText(product.getDesc());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = snapshot.getValue(Users.class);

//                String[] strs = users.getCart().split(",");
//
//                for (String s : strs){
//                    if (s.equals(product.getId())) {
//                        flag = 1;
//                        cartStatus(flag);
//                        break;
//                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Product> productList = new ArrayList<>();
                productList.add(product);
                Intent intent = new Intent(ProductActivity.this, PaymentActivity.class);
                intent.putParcelableArrayListExtra(PaymentActivity.EXTRA_PRODUCT, productList);
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
//                cartStatus(flag);
                if (flag == 0) {
                    flag = 1;
                    cart.setMinAndMaxProgress(0f, 0.8f); //Here, calculation is done on the basis of start and stop frame divided by the total number of frames
                    cart.setProgress(0);
                    cart.playAnimation();
                    Toast.makeText(ProductActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

                } else {
                    flag = 0;
                    cart.reverseAnimationSpeed();
                    cart.playAnimation();
                    Toast.makeText(ProductActivity.this, "Removed from cart", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void cartStatus(int flag){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (flag == 1){
            if (users.getCart().equals("")){
                users.setCart(product.getId());
                Log.d("ProductActivity", "cart equals null");
            } else {
                users.setCart(users.getCart() + "," + product.getId());
                Log.d("ProductActivity", "cart not null");
            }

            database.child("users").child(mAuth.getCurrentUser().getUid()).setValue(users);
        }
         else {
             Log.d("ProductActivity", "flag == 0");
        }
//        else{
//            if (users.getCart().contains(product.getId())){
//                users.getCart().replace(product.getId(), "");
//            }
//        }
    }
}