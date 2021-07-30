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

val mAuth = FirebaseAuth.getInstance()



class RegisterActivity : AppCompatActivity() {

    val dbHelper:DBHelper by lazy{ DBHelper(this)}
    val btRegister: Button by lazy { findViewById(R.id.btRegister) }
    val twEmail: TextView by lazy { findViewById(R.id.etEmail) }
    val twPassword: TextView by lazy { findViewById(R.id.etPassword) }
    val twConfrim: TextView by lazy { findViewById(R.id.etConfrim) }
    val progressBar: ProgressBar by lazy { findViewById(R.id.pbRegisterBar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }


    fun validateInput():Boolean{
        val email = twEmail.text.toString().trim()
        val password = twPassword.text.toString().trim()
        val confrim = twConfrim.text.toString().trim()

        if(email.isEmpty()){
            twEmail.error = "Введите email!"
            twEmail.requestFocus()
            return false
        }
        if(password.isEmpty()){
            twPassword.error = "Введите пароль!"
            twPassword.requestFocus()
            return false
        }
        if(password.length<6){
            twPassword.error = "Минимальная длина пароля 6 символов!"
            twPassword.requestFocus()
            return false
        }
        if(confrim.isEmpty()){
            twConfrim.error = "Введите подтверждение пароля!"
            twConfrim.requestFocus()
            return false
        }
        if(!confrim.equals(password)){
            twConfrim.error = "Пароли не совпадают!"
            twConfrim.requestFocus()
            return false
        }
        return true
    }
    fun onRegisterClick(view: View) {

        if(!validateInput())
            return

        val email = twEmail.text.toString().trim()
        val password = twPassword.text.toString().trim()

        progressBar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    userId?.let{
                        val user = User(it,email)
                        FDApi.createUser(user,
                            {

                                //dbHelper.createUser(user)
                                SetUserPreferences(this,user)
                                val intent = Intent(this,ChooseProfileActivity::class.java).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                }
                                startActivity(intent)
                            },
                            {
                                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                            })

                    }

                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

}