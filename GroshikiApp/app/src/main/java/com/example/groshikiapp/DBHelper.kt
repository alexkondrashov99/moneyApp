package com.example.groshikiapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
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
//const val PROFILE_PW_Hash= "profile_pw_hash"

//_________PSEUDONYM___________________________//
const val TABLE_PSEUDONYM = "table_pseudonym"
const val PSEUDONYM_ID = "pseudonym_id"
const val PSEUDONYM_NAME = "pseudonym_name"

//_________Category___________________________//
const val TABLE_CATEGORY = "table_category"
const val CATEGORY_ID = "category_id"
const val CATEGORY_NAME = "category_name"
const val CATEGORY_ICON = "category_icon"
const val CATEGORY_IS_Spend = "category_is_spend"
const val CATEGORY_IS_HIDE = "category_is_hide"

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

//_________TRANSACTION___________________________//
const val TABLE_TRANSACTION = "table_transaction"
const val TRANSACTION_ID = "transaction_id"
const val TRANSACTION_SUM = "transaction_sum"
const val TRANSACTION_TIME_UTC = "transaction_time_utc"
const val TRANSACTION_DESCRIPTION = "transaction_description"
//const val TRANSACTION_IS_SPEND = "transaction_is_spend"

//---------------------------------------------//
const val CREATE_TABLE_USER =
    "CREATE TABLE "+TABLE_USER+" ( "+
            USER_ID +" TEXT primary key ," +
            USER_EMAIL +" TEXT )"

const val CREATE_TABLE_PSEUDONYM =
    "CREATE TABLE "+TABLE_PSEUDONYM+" ( "+
            PSEUDONYM_ID +" TEXT primary key ," +
            PROFILE_ID +" TEXT," +
            USER_ID +" TEXT," +
            PSEUDONYM_NAME +" TEXT  )"


const val CREATE_TABLE_PROFILE =
    "CREATE TABLE "+TABLE_PROFILE+" ( "+
            PROFILE_ID +" TEXT primary key ," +
            PROFILE_NAME +" TEXT  )"
//PROFILE_PW_Hash +" INT )"

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




interface DatabaseDisplayer {
    fun onDatabaseError(error:String)
    fun getParentContext():Context
}




class DBHelper(activity: DatabaseDisplayer): SQLiteOpenHelper(activity.getParentContext(), DATABASE_NAME, null, DATABASE_VERSION) {

    val databaseDisplayer:DatabaseDisplayer = activity
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PROFILE)
        db.execSQL(CREATE_TABLE_USER)
        db.execSQL(CREATE_TABLE_PSEUDONYM)
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

