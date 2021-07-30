package com.example.groshikiapp.Firebase

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.groshikiapp.*
import com.example.groshikiapp.Model.*
import com.example.groshikiapp.Model.Transaction
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*

const val TAG = "FirebaseTag"
const val PROFILE_DATA ="profile_data"
const val MAIN_DATA = "main_data"


private val fbDb = FirebaseDatabase.getInstance()




object FDApi {

    private fun createData(data: MutableMap<String,Any>, tableName:String, idKey:String, onSuccess:(id:String)->Unit, onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(tableName)
        val id = dbRef.push().key
        if (id == null) {
            Log.w(TAG, "Couldn't get push key")
            return
        }
        data[idKey] = id
        dbRef.child(id)
            .setValue(data).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                    onSuccess(id)
                else
                    onFail(task.exception?.message.toString())
            }
    }
    private fun createInsideProfileData(data: MutableMap<String,Any>, profileId:String, tableName:String, idKey:String, onSuccess:(id:String)->Unit, onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(TABLE_PROFILE).child("$profileId/$MAIN_DATA/$tableName")
        val id = dbRef.push().key
        if (id == null) {
            Log.w(TAG, "Couldn't get push key")
            return
        }
        data[idKey] = id
        dbRef.child(id)
            .setValue(data).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                    onSuccess(id)
                else
                    onFail(task.exception?.message.toString())
            }
    }

    private fun updateInsideProfileData(data: MutableMap<String,Any>, profileId:String, tableName:String, id:String, onSuccess:()->Unit, onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(TABLE_PROFILE).child("$profileId/$MAIN_DATA/$tableName")
        dbRef.child(id)
            .updateChildren(data).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                    onSuccess()
                else
                    onFail(task.exception?.message.toString())
            }
    }
    private fun updateData(data: MutableMap<String,Any>, table:String, id:String, onSuccess:()->Unit, onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(table)
        dbRef.child(id)
            .updateChildren(data).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                    onSuccess()
                else
                    onFail(task.exception?.message.toString())
            }
    }

    private fun deleteInsideProfileData(profileId:String, tableName:String,  id:String, onSuccess:()->Unit, onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(TABLE_PROFILE).child("$profileId/$MAIN_DATA/$tableName")
        dbRef.child(id)
            .removeValue().addOnCompleteListener{ task ->
                if(task.isSuccessful)
                    onSuccess()
                else
                    onFail(task.exception?.message.toString())
            }
    }
    private fun deleteData(tableName:String, id:String, onSuccess:()->Unit, onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(tableName)
        dbRef.child(id)
            .removeValue().addOnCompleteListener{ task ->
                if(task.isSuccessful)
                    onSuccess()
                else
                    onFail(task.exception?.message.toString())
            }
    }

    private fun <T> getFromReferenceDataList(ref:Query, constructor:(d: DataSnapshot) -> T, onSuccess:(data:ArrayList<T>)->Unit, onFail: (message: String) -> Unit){
        ref.addListenerForSingleValueEvent(
            object: ValueEventListener {
                override fun onDataChange(var1: DataSnapshot) {
                    val dataList = ArrayList<T>()
                    val iterator = var1.children.iterator()
                    for (data in iterator) {
                        dataList.add(constructor(data))
                    }
                    onSuccess(dataList)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun createUser(user:User, onSuccess:(t:Task<Void>)->Unit,onFail:(message:String)->Unit){
        fbDb.getReference(TABLE_USER).child(user.id)
            .setValue(user.toMap()).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                    onSuccess(task)
                else
                    onFail(task.exception?.message.toString())
            }
    }
    fun getUserId(ctx:Context,email:String,onSuccess:(id:String)->Unit,onFail:(message:String)->Unit){
        if(!isNetworkAvailable(ctx))
            onFail("No internet connection")
        else
            fbDb.getReference(TABLE_USER).orderByChild(USER_EMAIL).equalTo(email)
                .addListenerForSingleValueEvent(
                    object: ValueEventListener {
                        override fun onDataChange(var1: DataSnapshot) {
                            val data = var1.children.iterator().next()
                            val userId = data.child(USER_ID).value.toString()
                            onSuccess(userId)
                        }
                        override fun onCancelled(p0: DatabaseError) {}
                    })
    }

    fun bindUserToProfile(userProfile:UserProfile, onSuccess:(id:String)->Unit, onFail:(message:String)->Unit){
        createData(userProfile.toMap(),TABLE_USER_PROFILE, USER_PROFILE_ID, onSuccess,onFail)
    }
    fun updateUserProfile(userProfile:UserProfile,onSuccess:()->Unit, onFail:(message:String)->Unit){
        updateData(userProfile.toMap(), TABLE_USER_PROFILE,userProfile.id,onSuccess,onFail)
    }

    fun createProfile(ctx: Context,profile: Profile,userId:String, userName:String,onSuccess:(profileId:String,bindId:String)->Unit,onFail:(message:String)->Unit){
        if(!isNetworkAvailable(ctx))
            onFail("No internet connection")
        else
        fbDb.getReference(TABLE_PROFILE).orderByChild(PROFILE_NAME).equalTo(profile.name)
            .addListenerForSingleValueEvent(
                object: ValueEventListener {
                    override fun onDataChange(var1: DataSnapshot) {
                        if(var1.value == null){
                            //Creteing Profile record
                            createData(profile.toMap(), TABLE_PROFILE, PROFILE_ID,
                                {   profileId ->
                                    val userProfile = UserProfile(userId,profileId,userName)
                                    //Creating user_profile binding record
                                    bindUserToProfile(userProfile,
                                        {   bindId ->
                                            onSuccess(profileId,bindId)
                                        },
                                        onFail)
                                },
                                onFail )

                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {
                    }

                }
            )
    }
    fun loginToProfile(ctx:Context,userId:String, userName:String ,profileName:String,pwHash:Int,onSuccess:(userProfile:UserProfile)->Unit,onFail:(message:String)->Unit){
        
        fun checkUserProfileRelation(profileId:String,onSuccess: (newUser:Boolean) -> Unit, onFail: (message: String) -> Unit){
            fbDb.getReference(TABLE_USER_PROFILE).orderByChild(PROFILE_ID).equalTo(profileId)
                .addListenerForSingleValueEvent(
                object: ValueEventListener {
                    override fun onDataChange(var1: DataSnapshot) {
                        val iterator = var1.children.iterator()
                        for (data in iterator) {
                            val existingUserName = data.child(USER_PROFILE_NAME).value.toString()
                            val existingUserId = data.child(USER_ID).value.toString()
                            if(existingUserName == userName){
                                onFail("This user name already exists")
                                return
                            }
                            if(existingUserId == userId){
                                //onFail("You already have this profile")
                                onSuccess(false)
                                return
                            }
                        }
                        onSuccess(true)
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
        
        if(!isNetworkAvailable(ctx))
            onFail("No internet connection")
        else
        fbDb.getReference(TABLE_PROFILE).orderByChild(PROFILE_NAME).equalTo(profileName)
            .addListenerForSingleValueEvent(
                object: ValueEventListener {
                    override fun onDataChange(var1: DataSnapshot) {
                        if(var1.value == null)//Wrong profile name
                            onFail("Wrong profile name")
                        else { //Correct profile name
                            val data = var1.children.iterator().next()
                            val profileId = data.child(PROFILE_ID).value.toString()
                            val pwTrueHash = Integer.valueOf(data.child(PROFILE_PW_HASH).value.toString())
                            if(pwHash == pwTrueHash){ // Correct password
                                checkUserProfileRelation(profileId,
                                    {newUser->
                                        if(newUser){
                                        //Creating user_profile binding record
                                        val userProfile = UserProfile(userId,profileId,userName)
                                        bindUserToProfile(userProfile,
                                            {
                                                onSuccess(UserProfile(it,userId,profileId,userName))
                                            },onFail)
                                        }
                                        //Find out existing user_profile_id
                                        else{
                                            getAllUserProfilesByUser(userId,{ userProfileList ->
                                                userProfileList.forEach{
                                                    if(it.profileId == profileId){
                                                        it.userName = userName
                                                        it.hide = false
                                                        updateData(it.toMap(), TABLE_USER_PROFILE,it.id,{
                                                            onSuccess(it)
                                                        },onFail)
                                                    }
                                                }
                                            },onFail)
                                        }
                                            //onSuccess(it,profileId)
                                    },
                                    onFail)
                            }
                            else{ //Incorrect password
                                onFail("Wrong password!")
                            }
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {
                    }

                }
            )
    }

    //Transactions
    fun createTransaction(transaction: Transaction,profileId:String,onSuccess:(id:String)->Unit,onFail:(message:String)->Unit){
        createInsideProfileData(transaction.toMap(), profileId, TABLE_TRANSACTION, TRANSACTION_ID, onSuccess,onFail)

    }
    fun updateTransaction(transaction: Transaction, profileId:String,onSuccess:()->Unit,onFail:(message:String)->Unit){
            updateInsideProfileData(transaction.toMap(), profileId, TABLE_TRANSACTION, transaction.id, onSuccess,onFail)
    }
    fun deleteTransaction(transaction: Transaction, profileId:String, onSuccess:()->Unit,onFail:(message:String)->Unit){
        deleteInsideProfileData(TABLE_TRANSACTION,profileId,transaction.id, onSuccess,onFail)
    }

    //Get data
    fun getAllProfileTransactions(profileId: String,onSuccess:(ArrayList<Transaction>)->Unit,onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(TABLE_PROFILE).child("${profileId}/$MAIN_DATA/$TABLE_TRANSACTION")
        getFromReferenceDataList(dbRef,{Transaction.withDataSnapshot(it)},onSuccess,onFail)
    }
    fun getAllProfilePockets(profileId: String,onSuccess:(ArrayList<Pocket>)->Unit,onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(TABLE_PROFILE).child("${profileId}/$MAIN_DATA/$TABLE_POCKET")
        getFromReferenceDataList(dbRef,{Pocket.withDataSnapshot(it)},onSuccess,onFail)
    }
    fun getAllProfileCategories(profileId: String,onSuccess:(ArrayList<Category>)->Unit,onFail:(message:String)->Unit){
        val dbRef = fbDb.getReference(TABLE_PROFILE).child("${profileId}/$MAIN_DATA/$TABLE_CATEGORY")
        getFromReferenceDataList(dbRef,{Category.withDataSnapshot(it)},onSuccess,onFail)
    }
    fun getAllUserProfilesByUser(userId: String,onSuccess:(ArrayList<UserProfile>)->Unit,onFail:(message:String)->Unit){
        val dbRef:Query = fbDb.getReference(TABLE_USER_PROFILE).orderByChild(USER_ID).equalTo(userId)
        getFromReferenceDataList(dbRef,{UserProfile.withDataSnapshot(it)},onSuccess,onFail)
    }
    fun getAllUserProfilesByProfile(profileId: String,onSuccess:(ArrayList<UserProfile>)->Unit,onFail:(message:String)->Unit){
        val dbRef:Query = fbDb.getReference(TABLE_USER_PROFILE).orderByChild(PROFILE_NAME).equalTo(profileId)
        getFromReferenceDataList(dbRef,{UserProfile.withDataSnapshot(it)},onSuccess,onFail)
    }


    fun createPocket(pocket: Pocket, onSuccess:(id:String)->Unit, onFail:(message:String)->Unit){
        createInsideProfileData(pocket.toMap(),pocket.profileId, TABLE_POCKET, POCKET_ID,onSuccess,onFail)
    }
    fun updatePocket(pocket: Pocket, onSuccess:()->Unit, onFail:(message:String)->Unit){
        updateInsideProfileData(pocket.toMap(),pocket.profileId, TABLE_POCKET,pocket.id,onSuccess,onFail)
    }

    //TO DO
    fun deletePocket(pocket: Pocket, onSuccess: () -> Unit, onFail:(message:String)->Unit){
        //TO DO
        deleteData(TABLE_POCKET,pocket.id,onSuccess,onFail)
        //Get all trans by this pocket and DELETE
    }
    fun isNetworkAvailable(context:Context):Boolean{
        val connectivity = context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivity.getNetworkCapabilities(connectivity.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivity.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

}