package com.android.atpic;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.atpic.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPass;
    private TextView tvName;
    private Button btnEdit;

    public static  final String EXTRA_USERS = "extra_users";


    private Users users;
    private String usersId;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser authUsers = mAuth.getCurrentUser();

        tvName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        btnEdit = findViewById(R.id.btn_editprofile);
        btnEdit.setOnClickListener(this);

        users = getIntent().getParcelableExtra(EXTRA_USERS);

        database = FirebaseDatabase.getInstance().getReference("users").child(authUsers.getUid());
        database.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                tvName.setText(users.getName());
                edtEmail.setText(users.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));

        if(users != null){
            usersId = users.getId();
        }else{
            users = new Users();
        }

        if(users != null){
            edtEmail.setText(users.getEmail());
            edtPass.setText(users.getPassword());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_editprofile){
            updateUsers();
        }
    }

    private void updateUsers() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();

        boolean isEmptyFields = false;

        if(TextUtils.isEmpty(pass)){
            isEmptyFields = true;
            edtPass.setError("Field ini tidak boleh kosong");
        }

        if(! isEmptyFields){

            users.setEmail(email);
            users.setPassword(pass);

            database.child(users.getId()).setValue(users);
            finish();
        }
    }
}