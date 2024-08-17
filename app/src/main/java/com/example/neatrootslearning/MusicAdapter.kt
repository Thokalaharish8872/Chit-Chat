package com.example.neatrootslearning

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MusicAdapter(var context: Activity, var dataList: List<Item>) :
    RecyclerView.Adapter<MusicAdapter.MyViewHolder>() {

    private lateinit var myListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setItemOnClickListener(listener: onItemClickListener) {
        myListener = listener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var coverImage: CircleImageView = itemView.findViewById(R.id.Cover_Image)
        var title: TextView = itemView.findViewById(R.id.Tittle)
        var btnPlay: Button = itemView.findViewById(R.id.btn_Play)
        var id: TextView = itemView.findViewById(R.id.id)

        init {
            itemView.setOnClickListener {
                myListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_music, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return minOf(dataList.size, 15)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sharedPref = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val phone = sharedPref.getString("Phone_of_recieve", null)
        val phoneNumber = sharedPref.getString("PhoneNumber", null)
        val mediaPlayer = MediaPlayer.create(context, dataList[position].data.uri.toUri())

        holder.title.text = dataList[position].data.name
        holder.id.text = dataList[position].data.id

        var isPlaying = false

        holder.btnPlay.setOnClickListener {
            if (isPlaying) {
                mediaPlayer.pause()
                holder.btnPlay.text = "Play"
                val database = FirebaseDatabase.getInstance().getReference("users")
                database.child(phoneNumber.toString()).child("Added Contacts")
                    .child(phone.toString()).child("isPlaying").setValue(false)
            } else {
                mediaPlayer.start()
                holder.btnPlay.text = "Pause"
                val database = FirebaseDatabase.getInstance().getReference("users")
                database.child(phoneNumber.toString()).child("Added Contacts")
                    .child(phone.toString()).child("isPlaying").setValue(true)
                database.child(phoneNumber.toString()).child("Added Contacts")
//                    .child(phone.toString()).child("Song Id").setValue(dataList[position].data.id)
            }
            isPlaying = !isPlaying
        }

        val coverUrl = dataList[position].data.albumOfTrack.coverArt.sources.firstOrNull()?.url
        Picasso.get().load(coverUrl).error(R.drawable.no_profile_pic).into(holder.coverImage)
    }}
