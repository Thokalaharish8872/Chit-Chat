package com.example.neatrootslearning

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class StartUpPage : AppCompatActivity() {

    var arraylist= arrayListOf<startuppagedata>()
    var arraylist2= arrayListOf<users3>()
    lateinit var database: DatabaseReference
    lateinit var editphone:TextView
    var img2=123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up_page)
        var imageview = findViewById<CircleImageView>(R.id.imageView)




//        val phoneNumber = intent.getStringExtra(Login.KEY)
//        var PhoneNumbers = phoneNumber
//       Toast.makeText(this,PhoneNumbers,Toast.LENGTH_SHORT).show()
        database = FirebaseDatabase.getInstance().getReference("users")

        // Initialize the ArrayList here, outside the database call
        arraylist = ArrayList()
        arraylist2 = ArrayList()
        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        var phoneNumber = sharedPref.getString("PhoneNumber", null)
        var password = sharedPref.getString("Password", null)
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(phoneNumber.toString()).get().addOnSuccessListener {
            var uriimg = it.child("photo").value
            Picasso.get().load(uriimg.toString())
                .error(R.drawable.pic2)
                .into(imageview)// will be used if the network request fails


            // Set the image and the tag

        }
// Get the resource ID from the tag
//            var imageResourceId: Int = imageview
//            val tag = imageview.getTag()
//            if (tag is Int) {
//                imageResourceId = tag
//                // Now imageResourceId is the integer you wanted
//            }







//            img2.setImageResource(imageResourceId)
//            var users = startuppagedata(pic =imageResourceId ?: R.drawable.no_profile_pic)
//            arraylist.add(users)


        database.child(phoneNumber.toString()).child("Added Contacts").get()
            .addOnSuccessListener { dataSnapshot ->
                database.child(phoneNumber.toString()).get().addOnSuccessListener {
                for (snapshot in dataSnapshot.children) {
                    val contact = snapshot.getValue(startuppagedata::class.java)
//                    val imgpic=snapshot.child("photo").value
                    if (contact != null) {
//                        contact.pic = imageResourceId ?: R.drawable.no_profile_pic
                        arraylist.add(contact)
//                        var users = startuppagedata(pic =imageResourceId ?: R.drawable.no_profile_pic)
//                        arraylist.add(users)

                    }
                }
//                var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
//                val image = sharedPref.getInt("img", 0)


                editphone = findViewById<TextView>(R.id.editphone)
                var addcontact = findViewById<ImageButton>(R.id.addContacts)
                var recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                var Music = findViewById<Button>(R.id.Musicbtn)
                var tasklist = arrayListOf<String>()
                var phone4 = editphone.text.toString()
                var logoutbtn = findViewById<Button>(R.id.Logoutbtn)
                var menubtn = findViewById<ImageButton>(R.id.menubtn)

                val layoutManager = LinearLayoutManager(this)
                layoutManager.reverseLayout = false
                layoutManager.stackFromEnd = false
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = MyAdapter(this, arraylist)

                addcontact.setOnClickListener() {
                    intent = Intent(applicationContext, AddContacts::class.java)
                    startActivity(intent)
                }
                Music.setOnClickListener() {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spotify.com"))
                    var choose = Intent.createChooser(intent, "Open With")
                    startActivity(choose)

                }
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("users")
                    var result=imageRef.child(phoneNumber.toString()).child("profilepic")

                    val file = Uri.fromFile(File("users/no_profile_pic.png"))
                    val uploadTask = result.putFile(file)

                    uploadTask.addOnSuccessListener {
                        // Handle successful uploads
                    }.addOnFailureListener {
                        Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                        // Handle unsuccessful uploads
                    }
                logoutbtn.setOnClickListener() {


                    val editor = sharedPref.edit()
                    editor.putBoolean("isLoggedIn", false)
                    editor.apply()

                    intent = Intent(applicationContext, SignUp::class.java)
                    startActivity(intent)

                }
                menubtn.setOnClickListener() {
                    intent = Intent(this, EditingDetails::class.java)
                    startActivity(intent)
                }
            }
            }
    }
    }

