package com.android.atpic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.atpic.model.Product;
import com.android.atpic.model.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.directselect.DSDefaultPickerBox;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String EXTRA_USERS = "extra_users";

    private EditText edtName, edtPrice, edtDesc;
    private Button btnAdd, btnBack;
    private Spinner spinCategory;
    private Product product;

    String[] array;
    int category;
    String categoryString;
    Users users;

    DatabaseReference database;


    @Nullable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        database = FirebaseDatabase.getInstance().getReference().child("product");
        users = getIntent().getParcelableExtra(EXTRA_USERS);

        edtName = findViewById(R.id.et_name);
        edtPrice = findViewById(R.id.et_price);
        edtDesc = findViewById(R.id.et_desc);
        btnBack = findViewById(R.id.iconBack);
        spinCategory = findViewById(R.id.category);
        spinCategory.setOnItemSelectedListener(this);

        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        array = getResources().getStringArray(R.array.category);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinCategory.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add){
            addProduct();
            finish();
        } else if( v.getId() == R.id.iconBack){
            finish();
        }
    }

    private void addProduct() {
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

        switch (category){
            case 0 :
                categoryString = "Template";
                break;
            case 1 :
                categoryString = "Photo";
                break;
            case 2 :
                categoryString = "Font";
                break;
            case 3 :
                categoryString = "Icon";
                break;
        }

        if (!isEmptyFields){
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date now = new Date();

            product = new Product();
            product.setId(database.push().getKey());
            product.setId_user(users.getId());
            product.setId_category(categoryString);
            product.setName(name);
            product.setPrice(Integer.parseInt(price));
            product.setDesc(desc);
            product.setSold(0);
            product.setUpload_date(formatter.format(now));
//            product.setId_photo();

            database.child(product.getId()).setValue(product);

            Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getSelectedItemPosition();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}