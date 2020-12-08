package com.android.atpic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.atpic.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_profile.*

internal class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val product = Product();
        val mAuth = FirebaseAuth.getInstance();
        val user = mAuth.currentUser
        val database = FirebaseDatabase.getInstance().getReference("users")

        val btnAdd = view.findViewById<Button>(R.id.btn_addProduct)
        val name = view.findViewById<TextView>(R.id.tv_profileName)
        val credit = view.findViewById<TextView>(R.id.tv_credit)



        btnAdd.setOnClickListener {
            val dbProduct = database.child("product")
            val id = dbProduct.push().key
            product.id = id
            product.name = "fadel"
            product.desc = "desc"

            if (id != null) {
                dbProduct.child(id).setValue(product)
            }

            Toast.makeText(activity, "Added", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}