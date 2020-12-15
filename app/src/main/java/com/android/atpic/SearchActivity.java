package com.android.atpic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.atpic.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    DatabaseReference db;
    private ListView listData;
    private AutoCompleteTextView txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseDatabase.getInstance().getReference("product");
        listData=(ListView)findViewById(R.id.list_view) ;
        txtSearch=(AutoCompleteTextView) findViewById(R.id.txtSearch);

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
                    String name=txtSearch.getText().toString();
                    searchProduct(name);
                }
            });
        } else {
            Toast.makeText(SearchActivity.this, "No product found", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchProduct(String name) {
        Query query=db.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<String> listProducts = new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        Product product=new Product();
                        product.setId_category(ds.child("id_category").getValue(String.class));
                        product.setName(ds.child("name").getValue(String.class));
                        product.setPrice(ds.child("price").getValue(Long.class));

                        listProducts.add(product.getId_category() + "\n" + product.getName() + "\n" + (product.getPrice()));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,listProducts);
                    listData.setAdapter(adapter);
                }else{
                    Toast.makeText(SearchActivity.this, "No product found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}