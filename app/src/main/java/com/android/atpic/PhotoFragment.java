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

public class PhotoFragment extends Fragment {

    private static final String TAG = "PhotoFragment";
    DatabaseReference database;
    ArrayList<Product> photoList;
    Product photo;
    ProductAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        recyclerView = view.findViewById(R.id.rv_template);

        database = FirebaseDatabase.getInstance().getReference();
        photoList = new ArrayList<>();

        database.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photoList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    photo = dataSnapshot.getValue(Product.class);
                    if (photo != null && photo.getId_category().equals("Photo")) {
                        photoList.add(photo);
                        Log.d(TAG, "onDataChange: photo added photoList");
                    }
                    adapter.setProductList(photoList);
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