package com.android.atpic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.atpic.R
import com.android.atpic.adapter.CartAdapter
import com.android.atpic.adapter.ProductAdapter
import com.android.atpic.model.Product
import com.android.atpic.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_cart.*

internal class CartFragment : Fragment() {

    val cartList = ArrayList<Product>()
    val productList = ArrayList<Product>()
    lateinit var users : Users
    val mAuth = FirebaseAuth.getInstance()
    val authUsers = mAuth.currentUser
    val database = FirebaseDatabase.getInstance().getReference()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        val btnCheckout = view.findViewById<Button>(R.id.btn_checkout)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_cart)
        val imageCart = view.findViewById<ImageView>(R.id.iv_undrawCart)

        val adapter = CartAdapter(activity)

        database.child("users").child(authUsers!!.uid).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        users = snapshot.getValue(Users::class.java)!!

                        if (users != null && users.cart != "") {
                            val strs = users.cart.split(",").toTypedArray()

                            cartList.clear()
                            for (data in strs) {
                                database.child("product").child(data).addValueEventListener(
                                        object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                val product = snapshot.getValue(Product::class.java)

                                                if (product != null){
                                                    cartList.add(product)
                                                }

                                                recyclerView.visibility = View.VISIBLE
                                                btnCheckout.visibility = View.VISIBLE
                                                imageCart.visibility = View.INVISIBLE

                                                adapter.productList = cartList
                                                recyclerView.adapter = adapter
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }
                                        }
                                )

                            }

                        } else {
                            Log.d("CartFragment", "Cart is empty" )
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }

        )

        btnCheckout.setOnClickListener{
            val intent = Intent(activity , PaymentActivity::class.java)
            intent.putParcelableArrayListExtra(PaymentActivity.EXTRA_PRODUCT, cartList)
            intent.putExtra(PaymentActivity.EXTRA_USERS, users)
            startActivity(intent)
        }

        return view
    }
}