//    fun load (){
//        getDataList<Pseudonym>("asda",{ c -> Pseudonym(c) })
//    }


    //Generic GET functions for all Database objects
    public fun <T> getDataList(table:String, construcor:(c:Cursor) -> T, condition: String = ""):ArrayList<T>{
        val db = writableDatabase
        val dataList = ArrayList<T>()
        val c: Cursor = db.rawQuery("SELECT * FROM $table $condition",null)
        if(c.moveToFirst())
            do {
                val data = construcor(c)
            } while (c.moveToNext())
        db.close()
        return dataList
    }
    public fun <T> getDataList(table:String, construcor:(c:Cursor) -> T, condition: String = "", db:SQLiteDatabase):ArrayList<T>{
        val dataList = ArrayList<T>()
        val c: Cursor = db.rawQuery("SELECT * FROM $table $condition",null)
        if(c.moveToFirst())
            do {
                val data = construcor(c)
            } while (c.moveToNext())
        return dataList
    }
    public fun <T> getDataByCondition(table:String, condition: String, construcor:(c:Cursor) -> T):T?{
        val db = writableDatabase
        var data:T? = null
        val c: Cursor = db.rawQuery("SELECT * FROM $table $condition",null)
        if(c.moveToFirst())
                data = construcor(c)

        db.close()
        return data
    }
    public fun <T> getDataByCondition(table:String, condition: String, construcor:(c:Cursor) -> T, db: SQLiteDatabase):T?{
        var data:T? = null
        val c: Cursor = db.rawQuery("SELECT * FROM $table $condition",null)
        if(c.moveToFirst())
            data = construcor(c)

        return data
    }


    //_________USER___________________________//
    public fun createUser(user: User){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(USER_ID,user.id)
            put(USER_EMAIL,user.email)
        }
        db.insert(TABLE_USER, null, values);

        db.close()
    }

    //_________PROFILE___________________________//
    public fun createProfile(profile: Profile){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(PROFILE_ID,profile.id)
            put(PROFILE_NAME,profile.name)
        }
        db.insert(TABLE_PROFILE, null, values);
        db.close()
    }
    public fun removeProfile(){

    }

    //_________PSEUDONYM___________________________//
    public fun createPseudonym(pseudonym: Pseudonym){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(PSEUDONYM_ID,pseudonym.id)
            put(PROFILE_ID,pseudonym.profileId)
            put(USER_ID,pseudonym.userId)
            put(PSEUDONYM_NAME,pseudonym.name)

        }
        db.insert(TABLE_PSEUDONYM, null, values);
        db.close()
    }
    public fun createPseudonym(pseudonym: Pseudonym,db: SQLiteDatabase){
        val values = ContentValues().apply {
            put(PSEUDONYM_ID,pseudonym.id)
            put(PROFILE_ID,pseudonym.profileId)
            put(USER_ID,pseudonym.userId)
            put(PSEUDONYM_NAME,pseudonym.name)

        }
        db.insert(TABLE_PSEUDONYM, null, values);
    }
    public fun updatePseudonym(pseudonym: Pseudonym){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(PSEUDONYM_NAME,pseudonym.name)
        }
        db.update(TABLE_PSEUDONYM, values, PSEUDONYM_ID + " = ?", arrayOf(pseudonym.id));
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
        val transactionList = getDataList(TABLE_TRANSACTION,{Transaction(it)},"WHERE $CATEGORY_ID = '$categoryId'")

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
        val pocket = getDataByCondition<Pocket>(TABLE_POCKET, "WHERE $POCKET_ID = '${transaction.pocketId}'",{Pocket(it)},db)

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
        val pocket = getDataByCondition<Pocket>(TABLE_POCKET, "WHERE $POCKET_ID = '${transaction.pocketId}'",{Pocket(it)},db)

        pocket?.let{
            pocket.balance -= transaction.sum
            updatePocket(pocket,db)
        }

        db.execSQL("DELETE FROM $TABLE_TRANSACTION WHERE $TRANSACTION_ID = '${transaction.id}'")
        db.close()
    }
    public fun deleteTransaction(transaction:Transaction,db: SQLiteDatabase){
        val pocket = getDataByCondition<Pocket>(TABLE_POCKET, "WHERE $POCKET_ID = '${transaction.pocketId}'",{Pocket(it)},db)

        pocket?.let{
            pocket.balance -= transaction.sum
            updatePocket(pocket,db)
        }

        db.execSQL("DELETE FROM $TABLE_TRANSACTION WHERE $TRANSACTION_ID = '${transaction.id}'")
    }
    public fun updateTransaction(transaction:Transaction){
        val db = writableDatabase
        val pocket = getDataByCondition<Pocket>(TABLE_POCKET, "WHERE $POCKET_ID = '${transaction.pocketId}'",{Pocket(it)},db)
        val oldTransaction:Transaction? = null//get
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
        val transactionList = getDataList(TABLE_TRANSACTION,{Transaction(it)},"WHERE $POCKET_ID = '${pocket.id}'")

        transactionList.forEach(){
            deleteTransaction(it,db)
        }

        db.execSQL("DELETE FROM $TABLE_POCKET WHERE $POCKET_ID = '${pocket.id}'")
        db.close()
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
    public fun bindUserProfile(bindingId:String, userId:String, profileId:String){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(USER_PROFILE_ID,bindingId)
            put(USER_ID,userId)
            put(USER_EMAIL,profileId)
        }
        db.insert(TABLE_USER_PROFILE, null, values);

        db.close()
    }
    public fun unbindUserProfile(userId:String, profileId:String){
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_PSEUDONYM WHERE $USER_ID = '$userId' AND $PROFILE_ID = '$profileId'")
        db.close()
    }

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