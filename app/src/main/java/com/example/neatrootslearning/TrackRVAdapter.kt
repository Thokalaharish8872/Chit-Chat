package com.example.neatrootslearning

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class TrackRVAdapter(
    private val trackRVModals: ArrayList<TrackRVModal>,
    private val context: Context
) : RecyclerView.Adapter<TrackRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trackRVModal = trackRVModals[position]
        holder.trackNameTV.text = trackRVModal.trackName
        holder.trackArtistTV.text = trackRVModal.trackArtist
        var sharedPref = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val phoneNumber = sharedPref.getString("PhoneNumber", null)
        val phone = sharedPref.getString("Phone_of_recieve", null)
        holder.itemView.setOnClickListener {

//            var choose = Intent.createChooser(intent, "Open With")
//            startActivity(choose)
            val trackUrl = "https://open.spotify.com/track/${trackRVModal.id}"
            val uri = Uri.parse(trackUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            var choose = Intent.createChooser(intent, "Open With")
            context.startActivity(choose)

            // Handler to bring your app back to the foreground after 2 seconds
            Handler(Looper.getMainLooper()).postDelayed({

                // Intent that relaunches your app's MainActivity
                val returnIntent = Intent(context, chatPage::class.java)
                returnIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                context.startActivity(returnIntent)
            }, 5000)
            sharedPref = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("Song Name",trackRVModal.trackName)
            editor.putBoolean("isplay",true)
            editor.apply()
//            var message= arrayOf("played_by":phoneNumber):Arr
            val roomId = if (phoneNumber!! < phone.toString()) phoneNumber + "_" + phone.toString() else phone.toString() + "_" + phoneNumber

            val database = FirebaseDatabase.getInstance().getReference("Songs").child(roomId)
//            database.child(phone.toString()).child("Added Contacts").child(phoneNumber.toString()).child("Song Name").setValue(trackRVModal.trackName)
            database.child("played_by").setValue(phoneNumber.toString())
            database.child("isPlaying").setValue(true)
            database.child("Song Id").setValue(trackRVModal.id)
            database.child("Song Name").setValue(trackRVModal.trackName)
            var database2=FirebaseDatabase.getInstance().getReference("users")

//            database.child("Song Name").setValue(trackRVModal.trackName)


        }}


    override fun getItemCount(): Int {
        return trackRVModals.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackNameTV: TextView = itemView.findViewById(R.id.idTVTrackName)
        val trackArtistTV: TextView = itemView.findViewById(R.id.idTVTrackArtist)
    }
}
