//package com.example.neatrootslearning
//
//import android.app.Activity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import de.hdodenhof.circleimageview.CircleImageView
//
//class Adapter_for_profilepic (var context: Activity, private var arraylist2 : ArrayList<users3>):
//    RecyclerView.Adapter<Adapter_for_profilepic.phoneDetailsViewHolder>() {
//    class phoneDetailsViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
//
////        var personName : TextView = itemView.findViewById(R.id.name1)
////        var personPhone : TextView = itemView.findViewById(R.id.Phone1)
//        var personpic =itemView.findViewById<CircleImageView>(R.id.profile_image)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): phoneDetailsViewHolder {
//        var view = LayoutInflater.from(parent.context).inflate(R.layout.eachprofile,parent,false)
//        return phoneDetailsViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: phoneDetailsViewHolder, position: Int) {
//
//        holder.personpic.setImageResource(arraylist2[position].pic)
//
//
//    }
//
//    override fun getItemCount(): Int {
//        return arraylist2.size
//    }
//}