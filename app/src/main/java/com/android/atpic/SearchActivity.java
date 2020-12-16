package com.android.atpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.atpic.adapter.ProductAdapter;
import com.android.atpic.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnKeyListener {

    DatabaseReference db;
    private RecyclerView listData;
    private AutoCompleteTextView txtSearch;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseDatabase.getInstance().getReference("product");
        listData = findViewById(R.id.rv_search);
        txtSearch = findViewById(R.id.txtSearch);
        txtSearch.setOnKeyListener(this);

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(event);
    }

    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> names= new ArrayList<>();
        if(snapshot.exists()){
            for(DataSnapshot ds:snapshot.getChildren()){
                String name = ds.child("name").getValue(String.class);
                names.add(name);
            }
            ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);
            txtSearch.setAdapter(adapter);
            txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = txtSearch.getText().toString();
                    searchProduct(name);
                }
            });
        } else {
            Toast.makeText(SearchActivity.this, "No product found", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchProduct(String name) {
        Query query = db.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<Product> listProducts = new ArrayList<>();

                    for(DataSnapshot ds:snapshot.getChildren()){
                        Product product = ds.getValue(Product.class);
                        listProducts.add(product);
                        Log.d("SearchActivity", "product found");
                    }

                    adapter = new ProductAdapter(SearchActivity.this);
                    adapter.setProductList(listProducts);
                    listData.setAdapter(adapter);
                    Log.d("SearchActivity", "binding adapter");
                } else {
                    Toast.makeText(SearchActivity.this, "No product found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            searchProduct(txtSearch.getText().toString());
            return true;
        }
        return false;
    }
}