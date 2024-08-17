package com.example.neatrootslearning
//package com.gtappdevelopers.spotify_kotlin

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var albumID: String
    private lateinit var albumImgUrl: String
    private lateinit var albumName: String
    private lateinit var artist: String
    private lateinit var albumUrl: String

    private lateinit var albumNameTV: TextView
    private lateinit var artistTV: TextView
    private lateinit var albumIV: ImageView
    private lateinit var playFAB: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)

        albumID = intent.getStringExtra("id") ?: ""
        albumImgUrl = intent.getStringExtra("img") ?: ""
        albumName = intent.getStringExtra("name") ?: ""
        artist = intent.getStringExtra("artist") ?: ""
        albumUrl = intent.getStringExtra("albumUrl") ?: ""

        Log.e("TAG", "album id is : $albumID")

        albumIV = findViewById(R.id.idIVAlbum)
        albumNameTV = findViewById(R.id.idTVAlbumName)
        playFAB = findViewById(R.id.idFABPlay)
        artistTV = findViewById(R.id.idTVArtistName)

        albumNameTV.text = albumName
        artistTV.text = artist

        playFAB.setOnClickListener {
            val uri = Uri.parse(albumUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

            // Handler to bring your app back to the foreground after 2 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                // Intent that relaunches your app's MainActivity
                val returnIntent = Intent(applicationContext, chatPage::class.java)
                returnIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(returnIntent)
            }, 2000) // 2000 milliseconds delay for 2 seconds
        }

        Picasso.get().load(albumImgUrl).into(albumIV)

        getAlbumTracks(albumID)
    }

    private fun getToken(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        return sharedPreferences.getString("token", "Not Found") ?: "Not Found"
    }

    private fun getAlbumTracks(albumID: String) {
        val url = "https://api.spotify.com/v1/albums/$albumID/tracks"
        val trackRVModals = ArrayList<TrackRVModal>()
        val trackRVAdapter = TrackRVAdapter(trackRVModals, this)
        val trackRV: RecyclerView = findViewById(R.id.idRVTracks)
        trackRV.adapter = trackRVAdapter

        val queue: RequestQueue = Volley.newRequestQueue(this)

        val trackObj = object : JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    val itemsArray: JSONArray = response.getJSONArray("items")
                    for (i in 0 until itemsArray.length()) {
                        val itemObj = itemsArray.getJSONObject(i)
                        val trackName = itemObj.getString("name")
                        val id = itemObj.getString("id")
                        val trackArtist = itemObj.getJSONArray("artists").getJSONObject(0).getString("name")
                        trackRVModals.add(TrackRVModal(trackName, trackArtist, id))
                    }
                    trackRVAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this@AlbumDetailActivity, "Fail to get Tracks $error", Toast.LENGTH_SHORT).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = getToken()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        queue.add(trackObj)
    }
}
