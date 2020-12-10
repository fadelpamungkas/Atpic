package com.android.atpic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.atpic.adapter.ProductAdapter
import com.android.atpic.model.Product
import com.google.firebase.database.*

internal class HomeFragment : Fragment() {
    val adapter = ProductAdapter(this.context)
    val productList = ArrayList<Product>()
    val database = FirebaseDatabase.getInstance().getReference("product")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_bestSeller)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()

                for (data in snapshot.children) {
                    val product = data.getValue(Product::class.java)
                    if (product != null) {
                        productList.add(product)
                        println(product)
                    }
                    adapter.productList = productList
                    recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return view
    }
}