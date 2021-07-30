package com.example.groshikiapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.groshikiapp.Model.*
import java.util.*
import kotlin.collections.ArrayList




//_________USER___________________________//
const val TABLE_USER = "table_user"
const val USER_ID = "user_id"
const val USER_EMAIL = "user_email"

//_________PROFILE___________________________//
const val TABLE_PROFILE = "table_profile"
const val PROFILE_ID = "profile_id"
const val PROFILE_NAME = "profile_name"
const val PROFILE_PW_HASH= "profile_pw_hash"

//_________Category___________________________//
const val TABLE_CATEGORY = "table_category"
const val CATEGORY_ID = "category_id"
const val CATEGORY_NAME = "category_name"
const val CATEGORY_ICON = "category_icon"
const val CATEGORY_IS_Spend = "category_is_spend"
const val CATEGORY_IS_HIDE = "category_is_hide"
const val CATEGORY_IS_IMMORTAL = "category_is_immortal"

//_________POCKET___________________________//
const val TABLE_POCKET = "table_pocket"
const val POCKET_ID = "pocket_id"
const val POCKET_NAME = "pocket_name"
const val POCKET_BALANCE = "pocket_balance"
const val POCKET_CURRENCY = "pocket_currency"
const val POCKET_IS_JOINT = "pocket_is_joint"
const val POCKET_IS_HIDE = "pocket_is_hide"
const val POCKET_ICON = "pocket_icon"

//_________USER-PROFILE___________________________//
const val TABLE_USER_PROFILE = "table_user_profile"
const val USER_PROFILE_ID = "user_profile_id"
const val USER_PROFILE_NAME = "user_profile_name"
const val USER_PROFILE_IS_HIDE = "user_profile_is_hide"

//_________TRANSACTION___________________________//
const val TABLE_TRANSACTION = "table_transaction"
const val TRANSACTION_ID = "transaction_id"
const val TRANSACTION_SUM = "transaction_sum"
const val TRANSACTION_TIME_UTC = "transaction_time_utc"
const val TRANSACTION_DESCRIPTION = "transaction_description"
//const val TRANSACTION_IS_SPEND = "transaction_is_spend"

const val CREATE_TABLE_USER_PROFILE =
    "CREATE TABLE "+ TABLE_USER_PROFILE+" ( "+
            USER_PROFILE_ID +" TEXT primary key ," +
            PROFILE_ID +" TEXT," +
            USER_ID +" TEXT," +
            USER_PROFILE_IS_HIDE +" INT," +
            USER_PROFILE_NAME +" TEXT  )"


const val CREATE_TABLE_PROFILE =
    "CREATE TABLE "+TABLE_PROFILE+" ( "+
            PROFILE_ID +" TEXT primary key ," +
            PROFILE_NAME +" TEXT,"+
            PROFILE_PW_HASH +" INT )"

const val CREATE_TABLE_CATEGORY =
    "CREATE TABLE "+ TABLE_CATEGORY+" ( "+
            CATEGORY_ID +" TEXT primary key ," +
            PROFILE_ID +" TEXT," +
            CATEGORY_NAME +" TEXT  ," +
            CATEGORY_ICON +" INT ," +
            CATEGORY_IS_HIDE +" INT ," +
            CATEGORY_IS_Spend +" INT )"

const val CREATE_TABLE_POCKET =
    "CREATE TABLE "+ TABLE_POCKET+" ( "+
            POCKET_ID +" TEXT primary key ," +
            PROFILE_ID +" TEXT," +
            USER_ID +" TEXT," +
            POCKET_NAME +" TEXT  ," +
            POCKET_BALANCE +" REAL  ," +
            POCKET_CURRENCY +" TEXT  ," +
            POCKET_ICON +" INT  ," +
            POCKET_IS_HIDE + " INT ,"+
            POCKET_IS_JOINT +" INT )"

const val CREATE_TABLE_TRANSACTION =
    "CREATE TABLE "+ TABLE_TRANSACTION+" ( "+
            TRANSACTION_ID +" TEXT primary key ," +
            POCKET_ID +" TEXT," +
            TRANSACTION_SUM +" REAL  ," +
            TRANSACTION_TIME_UTC +" TEXT ," +
            CATEGORY_ID +" TEXT ," +
            TRANSACTION_DESCRIPTION +" TEXT )"
