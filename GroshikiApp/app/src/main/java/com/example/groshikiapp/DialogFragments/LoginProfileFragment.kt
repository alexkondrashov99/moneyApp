package com.example.groshikiapp.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.groshikiapp.R

class LoginProfileFragment(val onLoginClick:(name:String,password:String,username:String)->Unit):AppCompatDialogFragment() {

    var etProfileName:EditText? = null
    var etPassword:EditText? = null
    var etUserName:EditText? = null
    var btDismiss: TextView? = null
    var btLogin:TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(activity)
        val view = activity?.layoutInflater?.inflate(R.layout.dialog_profile_login,null)
        view?.let{
            etPassword = view.findViewById(R.id.etPassword)
            etProfileName = view.findViewById(R.id.etProfileName)
            etUserName = view.findViewById(R.id.etUserName)
            btDismiss = view.findViewById(R.id.btDismiss)
            btDismiss?.setOnClickListener(){this.dismiss()}

            btLogin = view.findViewById(R.id.btLogin)
            btLogin?.setOnClickListener(){
                if(validateInput())
                onLoginClick(etProfileName?.text.toString(),etPassword?.text.toString(),etUserName?.text.toString())}
            dialogBuilder.setView(view)
                .setTitle("Вход в профиль")

            return dialogBuilder.create();
        }



        return super.onCreateDialog(savedInstanceState)
    }

    fun validateInput():Boolean{

        val userName = etUserName?.text.toString().trim()

        if(userName.isEmpty()){
            etUserName?.error = "Введите имя!"
            etUserName?.requestFocus()
            return false
        }
        return true
    }
}