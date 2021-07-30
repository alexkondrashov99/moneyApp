package com.example.groshikiapp

import android.content.Context
import com.example.groshikiapp.Model.Profile
import com.example.groshikiapp.Model.User

//******SHARED PREFERENCES************
const val PREFS_USER = "USER_PREFERENCES"
const val PREFS_PROFILE = "PROFILE_PREFERENCES"
fun SetProfilePreferences(ctx: Context, profile: Profile, userName:String) {
    ClearProfilePreferences(ctx)
    val settings = ctx.getSharedPreferences(PREFS_PROFILE, 0)
    val editor = settings.edit()
    editor.putString(PROFILE_ID, profile.id)
    editor.putInt(PROFILE_PW_HASH, profile.pwHash)
    editor.putString(PROFILE_NAME,profile.name)
    editor.putString(USER_PROFILE_NAME,userName)
    editor.apply()
}
fun getProfilePreferences(ctx: Context): Profile?{
    val settings = ctx.getSharedPreferences(PREFS_PROFILE, 0)
    val profileId = settings.getString(PROFILE_ID, "")
    val profileName = settings.getString(PROFILE_NAME, "")
    val pwHash = settings.getInt(PROFILE_PW_HASH, 0)

    if(profileId != null && profileName != null){
        if(!profileId.isEmpty() && !profileName.isEmpty())
            return Profile(profileId,profileName,pwHash)
        else
            return null
    }
    else
        return null
}
fun getCurrentUserName(ctx: Context): String? {
    val settings = ctx.getSharedPreferences(PREFS_PROFILE, 0)
    val userName = settings.getString(USER_PROFILE_NAME, "")
    return userName
}
fun ClearProfilePreferences(ctx: Context) {
    val settings = ctx.getSharedPreferences(PREFS_PROFILE, 0)
    val editor = settings.edit()
    editor.remove(PROFILE_ID).apply()
    editor.remove(PROFILE_PW_HASH).apply()
    editor.remove(PROFILE_NAME).apply()
    editor.remove(USER_PROFILE_NAME).apply()
}
fun SetUserPreferences(ctx: Context, user: User) {
    ClearUserPreferences(ctx)
    val settings = ctx.getSharedPreferences(PREFS_USER, 0)
    val editor = settings.edit()
    editor.putString(USER_ID, user.id)
    editor.putString(USER_EMAIL,user.email)
    editor.apply()

}
fun GetUserPreferences(ctx: Context): User? {
    val settings = ctx.getSharedPreferences(PREFS_USER, 0)
    val userId = settings.getString(USER_ID, "")
    val email = settings.getString(USER_EMAIL, "")
    if(userId != null && email != null){
        if(!userId.isEmpty() && !email.isEmpty())
            return User(userId,email)
        else
            return null
    }
    else
        return null
}
fun ClearUserPreferences(ctx: Context) {
    val settings = ctx.getSharedPreferences(PREFS_USER, 0)
    settings.edit().remove(USER_ID).apply()
    settings.edit().remove(USER_EMAIL).apply()
}
