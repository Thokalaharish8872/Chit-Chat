package com.example.neatrootslearning

// Assuming same package structure

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AlbumRVAdapter(
    private val albumRVModalArrayList: ArrayList<AlbumRVModal>,
    private val context: Context
) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumRVModal = albumRVModalArrayList[position]
        Picasso.get().load(albumRVModal.imageUrl).into(holder.albumIV)
        holder.albumNameTV.text = albumRVModal.name
        holder.albumDetailTV.text = albumRVModal.artistName
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AlbumDetailActivity::class.java)
            intent.putExtra("id", albumRVModal.id)
            intent.putExtra("name", albumRVModal.name)
            intent.putExtra("img", albumRVModal.imageUrl)
            intent.putExtra("artist", albumRVModal.artistName)
            intent.putExtra("albumUrl", albumRVModal.external_urls)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = albumRVModalArrayList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumIV: ImageView = itemView.findViewById(R.id.idIVAlbum)
        val albumNameTV: TextView = itemView.findViewById(R.id.idTVAlbumName)
        val albumDetailTV: TextView = itemView.findViewById(R.id.idTVALbumDetails)
    }
}
