package com.example.groshikiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.groshikiapp.Firebase.FDApi
import com.example.groshikiapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class LoginActivity : AppCompatActivity() {


    val dbHelper:DBHelper by lazy{ DBHelper(this)}
    val btLogin: Button by lazy { findViewById(R.id.btLogin) }
    val twEmail: TextView by lazy { findViewById(R.id.etEmail) }
    val twPassword: TextView by lazy { findViewById(R.id.etPassword) }
    val progressBar: ProgressBar by lazy { findViewById(R.id.pbRegisterBar) }
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(mAuth.currentUser != null){
            val intent = Intent(this,ChooseProfileActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)

        }

    }
    fun onRegisterClick(view: View) {
        val intent = Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }

    inline fun validateInput(){
        val email = twEmail.text.toString().trim()
        val password = twPassword.text.toString().trim()



        if(email.isEmpty()){
            twEmail.error = "Введите email!"
            twEmail.requestFocus()
            return
        }
        if(password.isEmpty()){
            twPassword.error = "Введите пароль!"
            twPassword.requestFocus()
            return
        }
    }
    fun onLoginClick(view: View) {
        val email = twEmail.text.toString().trim().lowercase(Locale.getDefault())
        val password = twPassword.text.toString()

        if(email.isEmpty()){
            twEmail.setError("Введите email!")
            twEmail.requestFocus()
            return
        }
        if(password.isEmpty()){
            twPassword.setError("Введите пароль!")
            twPassword.requestFocus()
            return
        }
        progressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                FDApi.getUserId(this,email,
                {
                    val user = User(it,email)

                    //DELETE THIS????????
                    //dbHelper.createUser(user)

                    SetUserPreferences(this,user)
                    val intent = Intent(this,ChooseProfileActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(intent)

                },
                {
                    Toast.makeText(applicationContext,it,Toast.LENGTH_LONG).show()
                })

            }
            else{
                Toast.makeText(applicationContext,task.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }

    }
}