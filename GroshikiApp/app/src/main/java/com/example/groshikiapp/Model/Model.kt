package com.example.groshikiapp.Model



import android.database.Cursor
import com.example.groshikiapp.*
import java.util.*


data class Pseudonym(val id:String, val profileId: String, val userId: String, val name:String){
    constructor(c: Cursor):this(
        c.getString(c.getColumnIndex(PSEUDONYM_ID)),
        c.getString(c.getColumnIndex(USER_ID)),
        c.getString(c.getColumnIndex(PROFILE_ID)),
        c.getString(c.getColumnIndex(PSEUDONYM_NAME))
    ){}
}
data class User(val id:String, val email:String){
    constructor(c: Cursor):this(
        c.getString(c.getColumnIndex(USER_ID)),
        c.getString(c.getColumnIndex(USER_EMAIL)),
    ){}
}
data class Profile(val id:String, val name:String){
    constructor(c: Cursor):this(
        c.getString(c.getColumnIndex(PROFILE_ID)),
        c.getString(c.getColumnIndex(PROFILE_NAME)),
    ){}
}
data class Transaction(val id:String, val sum:Double, val timeUTC:Long, val categoryId:String, val description:String, val pocketId:String){
    constructor(c: Cursor):this(
        c.getString(c.getColumnIndex(TRANSACTION_ID)),
        c.getDouble(c.getColumnIndex(TRANSACTION_SUM)),
        c.getLong(c.getColumnIndex(TRANSACTION_TIME_UTC)),
        c.getString(c.getColumnIndex(CATEGORY_ID)),
        c.getString(c.getColumnIndex(TRANSACTION_DESCRIPTION)),
        c.getString(c.getColumnIndex(POCKET_ID)),
    ){}

}
data class Category(val id:String, val name:String, val spend:Boolean,val profileId: String,val hide:Boolean = false, val icon:Int = 0){
    constructor(c: Cursor):this(
        c.getString(c.getColumnIndex(CATEGORY_ID)),
        c.getString(c.getColumnIndex(CATEGORY_NAME)),
        c.getInt(c.getColumnIndex(CATEGORY_IS_Spend)) == 1,
        c.getString(c.getColumnIndex(PROFILE_ID)),
        c.getInt(c.getColumnIndex(CATEGORY_IS_HIDE)) == 1,
        c.getInt(c.getColumnIndex(CATEGORY_ICON)),
    ){}

}
data class Pocket(val id:String, val name: String, var balance:Double, val currency: String,
                  val joint:Boolean, val userId:String, val profileId:String, val icon:Int = 0,val hide:Boolean = false){
    constructor(c: Cursor):this(
        c.getString(c.getColumnIndex(POCKET_ID)),
        c.getString(c.getColumnIndex(POCKET_NAME)),
        c.getDouble(c.getColumnIndex(POCKET_BALANCE)),
        c.getString(c.getColumnIndex(POCKET_CURRENCY)),
        c.getInt(c.getColumnIndex(POCKET_IS_JOINT)) == 1,
        c.getString(c.getColumnIndex(USER_ID)),
        c.getString(c.getColumnIndex(PROFILE_ID)),
        c.getInt(c.getColumnIndex(POCKET_ICON)),
        c.getInt(c.getColumnIndex(POCKET_IS_HIDE)) == 1,
    ){}

}