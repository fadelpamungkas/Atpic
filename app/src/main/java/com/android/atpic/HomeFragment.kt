package com.android.atpic

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.atpic.adapter.ProductAdapter
import com.android.atpic.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


internal class HomeFragment : Fragment() {
    val productList = ArrayList<Product>()
    val database = FirebaseDatabase.getInstance().getReference("product")

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_bestSeller)
        val btnTemplate = view.findViewById<Button>(R.id.btnTemplate)
        val btnPhoto= view.findViewById<Button>(R.id.btnPhoto)
        val btnFonts = view.findViewById<Button>(R.id.btnFonts)
        val btnIcon = view.findViewById<Button>(R.id.btnIcon)

        val adapter = ProductAdapter(context)

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

        btnTemplate.setOnClickListener {
            val intent = Intent(activity, ExploreActivity::class.java)
            intent.putExtra(ExploreActivity.EXTRA_FRAGMENT, 0)
            startActivity(intent)
        }
        btnPhoto.setOnClickListener {
            val intent = Intent(activity, ExploreActivity::class.java)
            intent.putExtra(ExploreActivity.EXTRA_FRAGMENT, 1)
            startActivity(intent)
        }
        btnFonts.setOnClickListener {
            val intent = Intent(activity, ExploreActivity::class.java)
            intent.putExtra(ExploreActivity.EXTRA_FRAGMENT, 2)
            startActivity(intent)
        }
        btnIcon.setOnClickListener {
            val intent = Intent(activity, ExploreActivity::class.java)
            intent.putExtra(ExploreActivity.EXTRA_FRAGMENT, 3)
            startActivity(intent)
        }

        return view
    }
}