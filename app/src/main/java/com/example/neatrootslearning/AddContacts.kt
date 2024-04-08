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
import de.hdodenhof.circleimageview.CircleImageView

class AddContacts : AppCompatActivity() {
    companion object{
        const val KEY = ""
    }
    lateinit var profileimg:CircleImageView
    lateinit var img2:CircleImageView
    lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contacts)
        var phone1 = findViewById<TextInputEditText>(R.id.edit_phone)
        var search = findViewById<Button>(R.id.btn_Search)
        var txtname= findViewById<TextInputEditText>(R.id.editname)
        var txtname2 = findViewById<TextInputEditText>(R.id.editname2)
        var found = findViewById<TextView>(R.id.found)
//        var circularimg = findViewById<CircleImageView>(R.id.imageView2)
        found.alpha=0f
        txtname2.alpha=0f
//        var addbtn=findViewById<Button>(R.id.btn_Add)
//        addbtn.alpha=0f
//        txtname.alpha=0f

        val phoneNumber6 = intent.getStringExtra(Login.KEY)
        var PhoneNumbers7 = phoneNumber6
//        Toast.makeText(this,PhoneNumbers7.toString(),Toast.LENGTH_SHORT).show()

        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val phoneNumber = sharedPref.getString("PhoneNumber", null)

//        Toast.makeText(this,phoneNumber,Toast.LENGTH_SHORT).show()



        search.setOnClickListener() {

            var phone2 = phone1.text.toString()


            if (phone2.isNotEmpty()) {

                database = FirebaseDatabase.getInstance().getReference("users")
                database.child(phone2).get().addOnSuccessListener {
                    if (it.exists()) {
                        var phone = it.child("phone").value.toString()
                        var name = it.child("name").value.toString()
                        if (phone == phone2) {
                            txtname2.alpha = 1f
                            found.alpha = 1f
                            found.setText("Contact found")
//                            txtname2.isEnabled=false
                            txtname.setText(name)
                            search.setText("Add Contact")
                            search.setOnClickListener() {
                                txtname.setText(name)
//                                var circularimg=R.drawable.no_profile_pic
//                                img2.setImageResource(circularimg)
//                                var user = startuppagedata(pic=img2)
                                database.child(phone2).get().addOnSuccessListener {
                                    var uriimg = it.child("photo").value.toString()

                                    // Save the contact to shared preferences
                                    database.child(phoneNumber.toString()).child("Added Contacts")
                                        .child(phone)
                                        .setValue(startuppagedata(name, phone, picUri  = uriimg))
                                    Toast.makeText(
                                        this,
                                        "Contact Added Succesfully",
                                        Toast.LENGTH_SHORT
                                    ).show()




                                intent = Intent(this, StartUpPage::class.java)
                                intent.putExtra("phoneNumber", phone)
                                startActivity(intent)
                            }
                        }

                        } else {
                            txtname.isEnabled=false
                            found.alpha=1f
                            found.setText("Contact not found")

                        }
                    }
                    else {
                        txtname.isEnabled=false
                        found.alpha=1f
                        found.setText("PhoneNumber Does not exist")

                    }
                }.addOnFailureListener() {
                    txtname.isEnabled=false
                    found.alpha=1f
                    found.setText("Network issue")

                }
            } else {
                txtname.isEnabled=false
                found.alpha=1f
                found.setText("Please enter PhoneNumber")

            }
        }
        }
    }