// TRANSACTION_IS_SPEND +" INT

//const val CLEAR_TABLE_TRANSACTION =  "DELETE FROM " + TABLE_TRANSACTION
const val LOG_TAG = "DATABASE"







class DBHelper(context:Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PROFILE)
        //db.execSQL(CREATE_TABLE_USER)
        db.execSQL(CREATE_TABLE_USER_PROFILE)
        db.execSQL(CREATE_TABLE_CATEGORY)
        db.execSQL(CREATE_TABLE_POCKET)
        db.execSQL(CREATE_TABLE_TRANSACTION)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Groshiki.db"
    }

    private fun clearTable(tableName: String){
        val db = writableDatabase
        db.execSQL("DELETE FROM " + tableName)
        db.close()
    }




    //Generic GET functions for all Database objects
    public fun <T> getDataList(table:String, constructor:(c:Cursor) -> T, condition: String = ""):ArrayList<T>{
        val db = writableDatabase
        val dataList = ArrayList<T>()
        val c: Cursor = db.rawQuery("SELECT * FROM $table $condition",null)
        if(c.moveToFirst())
            do {
                dataList.add(constructor(c))
            } while (c.moveToNext())
        db.close()
        return dataList
    }
    public fun <T> getDataList(table:String, constructor:(c:Cursor) -> T, condition: String = "", db:SQLiteDatabase):ArrayList<T>{
        val dataList = ArrayList<T>()
        val c: Cursor = db.rawQuery("SELECT * FROM $table $condition",null)
        if(c.moveToFirst())
            do {
                dataList.add(constructor(c))
            } while (c.moveToNext())
        return dataList
    }
    public fun <T> getDataByCondition(table:String, condition: String, constructor:(c:Cursor) -> T):T?{
        val db = writableDatabase
        var data:T? = null
        val c: Cursor = db.rawQuery("SELECT * FROM $table $condition",null)
        if(c.moveToFirst())
            data = constructor(c)

        db.close()
        return data
    }
    public fun <T> getDataByCondition(table:String, condition: String, constructor:(c:Cursor) -> T, db: SQLiteDatabase):T?{
        var data:T? = null
        val c: Cursor = db.rawQuery("SELECT * FROM $table $condition",null)
        if(c.moveToFirst())
            data = constructor(c)

        return data
    }


    //_________USER___________________________//
