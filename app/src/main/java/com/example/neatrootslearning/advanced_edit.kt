package com.example.neatrootslearning

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class advanced_edit : AppCompatActivity() {
    lateinit var database: DatabaseReference
    lateinit var myref: FirebaseDatabase
    lateinit var Phone: String
    lateinit var Name:String
    lateinit var About:String
    lateinit var Profile:String
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_edit)
        var chat_Tab_wallpaper=findViewById<TextView>(R.id.chats_tab_wallpaper)
        var chat_page_wallpaper=findViewById<TextView>(R.id.Chats_page_wallpaper)
        Phone = intent.getStringExtra("Phone").toString()
        Name= intent.getStringExtra("Name").toString()
        Profile = intent.getStringExtra("Photo").toString()
        About = intent.getStringExtra("About").toString()
        chat_Tab_wallpaper.setOnClickListener(){
            intent= Intent(this,chat_tab_wallpaper::class.java)
        }
        chat_page_wallpaper.setOnClickListener(){
            intent=Intent(this, com.example.neatrootslearning.chat_page_wallpaper::class.java)
            intent.putExtra("Name",Name)
            intent.putExtra("Phone",Phone)
            intent.putExtra("About",About)
            intent.putExtra("Photo",Profile)
            startActivity(intent)
            finish()
        }
        chat_Tab_wallpaper.setOnClickListener(){
            intent=Intent(this, com.example.neatrootslearning.chat_tab_wallpaper::class.java)
            intent.putExtra("Name",Name)
            intent.putExtra("Phone",Phone)
            intent.putExtra("About",About)
            intent.putExtra("Photo",Profile)
            startActivity(intent)
            finish()
        }
    }
}