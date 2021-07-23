package com.example.groshikiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    val btLogin: Button by lazy { findViewById(R.id.btLogin) }
    val twEmail: TextView by lazy { findViewById(R.id.etEmail) }
    val twPassword: TextView by lazy { findViewById(R.id.etPassword) }
    val progressBar: ProgressBar by lazy { findViewById(R.id.pbRegisterBar) }
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(mAuth.currentUser != null){
            val intent = Intent(this,MainActivity::class.java).apply {
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
        val email = twEmail.text.toString()
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
                val intent = Intent(this,MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext,task.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }

    }
}