//    public fun createUser(user: User){
//        val db = writableDatabase
//        val values = ContentValues().apply {
//            put(USER_ID,user.id)
//            put(USER_EMAIL,user.email)
//        }
//        db.insert(TABLE_USER, null, values);
//
//        db.close()
//    }

    //_________PROFILE___________________________//
    public fun createProfile(profile: Profile){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(PROFILE_ID,profile.id)
            put(PROFILE_NAME,profile.name)
            put(PROFILE_PW_HASH,profile.pwHash)
        }
        val c = db.rawQuery("SELECT * FROM $TABLE_PROFILE WHERE $PROFILE_ID = '${profile.id}'",null)
        if(c.count == 0)
            db.insert(TABLE_PROFILE, null, values);
        db.close()
    }
    public fun deleteProfile(profileId:String){
        val db = writableDatabase
        val pocketList = getDataList(TABLE_POCKET,{Pocket.withCursor(it)},"WHERE $PROFILE_ID = '$profileId'",db)
        pocketList.forEach{
            deletePocket(it,db)
        }
        db.execSQL("DELETE FROM $TABLE_CATEGORY WHERE $PROFILE_ID = '$profileId'")
        db.execSQL("DELETE FROM $TABLE_USER_PROFILE WHERE $PROFILE_ID = '$profileId'")
        db.execSQL("DELETE FROM $TABLE_PROFILE WHERE $PROFILE_ID = '${profileId}'")
        db.close()
    }


    //_________Category___________________________//
    public fun createCategory(category:Category){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(CATEGORY_ID,category.id)
            put(PROFILE_ID,category.profileId)
            put(CATEGORY_NAME,category.name)
            put(CATEGORY_IS_HIDE, if(category.hide) 1 else 0)
            put(CATEGORY_IS_Spend,if(category.spend) 1 else 0)
            put(CATEGORY_ICON,category.icon)
        }
        db.insert(TABLE_CATEGORY, null, values);
        db.close()
    }
    public fun updateCategory(category:Category){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(CATEGORY_NAME,category.name)
            put(CATEGORY_IS_HIDE, if(category.hide) 1 else 0)
            put(CATEGORY_IS_Spend,if(category.spend) 1 else 0)
            put(CATEGORY_ICON,category.icon)
        }
        db.update(TABLE_CATEGORY, values, CATEGORY_ID + " = ?", arrayOf(category.id));
        db.close()
    }
    public fun deleteCategory(categoryId: String){
        val db = writableDatabase
        val transactionList = getDataList(TABLE_TRANSACTION,{Transaction.withCursor(it)},"WHERE $CATEGORY_ID = '$categoryId'")

        transactionList.forEach(){
            deleteTransaction(it,db)
        }

        db.execSQL("DELETE FROM $TABLE_CATEGORY WHERE $CATEGORY_ID = '${categoryId}'")
        //db.delete(TABLE_CATEGORY, CATEGORY_ID + " = ?", arrayOf(categoryId));
        db.close()
    }
    public fun hideCategory(categoryId: String){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(CATEGORY_IS_HIDE, 1)
        }
        db.update(TABLE_CATEGORY, values, CATEGORY_ID + " = ?", arrayOf(categoryId));
        db.close()
    }

    //_________TRANSACTION___________________________//
    public fun createTransaction(transaction:Transaction){
        val db = writableDatabase
        val pocket = getDataByCondition<Pocket>(TABLE_POCKET, "WHERE $POCKET_ID = '${transaction.pocketId}'",{Pocket.withCursor(it)},db)

        pocket?.let{
            pocket.balance += transaction.sum
            updatePocket(pocket,db)

            val values = ContentValues().apply {
                put(TRANSACTION_ID,transaction.id)
                put(POCKET_ID,transaction.pocketId)
                put(CATEGORY_ID,transaction.categoryId)
                put(TRANSACTION_SUM,transaction.sum)
                put(TRANSACTION_TIME_UTC,transaction.timeUTC)
                put(TRANSACTION_DESCRIPTION,transaction.description)
            }

            db.insert(TABLE_TRANSACTION, null, values);
        }

        db.close()
    }
    public fun deleteTransaction(transaction:Transaction){
        val db = writableDatabase
        val pocket = getDataByCondition<Pocket>(TABLE_POCKET, "WHERE $POCKET_ID = '${transaction.pocketId}'",{Pocket.withCursor(it)},db)

        pocket?.let{
            pocket.balance -= transaction.sum
            updatePocket(pocket,db)
        }

        db.execSQL("DELETE FROM $TABLE_TRANSACTION WHERE $TRANSACTION_ID = '${transaction.id}'")
        db.close()
    }
    public fun deleteTransaction(transaction:Transaction,db: SQLiteDatabase){
        val pocket = getDataByCondition<Pocket>(TABLE_POCKET, "WHERE $POCKET_ID = '${transaction.pocketId}'",{Pocket.withCursor(it)},db)

        pocket?.let{
            pocket.balance -= transaction.sum
            updatePocket(pocket,db)
        }

        db.execSQL("DELETE FROM $TABLE_TRANSACTION WHERE $TRANSACTION_ID = '${transaction.id}'")
    }
    public fun updateTransaction(transaction:Transaction){
        val db = writableDatabase
        val pocket = getDataByCondition<Pocket>(TABLE_POCKET, "WHERE $POCKET_ID = '${transaction.pocketId}'",{Pocket.withCursor(it)},db)
        val oldTransaction:Transaction? = getDataByCondition<Transaction>(TABLE_TRANSACTION, "WHERE $TRANSACTION_ID = '${transaction.id}'",{Transaction.withCursor(it)},db)
        oldTransaction?.let {
            if(oldTransaction.sum != transaction.sum)
                pocket?.let{
                    pocket.balance += (transaction.sum - oldTransaction.sum)
                    updatePocket(pocket,db)
                }

            val values = ContentValues().apply {
                put(TRANSACTION_SUM,transaction.sum)
                put(TRANSACTION_TIME_UTC, transaction.timeUTC)
                put(TRANSACTION_DESCRIPTION,transaction.description)
            }
            db.update(TABLE_TRANSACTION, values, "$TRANSACTION_ID = ?", arrayOf(transaction.id));
        }


        db.execSQL("DELETE FROM $TABLE_TRANSACTION WHERE $TRANSACTION_ID = '${transaction.id}'")
        db.close()
    }

    //_________POCKET___________________________//
    public fun createPocket(pocket: Pocket){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(POCKET_ID,pocket.id)
            put(PROFILE_ID,pocket.profileId)
            put(USER_ID,pocket.userId)
            put(POCKET_NAME,pocket.name)
            put(POCKET_BALANCE,pocket.balance)
            put(POCKET_CURRENCY,pocket.currency)
            put(POCKET_ICON,pocket.icon)
            put(POCKET_IS_JOINT,if(pocket.joint) 1 else 0)
            put(POCKET_IS_HIDE,if(pocket.hide) 1 else 0)
        }
        db.insert(TABLE_POCKET, null, values);
        db.close()
    }
    public fun deletePocket(pocket: Pocket){
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_TRANSACTION WHERE $POCKET_ID = '${pocket.id}'")
        db.execSQL("DELETE FROM $TABLE_POCKET WHERE $POCKET_ID = '${pocket.id}'")
        db.close()
    }
    public fun deletePocket(pocket: Pocket, db:SQLiteDatabase){
        db.execSQL("DELETE FROM $TABLE_TRANSACTION WHERE $POCKET_ID = '${pocket.id}'")
        db.execSQL("DELETE FROM $TABLE_POCKET WHERE $POCKET_ID = '${pocket.id}'")
    }
    public fun updatePocket(pocket: Pocket){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(POCKET_NAME,pocket.name)
            put(POCKET_CURRENCY,pocket.currency)
            put(POCKET_ICON,pocket.icon)
            put(POCKET_IS_HIDE,if(pocket.hide) 1 else 0)
        }
        db.update(TABLE_POCKET, values, POCKET_ID + " = ?", arrayOf(pocket.id));
        db.close()
    }
    public fun hidePocket(pocket: Pocket){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(POCKET_IS_HIDE,1)
        }
        db.update(TABLE_POCKET, values, POCKET_ID + " = ?", arrayOf(pocket.id));
        db.close()
    }

    private fun updatePocket(pocket: Pocket, db:SQLiteDatabase){
        val values = ContentValues().apply {
            put(POCKET_NAME,pocket.name)
            put(POCKET_BALANCE,pocket.balance)
            put(POCKET_CURRENCY,pocket.currency)
            put(POCKET_ICON,pocket.icon)
            put(POCKET_IS_HIDE,if(pocket.hide) 1 else 0)
        }
        db.update(TABLE_POCKET, values, POCKET_ID + " = ?", arrayOf(pocket.id));
    }



    //_________USER-PROFILE___________________________//
    public fun getUserProfile(userId:String,profileId:String):UserProfile?{
       val userProfile =  getDataByCondition(TABLE_USER_PROFILE,"WHERE $USER_ID = '$userId' AND $PROFILE_ID = '$profileId'",{UserProfile.withCursor(it)})
       return userProfile
    }

    public fun updateUserProfile(userProfile: UserProfile){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(USER_PROFILE_NAME,userProfile.userName)
            put(USER_PROFILE_IS_HIDE,if(userProfile.hide) 1 else 0)
        }
        db.update(TABLE_USER_PROFILE, values, USER_PROFILE_ID + " = ?", arrayOf(userProfile.id));
        db.close()
    }
    public fun bindUserProfile(userProfile:UserProfile){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(USER_PROFILE_ID,userProfile.id)
            put(USER_ID,userProfile.userId)
            put(PROFILE_ID,userProfile.profileId)
            put(USER_PROFILE_NAME,userProfile.userName)
            put(USER_PROFILE_IS_HIDE,if(userProfile.hide) 1 else 0)
        }
        val c = db.rawQuery("SELECT * FROM $TABLE_USER_PROFILE WHERE $USER_PROFILE_ID = '${userProfile.id}'",null)
        if(c.count == 0)
            db.insert(TABLE_USER_PROFILE, null, values);
        else
            updateUserProfile(userProfile)
        db.close()
    }
    public fun unbindUserProfile(userId:String, profileId:String){
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_USER_PROFILE WHERE $USER_ID = '$userId' AND $PROFILE_ID = '$profileId'")
        db.close()
    }
    //TO DO


}

