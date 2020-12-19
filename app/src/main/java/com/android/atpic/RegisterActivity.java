package com.android.atpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.atpic.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.royrodriguez.transitionbutton.TransitionButton;
import com.royrodriguez.transitionbutton.utils.WindowUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TransitionButton btnRegister;
    private EditText etName, etEmail, etPassword;

    private Users users;

    DatabaseReference database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        WindowUtils.makeStatusbarTransparent(this);
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.register_button);

        users = new Users();

        btnRegister.setOnClickListener(this);
    }

    private void saveUser(){
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        DatabaseReference dbUser = database.child("users");

        String id = dbUser.push().getKey();

        users.setId(id);
        users.setName(name);
        users.setEmail(email);
        users.setPassword(password);
        users.setCredit(0);
        users.setCart("");

        dbUser.child(id).setValue(users);

        Toast.makeText(this, "Register Success!" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.register_button){
            btnRegister.startAnimation();

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            boolean isEmpty = false;

            if (TextUtils.isEmpty(name)){
                isEmpty = true;
                etName.setError("Field cannot be blank!");
            }
            if (TextUtils.isEmpty(email)){
                isEmpty = true;
                etEmail.setError("Field cannot be blank!");
            }
            if (TextUtils.isEmpty(password)){
                isEmpty = true;
                etPassword.setError("Field cannot be blank!");
            }


            if (!isEmpty) {
                // Do your networking task or background work here.
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                            btnRegister.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                                @Override
                                                public void onAnimationStopEnd() {
                                                    users.setId(mAuth.getUid());
                                                    users.setName(name);
                                                    users.setEmail(email);
                                                    users.setPassword(password);
                                                    users.setCredit(0);
                                                    users.setCart("");
                                                    database.child("users").child(mAuth.getUid()).setValue(users);
                                                    finish();
                                                }
                                            });
                                        } else{
                                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                                            btnRegister.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                                        }
                                    }
                                });
                    }
                }, 2000);

            } else {
                btnRegister.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            }
        } else{

        }
    }

    public void btnLogin(View view) {
        finish();
    }
}