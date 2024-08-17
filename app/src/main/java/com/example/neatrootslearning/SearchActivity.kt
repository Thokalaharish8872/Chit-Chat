package com.example.neatrootslearning
//package com.gtappdevelopers.spotify_kotlin

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {

    private lateinit var searchQuery: String
    private lateinit var searchEdt: EditText
    private lateinit var songsRV: RecyclerView
    private lateinit var trackRVAdapter: TrackRVAdapter
    private val trackRVModals = ArrayList<TrackRVModal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEdt = findViewById(R.id.idEdtSearch)
        songsRV = findViewById(R.id.idRVSongs)

        searchQuery = intent.getStringExtra("searchQuery") ?: ""
        searchEdt.setText(searchQuery)

        // Setup RecyclerView
        songsRV.layoutManager = LinearLayoutManager(this)
        trackRVAdapter = TrackRVAdapter(trackRVModals, this)
        if (songsRV != null) {
            songsRV.adapter = trackRVAdapter
        } else {
            // Handle the case where RecyclerView is not found
            Log.e("MusicPage", "RecyclerView not found!")
        }


        searchEdt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTracks(searchEdt.text.toString())
                true
            } else {
                false
            }
        }

        // Get initial tracks
        getTracks(searchQuery)
    }

    private fun getToken(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        return sharedPreferences.getString("token", "Not Found") ?: "Not Found"
    }

    private fun getTracks(searchQuery: String) {
        val url = "https://api.spotify.com/v1/search?q=$searchQuery&type=track"
        val queue: RequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    // Print the entire response to the console
                    println("Response: $response")

                    val trackObj: JSONObject = response.getJSONObject("tracks")
                    val itemsArray: JSONArray = trackObj.getJSONArray("items")
                    for (i in 0 until itemsArray.length()) {
                        val itemObj: JSONObject = itemsArray.getJSONObject(i)
                        val trackName = itemObj.getString("name")
                        val trackArtist = itemObj.getJSONArray("artists").getJSONObject(0).getString("name")
                        val trackID = itemObj.getString("id")
                        trackRVModals.add(TrackRVModal(trackName, trackArtist, trackID))
                    }
                    trackRVAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Fail to get data: $error", Toast.LENGTH_SHORT).show()
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

        queue.add(jsonObjectRequest)
    }}


