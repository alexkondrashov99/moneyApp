package com.example.groshikiapp.Model



import android.database.Cursor
import com.example.groshikiapp.*
import com.google.firebase.database.DataSnapshot

interface ToMap{
    fun toMap():MutableMap<String,Any>
}

data class User(var id:String = "", val email:String):ToMap{
    constructor(email:String):this("",email)
    companion object{

    }
    override fun toMap(): MutableMap<String, Any> {
        return hashMapOf<String, Any>(
            USER_ID to id,
            USER_EMAIL to email
        )
    }
}
data class Profile(var id:String = "", val name:String, val pwHash:Int):ToMap{
    constructor(name:String,pwHash:Int):this("",name,pwHash)
    companion object{

    }
    override fun toMap(): MutableMap<String, Any> {
        return hashMapOf<String, Any>(
            PROFILE_ID to id,
            PROFILE_NAME to name,
            PROFILE_PW_HASH to pwHash,
        )
    }
}
data class Transaction(var id:String = "", val sum:Double, val timeUTC:Long, val categoryId:String, val description:String, val pocketId:String):ToMap{
    constructor(sum:Double, timeUTC:Long, categoryId:String, description:String, pocketId:String):this("",sum,timeUTC,categoryId,description,pocketId)
    companion object{

    }
    override fun toMap(): MutableMap<String, Any> {
        return hashMapOf<String, Any>(
            TRANSACTION_ID to id,
            TRANSACTION_SUM to sum,
            TRANSACTION_TIME_UTC to timeUTC,
            CATEGORY_ID to categoryId,
            TRANSACTION_DESCRIPTION to description,
            POCKET_ID to pocketId,
        )
    }
}
data class Category(var id:String = "", val name:String, val spend:Boolean, val profileId: String,val hide:Boolean = false, val icon:Int = 0,val immortal:Boolean = false):ToMap{
    constructor( name:String, spend:Boolean, profileId: String,hide:Boolean = false,
                 icon:Int = 0):this("",name,spend,profileId,hide,icon)
    companion object{

    }
    override fun toMap(): MutableMap<String, Any> {
        return hashMapOf<String, Any>(
            CATEGORY_ID to id,
            CATEGORY_NAME to name,
            CATEGORY_IS_Spend to spend,
            PROFILE_ID to profileId,
            CATEGORY_IS_HIDE to hide,
            CATEGORY_ICON to icon,
            CATEGORY_IS_IMMORTAL to immortal

            )
    }

}
data class Pocket(var id:String = "", val name: String, var balance:Double, val currency: String, val joint:Boolean, val userId:String, val profileId:String, val icon:Int = 0,val hide:Boolean = false):ToMap{
    constructor(name: String, balance:Double, currency: String, joint:Boolean, userId:String, profileId:String,
                icon:Int = 0,hide:Boolean = false):this("",name,balance,currency,joint,userId,profileId,icon,hide)

    override fun toMap(): MutableMap<String, Any> {
        return hashMapOf<String, Any>(
            POCKET_ID to id,
            POCKET_NAME to name,
            POCKET_BALANCE to balance,
            POCKET_CURRENCY to currency,
            POCKET_IS_JOINT to joint,
            USER_ID to userId,
            PROFILE_ID to profileId,
            POCKET_ICON to icon,
            POCKET_IS_HIDE to hide,
            )
    }
    companion object{

    }
}
data class UserProfile(var id:String = "", val userId: String, val profileId: String, var userName:String, var hide:Boolean = false):ToMap{
    constructor(userId:String,profileId: String,userName: String,hide:Boolean = false):this("",userId,profileId,userName,hide)
    override fun toMap(): MutableMap<String, Any> {
        return hashMapOf<String, Any>(
            USER_PROFILE_ID to id,
            USER_ID to userId,
            PROFILE_ID to profileId,
            USER_PROFILE_NAME to userName,
            USER_PROFILE_IS_HIDE to hide
        )
    }
    companion object {

    }
}

