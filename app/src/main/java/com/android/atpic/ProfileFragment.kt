package com.android.atpic

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.atpic.model.Product
import com.android.atpic.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*

internal class ProfileFragment : Fragment() {
    var product : Product = Product()
    var users : Users = Users()
    val mAuth = FirebaseAuth.getInstance()
    val authUsers = mAuth.currentUser
    val database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users = snapshot.getValue(Users::class.java)!!

                tv_profileName.text = users.name
                tv_credit.text = users.credit.toString()

                Toast.makeText(activity?.baseContext, "id: " + users.id + "name:" + users.name + " credit: " + users.credit.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        database.child("users").child(authUsers!!.uid).addListenerForSingleValueEvent(listener)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        val btnAdd = view.findViewById<Button>(R.id.btn_addProduct)
        val btnTopUp = view.findViewById<Button>(R.id.btn_topup)
        val name = view.findViewById<TextView>(R.id.tv_profileName)
        val credit = view.findViewById<TextView>(R.id.tv_credit)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit)

        btnEdit.setOnClickListener{
            activity?.let{
                val intent = Intent (it, EditProfileActivity::class.java)
                it.startActivity(intent)
            }
        }

        btnAdd.setOnClickListener {
            val intent = Intent(activity , AddProductActivity::class.java)
            startActivity(intent)

        }

        btnTopUp.setOnClickListener {
            val intent = Intent(activity , TopUpActivity::class.java)
            startActivity(intent)

        }
        return view
    }

//    val dbProduct = database.child("product")
//    val id = dbProduct.push().key
//    product.id = id
//    product.name = "fadel"
//    product.desc = "desc"
//    product.id_user = authUsers!!.uid
//
//    if (id != null) {
//        dbProduct.child(id).setValue(product)
//    }
}