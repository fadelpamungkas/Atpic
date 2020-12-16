package com.android.atpic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.atpic.adapter.ProductAdapter
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
    val myProduct = ArrayList<Product>()
    val database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = ProductAdapter(activity)

        database.child("users").child(authUsers!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users = snapshot.getValue(Users::class.java)!!
                myProduct.clear()

                tv_profileName.text = users.name
                val credit = "Rp" + users.credit.toString()
                tv_credit.text = credit

                database.child("product").addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(data in snapshot.children){
                                    val product = data.getValue(Product::class.java)
                                    if (product != null && product.id_user == users.id){
                                        myProduct.add(product)
                                        Log.d("ProfileFragment", "inserting product to myProduct")
                                    }
                                    if (myProduct.isNotEmpty()){
                                        adapter.productList = myProduct
                                        rv_myProduct.adapter = adapter
                                        rv_myProduct.visibility = View.VISIBLE
                                        iv_undrawAddProduct.visibility = View.INVISIBLE
                                        Log.d("ProfileFragment", "myProduct isnotempty")
                                    }

                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        }

                )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )


    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnAdd = view.findViewById<Button>(R.id.btn_addProduct)
        val btnTopUp = view.findViewById<Button>(R.id.btn_topup)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit)
        val btnLogout = view.findViewById<Button>(R.id.btn_signout)
        val imageAddProduct = view.findViewById<ImageView>(R.id.iv_undrawAddProduct)


        btnLogout.setOnClickListener{
            mAuth.signOut()
            val intent = Intent(activity , LoginActivity::class.java)
            startActivity(intent)
        }

        btnEdit.setOnClickListener{
            val intent = Intent(activity , EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.EXTRA_USERS, users)
            startActivity(intent)
        }

        btnAdd.setOnClickListener {
            val intent = Intent(activity , AddProductActivity::class.java)
            intent.putExtra(AddProductActivity.EXTRA_USERS, users)
            startActivity(intent)
        }

        imageAddProduct.setOnClickListener {
            val intent = Intent(activity , AddProductActivity::class.java)
            intent.putExtra(AddProductActivity.EXTRA_USERS, users)
            startActivity(intent)
        }

        btnTopUp.setOnClickListener {
            val intent = Intent(activity , TopUpActivity::class.java)
            intent.putExtra(TopUpActivity.EXTRA_USERS, users)
            startActivity(intent)
        }

        return view
    }

}