//withDataSnapshot
fun UserProfile.Companion.withDataSnapshot(d:DataSnapshot):UserProfile{
    return UserProfile(
        d.child(USER_PROFILE_ID).value.toString(),
        d.child(USER_ID).value.toString(),
        d.child(PROFILE_ID).value.toString(),
        d.child(USER_PROFILE_NAME).value.toString(),
        d.child(USER_PROFILE_IS_HIDE).value.toString().toBoolean()
    )
}
fun Pocket.Companion.withDataSnapshot(d:DataSnapshot):Pocket{
    return Pocket(
        d.child(com.example.groshikiapp.POCKET_ID).value.toString(),
        d.child(com.example.groshikiapp.POCKET_NAME).value.toString(),
        d.child(com.example.groshikiapp.POCKET_BALANCE).value.toString().toDouble(),
        d.child(com.example.groshikiapp.POCKET_CURRENCY).value.toString(),
        d.child(com.example.groshikiapp.POCKET_IS_JOINT).value.toString().toBoolean(),
        d.child(com.example.groshikiapp.USER_ID).value.toString(),
        d.child(com.example.groshikiapp.PROFILE_ID).value.toString(),
        d.child(com.example.groshikiapp.POCKET_ICON).value.toString().toInt(),
        d.child(com.example.groshikiapp.POCKET_IS_HIDE).value.toString().toBoolean()
    )
}
fun Transaction.Companion.withDataSnapshot(d:DataSnapshot):Transaction{
    return Transaction(
        d.child(com.example.groshikiapp.TRANSACTION_ID).value.toString(),
        d.child(com.example.groshikiapp.TRANSACTION_SUM).value.toString().toDouble(),
        d.child(com.example.groshikiapp.TRANSACTION_TIME_UTC).value.toString().toLong(),
        d.child(com.example.groshikiapp.CATEGORY_ID).value.toString(),
        d.child(com.example.groshikiapp.TRANSACTION_DESCRIPTION).value.toString(),
        d.child(com.example.groshikiapp.POCKET_ID).value.toString(),
    )
}
fun Category.Companion.withDataSnapshot(d:DataSnapshot):Category{
    return Category(
        d.child(CATEGORY_ID).value.toString(),
        d.child(CATEGORY_NAME).value.toString(),
        d.child(CATEGORY_IS_Spend).value.toString().toBoolean(),
        d.child(PROFILE_ID).value.toString(),
        d.child(CATEGORY_IS_HIDE).value.toString().toBoolean(),
        d.child(CATEGORY_ICON).value.toString().toInt(),
        d.child(CATEGORY_IS_IMMORTAL).value.toString().toBoolean()
    )
}
fun Profile.Companion.withDataSnapshot(d:DataSnapshot):Profile{
    return Profile(
        d.child(PROFILE_ID).value.toString(),
        d.child(PROFILE_NAME).value.toString(),
        d.child(PROFILE_PW_HASH).value.toString().toInt()
    )
}
fun User.Companion.withDataSnapshot(d:DataSnapshot):User{
    return User(
        d.child(USER_ID).value.toString(),
        d.child(USER_EMAIL).value.toString(),
    )
}

//withCursor
fun UserProfile.Companion.withCursor(c:Cursor):UserProfile{
    return UserProfile(
        c.getString(c.getColumnIndex(USER_PROFILE_ID)),
        c.getString(c.getColumnIndex(USER_ID)),
        c.getString(c.getColumnIndex(PROFILE_ID)),
        c.getString(c.getColumnIndex(USER_PROFILE_NAME)),
        c.getInt(c.getColumnIndex(USER_PROFILE_IS_HIDE)) == 1,
    )
}
fun Pocket.Companion.withCursor(c:Cursor):Pocket{
    return Pocket(
        c.getString(c.getColumnIndex(POCKET_ID)),
        c.getString(c.getColumnIndex(POCKET_NAME)),
        c.getDouble(c.getColumnIndex(POCKET_BALANCE)),
        c.getString(c.getColumnIndex(POCKET_CURRENCY)),
        c.getInt(c.getColumnIndex(POCKET_IS_JOINT)) == 1,
        c.getString(c.getColumnIndex(USER_ID)),
        c.getString(c.getColumnIndex(PROFILE_ID)),
        c.getInt(c.getColumnIndex(POCKET_ICON)),
        c.getInt(c.getColumnIndex(POCKET_IS_HIDE)) == 1,
    )
}
fun Transaction.Companion.withCursor(c:Cursor):Transaction{
    return Transaction(
        c.getString(c.getColumnIndex(TRANSACTION_ID)),
        c.getDouble(c.getColumnIndex(TRANSACTION_SUM)),
        c.getLong(c.getColumnIndex(TRANSACTION_TIME_UTC)),
        c.getString(c.getColumnIndex(CATEGORY_ID)),
        c.getString(c.getColumnIndex(TRANSACTION_DESCRIPTION)),
        c.getString(c.getColumnIndex(POCKET_ID)),
    )
}
fun Category.Companion.withCursor(c:Cursor):Category{
    return Category(
        c.getString(c.getColumnIndex(CATEGORY_ID)),
        c.getString(c.getColumnIndex(CATEGORY_NAME)),
        c.getInt(c.getColumnIndex(CATEGORY_IS_Spend)) == 1,
        c.getString(c.getColumnIndex(PROFILE_ID)),
        c.getInt(c.getColumnIndex(CATEGORY_IS_HIDE)) == 1,
        c.getInt(c.getColumnIndex(CATEGORY_ICON)),
        c.getInt(c.getColumnIndex(CATEGORY_IS_IMMORTAL)) == 1,
    )
}
fun Profile.Companion.withCursor(c:Cursor):Profile{
    return Profile(
        c.getString(c.getColumnIndex(PROFILE_ID)),
        c.getString(c.getColumnIndex(PROFILE_NAME)),
        c.getInt(c.getColumnIndex(PROFILE_PW_HASH))
    )
}
fun User.Companion.withCursor(c:Cursor):User{
    return User(
        c.getString(c.getColumnIndex(USER_ID)),
        c.getString(c.getColumnIndex(USER_EMAIL)),
    )
}