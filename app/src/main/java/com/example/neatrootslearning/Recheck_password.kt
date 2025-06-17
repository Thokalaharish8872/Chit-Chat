package com.example.neatrootslearning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Recheck_password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recheck_password)
        var b1=findViewById<Button>(R.id.b1)
        var b2=findViewById<Button>(R.id.b2)
        var b3=findViewById<Button>(R.id.b3)
        var b4=findViewById<Button>(R.id.b4)
        var b5=findViewById<Button>(R.id.b5)
        var b6=findViewById<Button>(R.id.b6)
        var b7=findViewById<Button>(R.id.b7)
        var b8=findViewById<Button>(R.id.b8)
        var b9=findViewById<Button>(R.id.b9)
        var b0=findViewById<Button>(R.id.b0)
        var bx=findViewById<Button>(R.id.bx)
        var inputPassword=findViewById<TextView>(R.id.inputpassword)
        var bsave=findViewById<Button>(R.id.bSave)
        b1.setOnClickListener(){
            inputPassword.append("1")
        }
        b2.setOnClickListener(){
            inputPassword.append("2")
        }
        b3.setOnClickListener(){
            inputPassword.append("3")
        }
        b4.setOnClickListener(){
            inputPassword.append("4")
            Toast.makeText(this@Recheck_password, inputPassword.text.toString(), Toast.LENGTH_SHORT).show()
        }
        b5.setOnClickListener(){
            inputPassword.append("5")
        }
        b6.setOnClickListener(){
            inputPassword.append("6")
        }
        b7.setOnClickListener(){
            inputPassword.append("7")
        }
        b8.setOnClickListener(){
            inputPassword.append("8")
        }
        b9.setOnClickListener(){
            inputPassword.append("9")
        }
        b0.setOnClickListener(){
            inputPassword.append("0")
        }
        bx.setOnClickListener() {
            val currentText = inputPassword.text.toString()
            if (currentText.isNotEmpty()) {
                // Remove the last character
                val newText = currentText.substring(0, currentText.length - 1)
                inputPassword.text = newText
            }
        }
        bsave.setOnClickListener {
            var sharedPref = getSharedPreferences("Hidden_Chats", Context.MODE_PRIVATE)
            var Recent_pass=sharedPref.getString("Hidden_chats_password",null)
//            Toast.makeText(this@Recheck_password, Recent_pass.toString(), Toast.LENGTH_SHORT).show()
            if(Recent_pass.toString()==inputPassword.text.toString()){
                Toast.makeText(this,"Password Created Succesfully",Toast.LENGTH_SHORT).show()
//                sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
//                with(sharedPref.edit()) {
//                    putBoolean("Is_Password_updated", true)
////                Toast.makeText(this@Password_Check, inputPassword.text.toString(), Toast.LENGTH_SHORT).show()
//                    apply()
//                }
                intent= Intent(this,Hidden_chats::class.java)
                startActivity(intent)
                finish()
            }else{

//                Toast.makeText(this@Recheck_password, inputPassword.text.toString(), Toast.LENGTH_SHORT).show()

//                Toast.makeText(this,Recent_pass.toString(),Toast.LENGTH_SHORT).show()
                Toast.makeText(this,"Password Did't match",Toast.LENGTH_SHORT).show()
            }
        }
    }
}