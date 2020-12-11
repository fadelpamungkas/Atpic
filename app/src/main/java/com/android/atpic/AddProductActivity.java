package com.android.atpic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.atpic.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.directselect.DSDefaultPickerBox;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName, edtPrice, edtDesc;
    private Button btnAdd;
    private DSDefaultPickerBox dsCategory;

    private Product product;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = FirebaseDatabase.getInstance().getReference();

        dsCategory = findViewById(R.id.category);
        edtName = findViewById(R.id.et_name);
        edtPrice = findViewById(R.id.et_price);
        edtDesc = findViewById(R.id.et_desc);

        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

        product = new Product();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add){
            addProduct();
        }
    }

    private void addProduct() {
        //String category = dsCategory.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String desc = edtDesc.getText().toString().trim();

        boolean isEmptyFields = false;

        if(TextUtils.isEmpty(name)){
            isEmptyFields = true;
            edtName.setError("Field ini tidak boleh kosong");
        }if(TextUtils.isEmpty(price)){
            isEmptyFields = true;
            edtPrice.setError("Field ini tidak boleh kosong");
        }if(TextUtils.isEmpty(desc)){
            isEmptyFields = true;
            edtDesc.setError("Field ini tidak boleh kosong");
        }
    }
}