package com.example.neatrootslearning
import AppLifecycleObserver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class musicpage_spotify : AppCompatActivity() {

    private lateinit var lifecycleObserver: AppLifecycleObserver
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_musicpage_spotify)
            // on below line calling method to
            // load our recycler views and search view.
            initializeAlbumsRV()
            initializePopularAlbumsRV()
            initializeTrendingAlbumsRV()
            initializeSearchView()
        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        var phoneNumber = sharedPref.getString("PhoneNumber", null).toString()
            lifecycleObserver = AppLifecycleObserver(phoneNumber!!)
            lifecycle.addObserver(lifecycleObserver)
        }

        // below method is used to initialize search view.
        private fun initializeSearchView() {
            // on below line initializing our
            // edit text for search views.
            val searchEdt = findViewById<EditText>(R.id.idEdtSearch)
            searchEdt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // on below line calling method to search tracks.
                    searchTracks(searchEdt.text.toString())
                    return@OnEditorActionListener true
                }
                false}) }

//         below method is used to open search
//         tracks activity to search tracks.
        private fun searchTracks(searchQuery: String) {
            // on below line opening search activity to
            // display search results in search activity.
            val i = Intent(this@musicpage_spotify, SearchActivity::class.java)
            i.putExtra("searchQuery", searchQuery)
            startActivity(i)
        }

        // below method is used to get token.
        private fun getToken(): String {
            // on below line getting token from shared prefs.
            val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            return sh.getString("token", "Not Found") ?: "Not Found"
        }
        override fun onStart() {
            super.onStart()
            // on below line calling generate
            // token method to generate token.
            generateToken()
        }
        private fun generateToken() {
            // on below line creating a variable for
            // url to generate access token
            val url = "https://accounts.spotify.com/api/token?grant_type=client_credentials"
            val queue = Volley.newRequestQueue(this@musicpage_spotify)
            // on below line making string request to generate access token.
            val request: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    // on below line getting access token and
                    // saving it to shared preferences.
                    val tk = jsonObject.getString("access_token")
                    val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                    val myEdit = sharedPreferences.edit()
                    myEdit.putString("token", "Bearer $tk")
                    myEdit.apply()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                // method to handle errors.
                Toast.makeText(this@musicpage_spotify, "Fail to get response = $error", Toast.LENGTH_SHORT).show()
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    // on below line passing headers.
                    // Make sure to add your authorization.
                    headers["Authorization"] = "Basic NDM4Y2YwMTIyOGYzNGNhOTgyNjU0YzkwYzY4ZGQwOTM6MmFiMDI2YzAxYjYwNDMzNWEwYWY0ZTQyNmIwMGZkNTY="
                    headers["Content-Type"] = "application/x-www-form-urlencoded"
                    return headers
                }
            }
            // adding request to queue.
            queue.add(request)
        }


    private fun initializeAlbumsRV() {
        // on below line initializing albums rv
        val albumsRV = findViewById<RecyclerView>(R.id.idRVAlbums)
        // on below line creating list, initializing adapter
        // and setting it to recycler view.
        val albumRVModalArrayList = ArrayList<AlbumRVModal>()
        val albumRVAdapter = AlbumRVAdapter(albumRVModalArrayList, this)
        albumsRV.adapter = albumRVAdapter
        // on below line creating a variable for url
        val url = "https://api.spotify.com/v1/albums?ids=2oZSF17FtHQ9sYBscQXoBe%2C0z7bJ6UpjUw8U4TATtc5Ku%2C36UJ90D0e295TvlU109Xvy%2C3uuu6u13U0KeVQsZ3CZKK4%2C45ZIondgVoMB84MQQaUo9T%2C15CyNDuGY5fsG0Hn9rjnpG%2C1HeX4SmCFW4EPHQDvHgrVS%2C6mCDTT1XGTf48p6FkK9qFL"
        val queue = Volley.newRequestQueue(this@musicpage_spotify)
        // on below line making json object request to parse json data.
        val albumObjReq = object : JsonObjectRequest(Method.GET, url, null, Response.Listener { response ->
            try {
                val albumArray = response.getJSONArray("albums")
                for (i in 0 until albumArray.length()) {
                    val albumObj = albumArray.getJSONObject(i)
                    val albumType = albumObj.getString("album_type")
                    val artistName = albumObj.getJSONArray("artists").getJSONObject(0).getString("name")
                    val externalIds = albumObj.getJSONObject("external_ids").getString("upc")
                    val externalUrls = albumObj.getJSONObject("external_urls").getString("spotify")
                    val href = albumObj.getString("href")
                    val id = albumObj.getString("id")
                    val imgUrl = albumObj.getJSONArray("images").getJSONObject(1).getString("url")
                    val label = albumObj.getString("label")
                    val name = albumObj.getString("name")
                    val popularity = albumObj.getInt("popularity")
                    val releaseDate = albumObj.getString("release_date")
                    val totalTracks = albumObj.getInt("total_tracks")
                    val type = albumObj.getString("type")
                    // on below line adding data to array list.
                    albumRVModalArrayList.add(AlbumRVModal(albumType, artistName, externalIds, externalUrls, href, id, imgUrl, label, name, popularity, releaseDate, totalTracks, type))

                }
                albumRVAdapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this@musicpage_spotify, "Fail to get data : $error", Toast.LENGTH_SHORT).show()
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // on below line passing headers.
                val headers = HashMap<String, String>()
                headers["Authorization"] = getToken()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }

        }
        // on below line adding request to queue.
        queue.add(albumObjReq)
    }

    private fun initializePopularAlbumsRV() {
        // on below line creating list, initializing
        // adapter and setting it to recycler view.
        val albumsRV = findViewById<RecyclerView>(R.id.idRVPopularAlbums)
        val albumRVModalArrayList = ArrayList<AlbumRVModal>()
        val albumRVAdapter = AlbumRVAdapter(albumRVModalArrayList, this)
        albumsRV.adapter = albumRVAdapter
        // on below line creating a variable for url
        val url = "https://api.spotify.com/v1/albums?ids=0sjyZypccO1vyihqaAkdt3%2C17vZRWjKOX7TmMktjQL2Qx%2C7lF34sP6HtRAL7VEMvYHff%2C2zXKlf81VmDHIMtQe3oD0r%2C7Gws1vUsWltRs58x8QuYVQ%2C7uftfPn8f7lwtRLUrEVRYM%2C7kSY0fqrPep5vcwOb1juye"
        val queue = Volley.newRequestQueue(this@musicpage_spotify)
        // on below line making json object request to parse json data.
        val albumObjReq = object : JsonObjectRequest(Method.GET, url, null, Response.Listener { response ->
            try {
                val albumArray = response.getJSONArray("albums")
                for (i in 0 until albumArray.length()) {
                    val albumObj = albumArray.getJSONObject(i)
                    val albumType = albumObj.getString("album_type")
                    val artistName = albumObj.getJSONArray("artists").getJSONObject(0).getString("name")
                    val externalIds = albumObj.getJSONObject("external_ids").getString("upc")
                    val externalUrls = albumObj.getJSONObject("external_urls").getString("spotify")
                    val href = albumObj.getString("href")
                    val id = albumObj.getString("id")
                    val imgUrl = albumObj.getJSONArray("images").getJSONObject(1).getString("url")
                    val label = albumObj.getString("label")
                    val name = albumObj.getString("name")
                    val popularity = albumObj.getInt("popularity")
                    val releaseDate = albumObj.getString("release_date")
                    val totalTracks = albumObj.getInt("total_tracks")
                    val type = albumObj.getString("type")
                    // on below line adding data to array list.
                    albumRVModalArrayList.add(AlbumRVModal(albumType, artistName, externalIds, externalUrls, href, id, imgUrl, label, name, popularity, releaseDate, totalTracks, type))
                }
                albumRVAdapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this@musicpage_spotify, "Fail to get data : $error", Toast.LENGTH_SHORT).show()
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // on below line passing headers.
                val headers = HashMap<String, String>()
                headers["Authorization"] = getToken()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        // on below line adding request to queue.
        queue.add(albumObjReq)
    }
    private fun initializeTrendingAlbumsRV() {
        // on below line creating list, initializing adapter
        // and setting it to recycler view.
        val albumsRV = findViewById<RecyclerView>(R.id.idRVTrendingAlbums)
        val albumRVModalArrayList = ArrayList<AlbumRVModal>()
        val albumRVAdapter = AlbumRVAdapter(albumRVModalArrayList, this)
        albumsRV.adapter = albumRVAdapter
        // on below line creating a variable for url
        val url = "https://api.spotify.com/v1/albums?ids=1P4eCx5b11Tfmi4s1GmWmQ%2C2SsEtiB6yJYn8hRRAmtVda%2C7hhxms8KCwlQCWffIJpN9b%2C3umvKIjsD484pa9pCyPK2x%2C3OHC6XD29wXWADtAOP2geV%2C3RZxrS2dDZlbsYtMRM89v8%2C24C47633GRlozws7WBth7t"
        val queue = Volley.newRequestQueue(this@musicpage_spotify)
        // on below line making json object request to parse json data.
        val albumObjReq = object : JsonObjectRequest(Method.GET, url, null, Response.Listener { response ->
            try {
                val albumArray = response.getJSONArray("albums")
                for (i in 0 until albumArray.length()) {
                    val albumObj = albumArray.getJSONObject(i)
                    val albumType = albumObj.getString("album_type")
                    val artistName = albumObj.getJSONArray("artists").getJSONObject(0).getString("name")
                    val externalIds = albumObj.getJSONObject("external_ids").getString("upc")
                    val externalUrls = albumObj.getJSONObject("external_urls").getString("spotify")
                    val href = albumObj.getString("href")
                    val id = albumObj.getString("id")
                    val imgUrl = albumObj.getJSONArray("images").getJSONObject(1).getString("url")
                    val label = albumObj.getString("label")
                    val name = albumObj.getString("name")
                    val popularity = albumObj.getInt("popularity")
                    val releaseDate = albumObj.getString("release_date")
                    val totalTracks = albumObj.getInt("total_tracks")
                    val type = albumObj.getString("type")
                    // on below line adding data to array list.
                    albumRVModalArrayList.add(AlbumRVModal(albumType, artistName, externalIds, externalUrls, href, id, imgUrl, label, name, popularity, releaseDate, totalTracks, type))
                }
                albumRVAdapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this@musicpage_spotify, "Fail to get data : $error", Toast.LENGTH_SHORT).show()
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // on below line passing headers.
                val headers = HashMap<String, String>()
                headers["Authorization"] = getToken()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        // on below line adding request to queue.
        queue.add(albumObjReq)
    }


}