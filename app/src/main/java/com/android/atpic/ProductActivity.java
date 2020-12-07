package com.android.atpic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.atpic.adapter.DotIndicatorPager2Adapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ProductActivity extends AppCompatActivity {

    DotsIndicator dotsIndicator;
    ViewPager2 viewPager;
    DotIndicatorPager2Adapter adapter;
    LottieAnimationView cart;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        cart = (LottieAnimationView)findViewById(R.id.lav_cart);

        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        viewPager = (ViewPager2) findViewById(R.id.view_pager2);
        adapter = new DotIndicatorPager2Adapter();
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager);

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
                    //---- Your code here------
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