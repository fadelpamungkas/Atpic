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

public class FontsFragment extends Fragment {

    private static final String TAG = "FontFragment";
    DatabaseReference database;
    ArrayList<Product> fontList;
    Product font;
    ProductAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fonts, container, false);

        recyclerView = view.findViewById(R.id.rv_font);

        database = FirebaseDatabase.getInstance().getReference();
        fontList = new ArrayList<>();
        adapter = new ProductAdapter(getActivity());

        database.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fontList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    font = dataSnapshot.getValue(Product.class);
                    if (font != null && font.getId_category().equals("Font")) {
                        fontList.add(font);
                        Log.d(TAG, "onDataChange: font added fontList");
                    }
                    adapter.setProductList(fontList);
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