package com.example.neatrootslearning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Edit_Contact_details : AppCompatActivity() {
    lateinit var database: DatabaseReference
    lateinit var myref: FirebaseDatabase
    lateinit var Phone: String
    lateinit var Name:String
    lateinit var About:String
    lateinit var Profile:String
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact_details)
        Phone = intent.getStringExtra("Phone").toString()
        Name= intent.getStringExtra("Name").toString()
        Profile = intent.getStringExtra("Photo").toString()
        About = intent.getStringExtra("About").toString()
        var Profile_pic2=findViewById<CircleImageView>(R.id.Profile_Pic)
        var Name2=findViewById<TextInputEditText>(R.id.name)
        var About2=findViewById<TextInputEditText>(R.id.About)
        var Phone2=findViewById<TextInputEditText>(R.id.phone)
        var btnsave = findViewById<Button>(R.id.savebtn)
        var On_Off=findViewById<TextView>(R.id.On_Off)
        Name2.setText(Name)
        Phone2.setText(Phone)
        About2.setText(About)
        Phone2.isEnabled = false
        About2.isEnabled=false



        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        phoneNumber = sharedPref.getString("PhoneNumber", null).toString()
        if(Profile.isNotEmpty()){
            Picasso.get().load(Profile).into(Profile_pic2)
        }

        database=FirebaseDatabase.getInstance().getReference("users")
        database.child(phoneNumber.toString()).child("Added Contacts").child(Phone).get().addOnSuccessListener {
            var IsOn=it.child("Floating_Notifications").value
            if(IsOn=="True"){
                On_Off.text="ON"}
            else{
                On_Off.text="OFF"
            }
            btnsave.setOnClickListener() {
                var About3 = About2.text.toString()
                var name3 = Name2.text.toString()
                database=FirebaseDatabase.getInstance().getReference("users")
                database.child(phoneNumber.toString()).child("Added Contacts").child(Phone).child("nickname").setValue(name3).addOnSuccessListener {
                    Toast.makeText(this,"Changes made Successfully",Toast.LENGTH_SHORT).show()

                    intent= Intent(this,StartUpPage::class.java)
                    startActivity(intent)
                }
            }
        On_Off.setOnClickListener(){
            database=FirebaseDatabase.getInstance().getReference("users")
            database.child(phoneNumber.toString()).child("Added Contacts").child(Phone).get().addOnSuccessListener {
                IsOn=it.child("Floating_Notifications").value

                if(IsOn=="True"){
                    On_Off.text="OFF"
                    database.child(phoneNumber.toString()).child("Added Contacts").child(Phone).child("Floating_Notifications").setValue("False").addOnSuccessListener {
                        Toast.makeText(this,"Floating Notifications are Disabled",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    On_Off.text="ON"
                    database.child(phoneNumber.toString()).child("Added Contacts").child(Phone).child("Floating_Notifications").setValue("True")
                    Toast.makeText(this,"Floating Notifications are Enabled",Toast.LENGTH_SHORT).show()
                }
            }}
        }




    }
}