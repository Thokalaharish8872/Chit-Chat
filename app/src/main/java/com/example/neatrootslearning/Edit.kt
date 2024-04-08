package com.example.neatrootslearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Edit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
//        var Details=intent.getStringExtra(SignUp.KEY)
        var phone=findViewById<TextView>(R.id.Phone)
        var btn_edit=findViewById<Button>(R.id.btn_edit)
        var btn_no=findViewById<Button>(R.id.btn_no)

        btn_edit.setOnClickListener(){
            intent = Intent(applicationContext,SignUp::class.java)
            startActivity(intent)
        }
        btn_no.setOnClickListener(){
            intent = Intent(applicationContext,StartUpPage::class.java)
            startActivity(intent)
        }

//        phone.text=Details

    }
}