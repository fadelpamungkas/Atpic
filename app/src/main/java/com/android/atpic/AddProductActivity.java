package com.android.atpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.atpic.model.Product;
import com.android.atpic.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ramotion.directselect.DSDefaultPickerBox;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String EXTRA_USERS = "extra_users";
    public static final int IMAGE_REQUESTS = 1;
    private static final String TAG = "AddProductActivity";

    private EditText edtName, edtPrice, edtDesc;
    private Button btnAdd, btnBack, btnImage;
    private LinearLayout imageLayout;
    private Spinner spinCategory;
    private Product product;

    Uri imageUri;
    ArrayList<Uri> imageUriList;
    String imageEncoded;
    String photoURL;
    List<String> imagesEncodedList;
    String[] array;
    int category;
    String categoryString;
    Users users;
    DatabaseReference database;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        database = FirebaseDatabase.getInstance().getReference().child("product");
        storage = FirebaseStorage.getInstance().getReference().child("image");
        users = getIntent().getParcelableExtra(EXTRA_USERS);

        edtName = findViewById(R.id.et_name);
        edtPrice = findViewById(R.id.et_price);
        edtDesc = findViewById(R.id.et_desc);
        btnBack = findViewById(R.id.iconBack);
        btnImage = findViewById(R.id.btnImage);
        imageLayout = findViewById(R.id.imageLayout);
        spinCategory = findViewById(R.id.category);

        spinCategory.setOnItemSelectedListener(this);

        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnImage.setOnClickListener(this);

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
        } else if(v.getId() == R.id.btnImage){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUESTS);
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

            String productId = database.push().getKey();
            photoURL = "";

            product = new Product();

            product.setId(productId);
            product.setId_user(users.getId());
            product.setId_category(categoryString);
            product.setName(name);
            product.setPrice(Integer.parseInt(price));
            product.setDesc(desc);
            product.setSold(0);
            product.setUpload_date(formatter.format(now));

            for (Uri uri : imageUriList){
                storage.child(productId).child(getFileName(uri)).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(AddProductActivity.this ,new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                photoURL += task.getResult().toString() + ",";
                                product.setPhotoURL(photoURL);
                                database.child(product.getId()).setValue(product);
                                Log.d("AddProductActivity", "getPhotoUrl:" + product.getPhotoURL() + "      photoUrl:" + photoURL);
                            }
                        });
                    }
                });
            }

            Log.d("AddProductActivityBottom", "getPhotoUrl:" + product.getPhotoURL() + "      photoUrl:" + photoURL);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == IMAGE_REQUESTS && resultCode == RESULT_OK && null != data) {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();

                if (data.getData() != null) {         //on Single image selected

                    imageUri = data.getData();
                    Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                } else {                              //on multiple image selected
                    if (data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();
                         imageUriList = new ArrayList<>();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            imageUri = item.getUri();
                            imageUriList.add(imageUri);
                            Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                        Log.v(TAG, "Selected Images" + imageUriList.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}