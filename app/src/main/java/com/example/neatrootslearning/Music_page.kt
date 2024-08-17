package com.example.neatrootslearning

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Music_page : AppCompatActivity() {
    lateinit var adapter: MusicAdapter
    lateinit var dataList: List<Item>
    lateinit var phone: String
    lateinit var name: String
    lateinit var about: String
    lateinit var profile: String
    private lateinit var phoneNumber: String
    lateinit var message: details
    lateinit var messages: MutableList<details>

    data class details(var phonenum: String = "", var myphone: String = "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_page)

        val recyclerView = findViewById<RecyclerView>(R.id.Music_Recyclerview)

        val intent = intent
        if (intent != null) {
            phone = intent.getStringExtra("Phone").toString()
            name = intent.getStringExtra("Name").toString()
            profile = intent.getStringExtra("Photo").toString()
            about = intent.getStringExtra("About").toString()
        }

        messages = mutableListOf()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://spotify23.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api_Interface::class.java)

        val retrofitData = retrofitBuilder.getData("telugu")
        retrofitData.enqueue(object : Callback<TeluguData> {
            override fun onResponse(call: Call<TeluguData>, response: Response<TeluguData>) {
                if (response.isSuccessful) {
                    dataList = response.body()?.tracks?.items ?: emptyList()

                    adapter = MusicAdapter(this@Music_page, dataList)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@Music_page)
                } else {
                    Toast.makeText(this@Music_page, "Error fetching data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TeluguData>, t: Throwable) {
                Log.d("TAG : onFailure", "onFailure: " + t.message)
                Toast.makeText(this@Music_page, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
