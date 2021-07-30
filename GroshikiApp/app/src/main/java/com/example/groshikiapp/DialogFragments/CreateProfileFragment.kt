package com.example.groshikiapp.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.groshikiapp.R

class CreateProfileFragment(val onCreateClick:(name:String, password:String, username:String)->Unit):AppCompatDialogFragment() {

    var etProfileName:EditText? = null
    var etPassword:EditText? = null
    var etComfrim:EditText? = null
    var etUserName:EditText? = null
    var btDismiss: TextView? = null
    var btLogin:TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(activity)
        val view = activity?.layoutInflater?.inflate(R.layout.dialog_profile_create,null)
        view?.let{
            etPassword = view.findViewById(R.id.etPassword)
            etComfrim = view.findViewById(R.id.etConfrim)
            etProfileName = view.findViewById(R.id.etProfileName)
            etUserName = view.findViewById(R.id.etUserName)
            btDismiss = view.findViewById(R.id.btDismiss)
            btDismiss?.setOnClickListener(){this.dismiss()}

            btLogin = view.findViewById(R.id.btLogin)
            btLogin?.setOnClickListener(){
                if(validateInput())
                onCreateClick(etProfileName?.text.toString(),etPassword?.text.toString(),etUserName?.text.toString())
            }
            dialogBuilder.setView(view)
                .setTitle("Создание профиля")

            return dialogBuilder.create();
        }



        return super.onCreateDialog(savedInstanceState)
    }

    fun validateInput():Boolean{
        val profileName = etProfileName?.text.toString().trim()
        val userName = etUserName?.text.toString().trim()
        val password = etPassword?.text.toString().trim()
        val confrim = etComfrim?.text.toString().trim()


        if(profileName.isEmpty()){
            etProfileName?.error = "Введите название профиля!"
            etProfileName?.requestFocus()
            return false
        }
        if(password.isEmpty()){
            etPassword?.error = "Введите пароль!"
            etPassword?.requestFocus()
            return false
        }
        if(password.length<6){
            etPassword?.error = "Минимальная длина пароля 6 символов!"
            etPassword?.requestFocus()
            return false
        }
        if(confrim.isEmpty()){
            etComfrim?.error = "Введите подтверждение пароля!"
            etComfrim?.requestFocus()
            return false
        }
        if(!confrim.equals(password)){
            etComfrim?.error = "Пароли не совпадают!"
            etComfrim?.requestFocus()
            return false
        }

        if(userName.isEmpty()){
            etUserName?.error = "Введите имя!"
            etUserName?.requestFocus()
            return false
        }
        return true
    }
}