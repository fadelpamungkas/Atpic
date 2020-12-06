package com.example.atpic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.atpic.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText etName, etEmail, etPassword;

    private User user;

    DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDatabase = FirebaseDatabase.getInstance().getReference();

        etName = (EditText)findViewById(R.id.et_name);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPassword = (EditText)findViewById(R.id.et_password);
        btnRegister = (Button)findViewById(R.id.register_button);

        user = new User();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });
    }

    private void saveUser(){
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean isEmpty = false;

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            isEmpty = true;
            Toast.makeText(this, "Field can not be blank!", Toast.LENGTH_SHORT).show();
        }

        if(!isEmpty){
            DatabaseReference dbUser = userDatabase.child("user");
            String id = dbUser.push().getKey();
            user.setId(id);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            dbUser.child(id).setValue(user);

            Toast.makeText(this, "Register Success!", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    public void btnLogin(View view) {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }
}