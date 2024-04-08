package com.example.neatrootslearning

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapter(var context: Activity, private var arrayList : ArrayList<startuppagedata>):
        RecyclerView.Adapter<MyAdapter.phoneDetailsViewHolder>() {
    class phoneDetailsViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

        var personName : TextView = itemView.findViewById(R.id.name1)
        var personPhone : TextView = itemView.findViewById(R.id.Phone1)
        var personpic =itemView.findViewById<CircleImageView>(R.id.profile_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): phoneDetailsViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.eachprofile,parent,false)
        return phoneDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: phoneDetailsViewHolder, position: Int) {
        holder.personName.text=arrayList[position].name
        holder.personPhone.text=arrayList[position].phone
//        holder.personpic.setImageResource(arrayList[position].pic)
        Glide.with(context)
            .load(arrayList[position].picUri) // Use the stored URI here
            .into(holder.personpic)






    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}