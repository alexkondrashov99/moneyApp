package com.example.groshikiapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.groshikiapp.Model.Profile
import com.example.groshikiapp.R



class ProfilesAdapter(var profilesArray: Array<Profile>,
                      val onItemClick:(profilesList:Array<Profile>,index:Int,view:View)->Unit,
                      val onLongClick:(profilesList:Array<Profile>,index:Int,view:View)->Unit) :
    RecyclerView.Adapter<ProfilesAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val twProfileName: TextView by lazy { view.findViewById(R.id.twProfileName)}
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_profile, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.twProfileName.text = profilesArray[position].name

        viewHolder.itemView.setOnClickListener {
            onItemClick(profilesArray, position, it)
        }
        viewHolder.itemView.setOnLongClickListener{
            onLongClick(profilesArray, position, it)
            true
        }
    }
    override fun getItemCount() = profilesArray.size

}