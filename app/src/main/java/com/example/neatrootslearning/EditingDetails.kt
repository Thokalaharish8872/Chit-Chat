package com.example.neatrootslearning

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class EditingDetails : AppCompatActivity() {
    lateinit var database: DatabaseReference
    private lateinit var sharedPref: SharedPreferences
    private lateinit var phoneNumber: String
    private lateinit var profileimg: CircleImageView
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editing_details)
        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        phoneNumber = sharedPref.getString("PhoneNumber", null).toString()

        var nameof = sharedPref.getString("Name", null)
        var about = sharedPref.getString("About", null)
        var password = sharedPref.getString("Password", null)
        var name4 = findViewById<TextInputEditText>(R.id.name100)

        var password2 = password.toString()


        name4.setText(nameof)

        var About2 = findViewById<TextInputEditText>(R.id.About)
        var btnsave = findViewById<Button>(R.id.savebtn)
        var btncancel = findViewById<Button>(R.id.cancelbtn)
        var phone = findViewById<TextInputEditText>(R.id.phone)
        var name = findViewById<TextInputEditText>(R.id.name)
        var uploadimg = findViewById<ImageButton>(R.id.editphoto)
        profileimg = findViewById<CircleImageView>(R.id.circleImageView)
        val tag2: Any? = profileimg.getTag()
        if (tag2 is Int) {
            val img3: Int = tag2
            with(sharedPref.edit()) {
                putInt("img", img3)
                apply()
            }


        }

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("users")
        var result=imageRef.child(phoneNumber).child("profilepic")
        Toast.makeText(this,phoneNumber,Toast.LENGTH_SHORT).show()

        result.downloadUrl.addOnSuccessListener { uri ->
            val savedUri = sharedPref.getString("uri", null)
            if (savedUri != null){
                if(savedUri.isNotEmpty()) {
                    Picasso.get().load(savedUri).into(profileimg)
                } else {
                    Picasso.get().load(uri).into(profileimg)
                }
            }else {
                Picasso.get().load(uri).into(profileimg)
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
        }







        name.setText(nameof)
        About2.setText(about)

        phone.isEnabled = false

        phone.setText(phoneNumber)

        var phone2 = phone.text.toString()
        var name5 = name4.text.toString()
        var edit = users2(name5)
        var name6 = edit.name7

        btnsave.setOnClickListener() {
            var About3 = About2.text.toString()
            var name3 = name.text.toString()

            if (name6.isNotEmpty()) {
                if (phone2.isNotEmpty()) {
                    if (About3.isNotEmpty()) {
                        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("Name", name3)
                            putString("About", About3)
                            apply()
                        }
                        var user = users(name3, phone2, password2)
                        database = FirebaseDatabase.getInstance().getReference("users")
                        database.child(phone2).child("name").setValue(name3).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Changes made Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            intent = Intent(this, StartUpPage::class.java)
                            startActivity(intent)
                        }
                    } else {
                        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("Name", name6)
                            apply()
                        }
                    }
                } else {
                    Toast.makeText(this, "PhoneNumber cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val img3 = sharedPref.getInt("img", 0)
        btncancel.setOnClickListener() {
          profileimg.setImageResource(img3)
            intent = Intent(this, StartUpPage::class.java)
            startActivity(intent)
        }
        uploadimg.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            }
            intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            imagelauncher.launch(intent)
        }


//        else{
//            Toast.makeText(this, "Profile pic Uploaded Successfully", Toast.LENGTH_SHORT).show()
//
//        }

    }

    var imagelauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                var ref = Firebase.storage.reference.child("users")
                ref.child(phoneNumber.toString()).child("profilepic").putFile(it.data!!.data!!).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        profileimg.setImageURI(uri)
                        Picasso.get().load(uri.toString()).into(profileimg)
                        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                        val phone5 = sharedPref.getString("PhoneNumber", null)
                        database=FirebaseDatabase.getInstance().getReference("users")
                        database.child(phoneNumber).child("photo").setValue(uri.toString())

                        Toast.makeText(this, "Profile pic Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("uri", uri.toString())
                            apply()

                        }
                    }
                }
            }
        }
    }
}
