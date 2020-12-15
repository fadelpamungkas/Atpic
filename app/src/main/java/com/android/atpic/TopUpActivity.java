package com.android.atpic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.atpic.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TopUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etTopUp;
    private Button btnTopUp, btnBack;

    public static  final String EXTRA_USERS = "extra_users";

    private long credit;
    private Users users;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser authUsers = mAuth.getCurrentUser();

        etTopUp = findViewById(R.id.et_topup);
        btnTopUp = findViewById(R.id.btn_topup);
        btnBack = findViewById(R.id.iconBack);

        btnBack.setOnClickListener(this);
        btnTopUp.setOnClickListener(this);

        users = getIntent().getParcelableExtra(EXTRA_USERS);

        db = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_topup){
            topUp();
            finish();
        } else if(v.getId() == R.id.iconBack){
            finish();
        }
    }

    private void topUp() {
        String topup = etTopUp.getText().toString().trim();
        credit = users.getCredit() + Integer.parseInt(topup);

        users.setCredit(credit);
        db.child(users.getId()).setValue(users);
        Toast.makeText(TopUpActivity.this, "Top Up was Successful", Toast.LENGTH_SHORT).show();
    }
}