//TRASH
/*
    public fun getPocketById(pocketID:String):Pocket?{
        val db = writableDatabase
        var pocket:Pocket? = null
        val c: Cursor = db.rawQuery("SELECT * FROM $TABLE_POCKET",null)
        if(c.moveToFirst())
        {
            val id = c.getString(c.getColumnIndex(POCKET_ID))
            val name = c.getString(c.getColumnIndex(POCKET_NAME))
            val balance = c.getDouble(c.getColumnIndex(POCKET_BALANCE))
            val userId = c.getString(c.getColumnIndex(USER_ID))
            val profileId = c.getString(c.getColumnIndex(PROFILE_ID))
            val currency = c.getString(c.getColumnIndex(POCKET_CURRENCY))
            val icon = c.getInt(c.getColumnIndex(POCKET_ICON))
            val hide = c.getInt(c.getColumnIndex(POCKET_IS_HIDE)) == 1
            val joint = c.getInt(c.getColumnIndex(POCKET_IS_JOINT)) == 1
            pocket = Pocket(id,name,balance,currency,joint,userId,profileId,icon,hide)
        }
        db.close()
        return pocket
    }
    private fun getPocketById(pocketID:String,db:SQLiteDatabase):Pocket?{
        //val db = writableDatabase
        var pocket:Pocket? = null
        val c: Cursor = db.rawQuery("SELECT * FROM $TABLE_POCKET",null)
        if(c.moveToFirst())
        {
            val id = c.getString(c.getColumnIndex(POCKET_ID))
            val name = c.getString(c.getColumnIndex(POCKET_NAME))
            val balance = c.getDouble(c.getColumnIndex(POCKET_BALANCE))
            val userId = c.getString(c.getColumnIndex(USER_ID))
            val profileId = c.getString(c.getColumnIndex(PROFILE_ID))
            val currency = c.getString(c.getColumnIndex(POCKET_CURRENCY))
            val icon = c.getInt(c.getColumnIndex(POCKET_ICON))
            val hide = c.getInt(c.getColumnIndex(POCKET_IS_HIDE)) == 1
            val joint = c.getInt(c.getColumnIndex(POCKET_IS_JOINT)) == 1
            pocket = Pocket(id,name,balance,currency,joint,userId,profileId,icon,hide)
        }
        return pocket
    }
    public fun getTransactionById(transactionId:String):Transaction?{
        val db = writableDatabase
        var transaction:Transaction? = null
        val c: Cursor = db.rawQuery("SELECT * FROM $TABLE_TRANSACTION WHERE $TRANSACTION_ID = '$transactionId'",null)
        if(c.moveToFirst())
            transaction = Transaction(c)

        db.close()
        return transaction

    }
    public fun getTransactionList(condition:String = ""):ArrayList<Transaction>{
        val db = writableDatabase
        val transactionList = ArrayList<Transaction>()
        val c: Cursor = db.rawQuery("SELECT * FROM $TABLE_TRANSACTION " + condition,null)
        if(c.moveToFirst())
        do {
            transactionList.add(Transaction(c))
        } while (c.moveToNext())

        db.close()
        return transactionList

    }
    public fun getPseudonym(userId:String,profileId:String):Pseudonym?{
        val db = writableDatabase
        var pseudonym:Pseudonym? = null
        val c: Cursor = db.rawQuery("SELECT * FROM $TABLE_PSEUDONYM WHERE $PROFILE_ID = '$profileId' AND $USER_ID = '$userId'",null)
        if(c.moveToFirst())
        {
            pseudonym = Pseudonym(c)
        }

        db.close()
        return pseudonym
    }
    public fun getPseudonymList(condition:String = ""):ArrayList<Pseudonym>{
        val db = writableDatabase
        val pseudonymList = ArrayList<Pseudonym>()
        val c: Cursor = db.rawQuery("SELECT * FROM $TABLE_PSEUDONYM " + condition,null)
        if(c.moveToFirst())
            do {
                pseudonymList.add(Pseudonym(c))
            } while (c.moveToNext())

        db.close()
        return pseudonymList
    }

 */