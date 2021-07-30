package com.example.groshikiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groshikiapp.Activities.ProfileActivity
import com.example.groshikiapp.Adapters.ProfilesAdapter
import com.example.groshikiapp.DialogFragments.CreateProfileFragment
import com.example.groshikiapp.DialogFragments.LoginProfileFragment
import com.example.groshikiapp.Firebase.FDApi
import com.example.groshikiapp.Model.Profile
import com.example.groshikiapp.Model.UserProfile
import com.example.groshikiapp.Model.withCursor
import com.example.groshikiapp.Model.withDataSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseProfileActivity : AppCompatActivity() {

    private val recyclerView:RecyclerView by lazy { findViewById(R.id.profilesList)}
    private val btLoginProfile: Button by lazy {findViewById(R.id.btLoginProfile)}
    private val btCreateProfile: Button by lazy {findViewById(R.id.btCreateProfile)}
    private val progressBar:ProgressBar by lazy {findViewById(R.id.progressBar)}
    private var dialog:DialogFragment? = null

    private val dbHelper:DBHelper by lazy{ DBHelper(this)}
    private val profileList:ArrayList<Profile>// by lazy { dbHelper.getDataList(TABLE_PROFILE,{Profile(it)})}
    get(){ return dbHelper.getDataList(TABLE_PROFILE,{Profile.withCursor(it)}) }








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_profile)

        val userId = GetUserPreferences(this)?.id
        val list = ArrayList<Profile>().toTypedArray()
        val profilesAdapter = ProfilesAdapter(list,
             { profilesList, index, _ ->
                //SingleToast.show(this,profilesList[index].name,Toast.LENGTH_LONG)
                userId?.let{
                    val profile = profilesList[index]
                    val userName = dbHelper.getUserProfile(userId,profile.id)?.userName ?: "Unknown"
                    openProfileActivity(profile,userId,userName)
                }
              },
              { profilesList, index, view ->
                  userId?.let { showPopupMenu(profilesList[index],userId,view) }
              })

        recyclerView.adapter = profilesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        btLoginProfile.setOnClickListener(){
            dialog = LoginProfileFragment { login, password,userName ->
                userId?.let{
                    if(progressBar.visibility == View.GONE){
                        progressBar.visibility = View.VISIBLE
                        FDApi.loginToProfile(this,userId,userName,login,password.hashCode(),
                            { userProfile ->
                                dialog?.dismiss()
                                val profile = Profile(userProfile.profileId,login,password.hashCode())
                                dbHelper.createProfile(profile)
                                dbHelper.bindUserProfile(userProfile)
                                progressBar.visibility = View.GONE
                                openProfileActivity(profile,userId,userName)
                                //PROGRESS_BAR - off
                            },
                            {
                                SingleToast.show(this,it,Toast.LENGTH_LONG)
                                progressBar.visibility = View.GONE
                                //PROGRESS_BAR - off
                            })
                    }
                }
            }
            dialog?.show(supportFragmentManager, "DialogLoginProfile");
        }
        btCreateProfile.setOnClickListener(){
            dialog = CreateProfileFragment { profileName, password, userName ->
                userId?.let{
                    if(progressBar.visibility == View.GONE){
                        val profile = Profile(profileName,password.hashCode())
                        progressBar.visibility = View.VISIBLE
                        FDApi.createProfile(this,profile,userId,userName,
                            { profileId, bindId ->
                                dialog?.dismiss()
                                profile.id = profileId
                                dbHelper.createProfile(profile)
                                dbHelper.bindUserProfile(UserProfile(bindId,userId,profileId,userName))
                                progressBar.visibility = View.GONE
                                openProfileActivity(profile,userId,userName)
                                //PROGRESS_BAR - off
                            },
                            {
                                SingleToast.show(this,it,Toast.LENGTH_LONG)
                                progressBar.visibility = View.GONE
                                //PROGRESS_BAR - off
                            })
                    }
                }
            }
            dialog?.show(supportFragmentManager, "DialogLoginProfile");
        }
    }

    override fun onResume() {
        super.onResume()
        updateProfileList()
    }

    private fun showPopupMenu(profile:Profile, userId:String, view:View){
        val popupMenu = PopupMenu(this,view)
        popupMenu.menu.add(1,1,1,"Удалить")
        popupMenu.setOnMenuItemClickListener {
        when(it.itemId){
            1 ->
                {
                    val userProfile = dbHelper.getUserProfile(userId,profile.id)
                    userProfile?.let{
                        progressBar.visibility = View.VISIBLE
                        userProfile.hide = true
                        FDApi.updateUserProfile(userProfile,
                        {
                            GlobalScope.launch(Dispatchers.IO){
                                dbHelper.deleteProfile(profile.id)
                                withContext(Dispatchers.Main){
                                    updateProfileList()
                                    progressBar.visibility = View.GONE
                                }
                            }
                        },
                        {
                            SingleToast.show(this,it,Toast.LENGTH_LONG)
                            progressBar.visibility = View.GONE
                        })
                    }

                }
        }

            true
        }
        popupMenu.show()
    }
    private fun openProfileActivity(profile:Profile,userId:String,userName:String){
            SetProfilePreferences(this,profile,userName)
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
    }
    private fun updateProfileList(){
        val list = profileList
        val adapter =  recyclerView.adapter as ProfilesAdapter
        adapter.profilesArray = list.toTypedArray()
        adapter.notifyDataSetChanged()
    }
}