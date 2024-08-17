package com.example.neatrootslearning

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyAdapter(var context: Activity, private var arrayList : ArrayList<startuppagedata>):
    RecyclerView.Adapter<MyAdapter.phoneDetailsViewHolder>() {

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
    fun updateList(newList: ArrayList<startuppagedata>) {
        arrayList = newList
        notifyDataSetChanged()
    }

    inner class phoneDetailsViewHolder(
        itemView: View,
        listener: onItemClickListner,
        onItemLongClickListener: ((Int) -> Boolean)?
    ): RecyclerView.ViewHolder(itemView){

        var personName : TextView = itemView.findViewById(R.id.name1)
//        var personPhone : TextView = itemView.findViewById(R.id.Phone1)
        var personpic =itemView.findViewById<CircleImageView>(R.id.profile_image)
        var lastMessage : TextView = itemView.findViewById(R.id.Last_Message)
        var time:TextView=itemView.findViewById(R.id.time)

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
        var view = LayoutInflater.from(parent.context).inflate(R.layout.eachprofile,parent,false)
        return phoneDetailsViewHolder(view,myListener,onItemLongClickListener)
    }


    override fun onBindViewHolder(holder: phoneDetailsViewHolder, position: Int) {

        var sharedPref =context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        var phoneNumber = sharedPref.getString("PhoneNumber", null)
        holder.personName.text=arrayList[position].name
        var database= FirebaseDatabase.getInstance().getReference("users")
        database.child(phoneNumber.toString()).child("Added Contacts").child(arrayList[position].phone).child("nickname").get().addOnSuccessListener {
//            val roomId = if (phoneNumber!! < arrayList[position].phone) phoneNumber +"_"+ arrayList[position].phone else arrayList[position].phone +"_"+ phoneNumber
//           var database2 = FirebaseDatabase.getInstance()
//            val Last_Message: DatabaseReference =
//                database2.getReference("Last Messages /${roomId}/Last Message text")
//            var lastMessagetext=database2.getReference("Last Messages/${roomId}").get().addOnSuccessListener {
//                var lastMessagetext2=it.child("Last Message text").value.toString()
//                holder.lastMessage.text=lastMessagetext2.toString()
//            }
//
//                Last_Message.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
////                        var lastMessageId = snapshot.value
////                        var Last_Message2= FirebaseDatabase.getInstance().getReference("conversations/${roomId}/Last Message/Last Message Id").get().addOnSuccessListener {
////                            var lastMessageId=it.value.toString()
//                        Toast.makeText(context, snapshot.value.toString(), Toast.LENGTH_SHORT).show()
////                            var Last_Message2= FirebaseDatabase.getInstance().getReference("Last Messages/${roomId}").get().addOnSuccessListener {
////                            var Last_Message3=it.child("Last Message text").value.toString()
////                            if(Last_Message3.isNotEmpty()){
////                                holder.lastMessage.text=Last_Message3
////
////                            }}
////                        }
//
//            }

//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//                })

            if(it.exists()){
                if(it.value.toString()!="") {
//                    Toast.makeText(context, it.value.toString(), Toast.LENGTH_SHORT).show()

                    holder.personName.text=it.value.toString()
                }else{
                    holder.personName.text = arrayList[position].name

                }
            }
            else{


                    holder.personName.text = arrayList[position].name

            }

        }
        holder.lastMessage.text=(sharedPref.getString("Last Message for ${arrayList[position].phone}", "hey!"))

//        holder.personPhone.text=arrayList[position].phone
//        holder.personpic.setImageResource(arrayList[position].pic)
        if (!arrayList[position].photo.isNullOrEmpty()) {
            Picasso.get().invalidate(arrayList[position].photo)
            Picasso.get().load(arrayList[position].photo).error(R.drawable.no_profile_pic).into(holder.personpic)
        } else {
            // Load a default image or leave the ImageView empty
            holder.personpic.setImageResource(R.drawable.no_profile_pic)
        }


        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val time = sdf.format(Date(arrayList[position].timestamp))
        holder.time?.text = time

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}