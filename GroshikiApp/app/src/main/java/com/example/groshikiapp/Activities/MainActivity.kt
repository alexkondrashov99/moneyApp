package com.example.groshikiapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groshikiapp.Firebase.FDApi
import com.example.groshikiapp.Model.*
import com.example.groshikiapp.Model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.StringBuilder

private val FB_USERS = "Users"
private val FB_EMAIL = "email"
private val FB_ID = "id"
private val FB_NAME = "name"
private val FB_PSEUDONYM = "pseudonym"

private data class Usaer(val email:String, val password:String)
var counter = 0
class MainActivity : AppCompatActivity() {

    val twUserData:TextView by lazy { findViewById(R.id.twUserData) }
    val dbUserRef = FirebaseDatabase.getInstance().getReference(FB_USERS)
    val valueEventListener:ValueEventListener by lazy {
        object: ValueEventListener {
            override fun onDataChange(var1: DataSnapshot) {
                val id = var1.child(FB_ID).getValue().toString()
                val email = var1.child(FB_EMAIL).getValue().toString()

                val sb = StringBuilder().apply{
                    append("id:").append(id).append("\n")
                    append("email:").append(email).append("\n")
                }
                twUserData.setText(sb.toString())
            }
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val key = user?.uid
        key?.let{
            //val child = dbUserRef.child(key)
            //    .addValueEventListener(valueEventListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dbHelper = DBHelper(this)


        //val profile = Profile("","MyTestProfile2","simplepassword".hashCode())
//        FbDbApi.createProfile(profile,
//            {
//                twUserData.setText(it)
//                val pocket = Pocket("","MyPocket",0.0,"Руб",true,"",it)
//                FbDbApi.createPocket(pocket,
//                    {
//
//                    },
//                    {
//                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
//                    })
//            },
//            {
//                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
//            })
        val profile = Profile("-MfU22V0U3555CJx6B_V", "MyTestProfile2",-1953097139)
        val categoryProfit = Category("testCategory_Profit",false,profile.id)
        val categorySpend = Category("testCategory_Spend",true,profile.id)

        val user = User("OxpYCxCPhAaIQIYhAKeJy9ogdx42","poprigun4ik99@gmail.com")
        //add user to profile
        //add 2 categories to profile
        //add transactions to profile

        val tr = Transaction(250.0,System.currentTimeMillis(),"caId","descr","pocketId")

    }


    fun onLogoutClick(view: View) {

        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val key = user?.uid
        key?.let{
            dbUserRef.child(key).removeEventListener(valueEventListener)
        }

        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }


}