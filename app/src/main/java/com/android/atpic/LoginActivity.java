package com.android.atpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.royrodriguez.transitionbutton.TransitionButton;
import com.royrodriguez.transitionbutton.utils.WindowUtils;

public class LoginActivity extends AppCompatActivity {
    private TransitionButton transitionButton;
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WindowUtils.makeStatusbarTransparent(this);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        transitionButton = findViewById(R.id.login_button);

        transitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Then start the loading animation when the user tap the button
                transitionButton.startAnimation();

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                boolean isEmpty = false;

                if(TextUtils.isEmpty(email)){
                    isEmpty = true;
                    etEmail.setError("Enter email");
                }
                if(TextUtils.isEmpty(password)){
                    isEmpty = true;
                    etPassword.setError("Enter passord");
                }

                if (!isEmpty) {
                    // Do your networking task or background work here.
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                                    @Override
                                                    public void onAnimationStopEnd() {
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        startActivity(intent);
                                                    }
                                                });
                                            } else{
                                                Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                                                transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                                            }
                                        }
                                    });
                        }
                    }, 2000);
                } else {
                    transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                }
            }
        });

    }

    public void btnRegister(View view) {
        Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(intent);
    }

}