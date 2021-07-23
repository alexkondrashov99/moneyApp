package com.example.groshikiapp

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groshikiapp.Firebase.FB_EMAIL
import com.example.groshikiapp.Firebase.FB_ID
import com.example.groshikiapp.Firebase.FB_NAME
import com.example.groshikiapp.Firebase.FB_USERS
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.StringBuilder

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
            val child = dbUserRef.child(key)
                .addValueEventListener(valueEventListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



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