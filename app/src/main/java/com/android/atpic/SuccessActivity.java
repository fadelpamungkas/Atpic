package com.android.atpic;

import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.android.atpic.model.Product;
import com.android.atpic.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.royrodriguez.transitionbutton.TransitionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SuccessActivity extends AppCompatActivity {

    public final static String EXTRA_PRODUCT= "extra_product";
    public final static String EXTRA_USERS= "extra_users";

    LottieAnimationView success;
    TransitionButton btnHome;
    TextView tvProduct, tvEmail;

    ArrayList<Product> productList;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        productList = new ArrayList<>();
        users = new Users();
        productList = getIntent().getParcelableArrayListExtra(EXTRA_PRODUCT);
        users = getIntent().getParcelableExtra(EXTRA_USERS);

        btnHome = findViewById(R.id.btnHome);
        success = findViewById(R.id.lav_success);
        tvProduct = findViewById(R.id.tv_productName);
        tvEmail = findViewById(R.id.tv_userEmail);

        String name = "";

        for (Product p : productList){
            name += p.getName() + ",";
        }

        tvProduct.setText(CustomClass.removeLastChar(name) + " has been sent to");
        tvEmail.setText(users.getEmail());

        success.playAnimation();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHome.startAnimation();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        btnHome.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                            @Override
                            public void onAnimationStopEnd() {
                                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        });
                    }
                }, 1000);

            }
        });
    }
}