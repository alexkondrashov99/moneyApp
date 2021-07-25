package com.example.groshikiapp

import android.content.Context
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
import com.example.groshikiapp.Model.*
import com.example.groshikiapp.Model.Transaction
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.StringBuilder

private data class Usaer(val email:String, val password:String)
var counter = 0
class MainActivity : AppCompatActivity(),DatabaseDisplayer {

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

        val profile = Profile("fadsgghdsgtr","Profile1")
        val user = User("sadasdsad","em@google.com")
        val pocket = Pocket("sadwrtg3q","pocket1",0.0,"Руб",true,"",profile.id)
        val category = Category("sadasdasd","categoryName",true,profile.id)
        val dbHelper = DBHelper(this)
//        dbHelper.createPocket(pocket)
//        dbHelper.createTransaction(Transaction("dsavcaew",-324.0,System.currentTimeMillis(),category.id,"Я добавил тразакцию",pocket.id))
//        dbHelper.createTransaction(Transaction("dsaasvcaew",-524.0,System.currentTimeMillis(),category.id,"Я добавил тразакцию",pocket.id))
//        dbHelper.createTransaction(Transaction("dsavsadcaew",1324.0,System.currentTimeMillis(),category.id,"Я добавил тразакцию",pocket.id))
//        dbHelper.createTransaction(Transaction("dsazxcvcaew",3324.0,System.currentTimeMillis(),category.id,"Я добавил тразакцию",pocket.id))
//        dbHelper.createTransaction(Transaction("dsavsdcaew",-1524.0,System.currentTimeMillis(),category.id,"Я добавил тразакцию",pocket.id))
//        dbHelper.createTransaction(Transaction("dsa2asvcaew",-324.0,System.currentTimeMillis(),category.id,"Я добавил тразакцию",pocket.id))

        val new_pocket = dbHelper.getDataByCondition<Pocket>(TABLE_POCKET,"WHERE $POCKET_ID = '${pocket.id}'", {Pocket(it)})
        new_pocket?.let{
            val sb = StringBuilder().apply{
               append("new_pocket.balance ${new_pocket.balance}${new_pocket.currency}")
            }
            twUserData.setText(sb.toString())
        }


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

    override fun onDatabaseError(error: String) {
        TODO("Not yet implemented")
    }

    override fun getParentContext(): Context {
        return this
    }


}