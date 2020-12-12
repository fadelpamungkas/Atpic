package com.android.atpic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.atpic.adapter.ProductAdapter;
import com.android.atpic.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TemplateFragment extends Fragment {

    private static final String TAG = "TemplateFragment";
    DatabaseReference database;
    ArrayList<Product> templateList;
    Product template;
    ProductAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_template, container, false);

        recyclerView = view.findViewById(R.id.rv_template);

        database = FirebaseDatabase.getInstance().getReference();
        templateList = new ArrayList<>();

        database.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                templateList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    template = dataSnapshot.getValue(Product.class);
                    if (template != null && template.getId_category().equals("Template")) {
                        templateList.add(template);
                        Log.d(TAG, "onDataChange: template added templateList");
                    }
                    adapter.setProductList(templateList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }
}