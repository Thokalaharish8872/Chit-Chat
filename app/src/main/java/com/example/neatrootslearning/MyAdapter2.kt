package com.example.neatrootslearning

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapter2(var context: Activity, private var arrayList2 : ArrayList<startuppagedata>):
        RecyclerView.Adapter<MyAdapter2.phoneDetailsViewHolder>() {

        private lateinit var myListener:onItemClickListner

        interface onItemClickListner{
            fun onItemClick(position: Int)
        }

        interface onItemLongClick {
            fun onItemLongClick(position: Int)
        }

        var onItemLongClickListener: ((Int) -> Boolean)? = null

        fun setItemOnClickListner(listener:onItemClickListner){
            myListener=listener
        }

        fun setItemOnLongClickListener(listener2: (Int) -> Boolean) {
            onItemLongClickListener = listener2
        }

        inner class phoneDetailsViewHolder(
            itemView: View,
            listener: onItemClickListner,
            onItemLongClickListener: ((Int) -> Boolean)?
        ): RecyclerView.ViewHolder(itemView){

            var personName : TextView = itemView.findViewById(R.id.name2)
//            var personPhone : TextView = itemView.findViewById(R.id.Phone1)
            var personpic =itemView.findViewById<CircleImageView>(R.id.profile_image2)

            init {
                itemView.setOnClickListener{
                    listener.onItemClick(adapterPosition)
                }

                itemView.setOnLongClickListener {
                    onItemLongClickListener?.invoke(adapterPosition)
                    true
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): phoneDetailsViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.each_profile_online,parent,false)
            return phoneDetailsViewHolder(view,myListener,onItemLongClickListener)
        }

        override fun onBindViewHolder(holder: phoneDetailsViewHolder, position: Int) {
            var sharedPref =context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
            var phoneNumber = sharedPref.getString("PhoneNumber", null)
            holder.personName.text=arrayList2[position].name
            var database= FirebaseDatabase.getInstance().getReference("users")
            database.child(phoneNumber.toString()).child("Added Contacts").child(arrayList2[position].phone).child("nickname").get().addOnSuccessListener {
                if(it.exists()){
                    if(it.value.toString()!="") {
//                    Toast.makeText(context, it.value.toString(), Toast.LENGTH_SHORT).show()

                        holder.personName.gravity= Gravity.CENTER_HORIZONTAL
                        holder.personName.text=it.value.toString()
                    }else{
                        holder.personName.text = arrayList2[position].name

                    }
                }
                else{


                    holder.personName.text = arrayList2[position].name

                }
            }
//            holder.personPhone.text=arrayList[position].phone
//        holder.personpic.setImageResource(arrayList[position].pic)
            if (!arrayList2[position].photo.isNullOrEmpty()) {

                Picasso.get().invalidate(arrayList2[position].photo)
                Picasso.get().load(arrayList2[position].photo).error(R.drawable.no_profile_pic).into(holder.personpic)
            } else {
                // Load a default image or leave the ImageView empty
                holder.personpic.setImageResource(R.drawable.no_profile_pic)
            }




        }

        override fun getItemCount(): Int {
            return arrayList2.size
        }
    }
