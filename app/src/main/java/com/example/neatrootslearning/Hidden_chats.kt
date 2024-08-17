package com.example.neatrootslearning

import AppLifecycleObserver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Hidden_chats : AppCompatActivity() {
    private lateinit var activityContext: Context

    var arraylist= arrayListOf<startuppagedata>()
    var arraylist2= arrayListOf<startuppagedata>()
    lateinit var database: DatabaseReference
    lateinit var editphone: TextView
    var img2=123
    lateinit var shimmerView: ShimmerFrameLayout
//    lateinit var shimmerView2: ShimmerFrameLayout
    lateinit var myAdapter:MyAdapter
    lateinit var myAdapter2:MyAdapter2
    var wallpaper_value=0
    private lateinit var lifecycleObserver: AppLifecycleObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hidden_chats)
        shimmerView=findViewById(R.id.shimmer_layout)
        var menubtn = findViewById<ImageButton>(R.id.menubtn)
        menubtn.setOnClickListener() {
            intent = Intent(this, EditingDetails::class.java)
            startActivity(intent)
        }
        arraylist = ArrayList()
        arraylist2 = ArrayList()

        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val PhoneNumber = sharedPref.getString("PhoneNumber", null)
        var databaseRef = FirebaseDatabase.getInstance().getReference("users")
        databaseRef.child(PhoneNumber.toString()).child("Added Contacts").child("Hidden Contacts").get().addOnSuccessListener { dataSnapshot ->
            arraylist.clear()
            for (snapshot in dataSnapshot.children) {
                val contactId = snapshot.key
//                Toast.makeText(this, contactId, Toast.LENGTH_SHORT).show()
                if (contactId != null) {

                    databaseRef.child(contactId).get()
                        .addOnSuccessListener { contactSnapshot ->

                            val contact =
                                contactSnapshot.getValue(startuppagedata::class.java)

                            if (contact != null) {



                                arraylist.add(contact)
                                myAdapter.notifyDataSetChanged()



                            }








                        }
                }
            }

//                    editphone = findViewById<TextView>(R.id.editphone)

            var recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

            var tasklist = arrayListOf<String>()
//                    var phone4 = editphone.text.toString()


            var adsbtn = findViewById<Button>(R.id.ads_page)
            adsbtn.setOnClickListener(){
                intent= Intent(this,Ads_page::class.java)
                startActivity(intent)
            }

            var layoutManager = LinearLayoutManager(this)
            layoutManager.reverseLayout = false
            layoutManager.stackFromEnd = false
            recyclerView.layoutManager = layoutManager
            myAdapter=MyAdapter(this, arraylist)
            recyclerView.adapter = myAdapter



            myAdapter.setItemOnClickListner(object :MyAdapter.onItemClickListner{
                override fun onItemClick(position: Int) {
                    database= FirebaseDatabase.getInstance().getReference("users")
                    database.child(PhoneNumber.toString()).child("Added Contacts").child("Hidden Contacts").child(arraylist[position].phone).get().addOnSuccessListener {
//                                Toast.makeText(this@StartUpPage,isuploaded.toString(),Toast.LENGTH_SHORT).show()
                        var wallpaper=it.child("updated_wallpaper").value

                        intent = Intent(this@Hidden_chats, chatPage::class.java)
                        intent.putExtra("Name", arraylist[position].name)
                        intent.putExtra("Photo", arraylist[position].photo)
                        intent.putExtra("Phone", arraylist[position].phone)

                        intent.putExtra("imageUri",wallpaper.toString())
                        if(wallpaper!=null) {
                            intent.putExtra("isuploaded2", "yes")
                        }
                        else{
                            intent.putExtra("isuploaded2","no")
                        }



                        sharedPref =
                            getSharedPreferences("UserDetails", Context.MODE_PRIVATE)


                        var database2 = FirebaseDatabase.getInstance().getReference("users")
                        database2.child(arraylist[position].phone).get()
                            .addOnSuccessListener {
                                var addedprof = it.child("IsPhotoUploaded").value.toString()
                                intent.putExtra("IsPhotoUploaded", addedprof)




                                database =
                                    FirebaseDatabase.getInstance().getReference("users")
                                database.child(arraylist[position].phone).get()
                                    .addOnSuccessListener {
                                        var about2 = it.child("about").value.toString()
                                        intent.putExtra("About", about2)
//                                Toast.makeText(this@StartUpPage,about2,Toast.LENGTH_SHORT).show()
                                        startActivity(intent)
                                    }
                            }


                    }
                }


            })
            var databaseRef = FirebaseDatabase.getInstance().getReference("users")
            databaseRef.child(PhoneNumber.toString()).child("Added Contacts").child("Hidden Contacts").get().addOnSuccessListener { dataSnapshot2 ->
                arraylist2.clear()
                for (snapshot2 in dataSnapshot2.children) {
                    val contactId2 = snapshot2.key
//                Toast.makeText(this, contactId, Toast.LENGTH_SHORT).show()
                    if (contactId2 != null) {

                        databaseRef.child(contactId2).get()
                            .addOnSuccessListener { contactSnapshot2 ->

                                val contact2 =
                                    contactSnapshot2.getValue(startuppagedata::class.java)

                                if (contact2 != null) {



                                    arraylist2.add(contact2)
                                    myAdapter2.notifyDataSetChanged()



                                }








                            }
                    }
                }
                var recyclerView2 = findViewById<RecyclerView>(R.id.recycler_view_for_status)
                var layoutManager2 = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

                layoutManager2.reverseLayout = false
                layoutManager2.stackFromEnd = false
                recyclerView2.layoutManager = layoutManager2
                myAdapter2=MyAdapter2(this, arraylist2)
                recyclerView2.adapter = myAdapter2
                myAdapter2.setItemOnClickListner(object :MyAdapter2.onItemClickListner{
                    override fun onItemClick(position: Int) {
                        Toast.makeText(this@Hidden_chats,arraylist2[position].name,Toast.LENGTH_SHORT).show()

                    }})

            }
//
//            layoutManager2.reverseLayout = false
//            layoutManager2.stackFromEnd = false
//            recyclerView.layoutManager = layoutManager2
//            myAdapter=MyAdapter(this, arraylist2)
//            recyclerView.adapter = myAdapter
            myAdapter.setItemOnLongClickListener { position ->
                // Get the view of the item at the position
                val v = recyclerView.layoutManager?.findViewByPosition(position)

                // Create a PopupMenu object
                val popup = PopupMenu(this, v)

                // Inflate your menu resource into the PopupMenu
                popup.menuInflater.inflate(R.menu.menu_hidden_contacts, popup.menu)

                // Set a click listener for menu item click
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_button -> {
                            intent= Intent(this,Edit_Contact_details::class.java)

                            intent.putExtra("Name", arraylist[position].name)


//                            Toast.makeText(this, arraylist2[position].phone, Toast.LENGTH_SHORT).show()
//                            Toast.makeText(this,arraylist2[position].phone,Toast.LENGTH_SHORT).show()

                            intent.putExtra("Photo", arraylist[position].photo)
                            intent.putExtra("Phone", arraylist[position].phone)
                            database =
                                FirebaseDatabase.getInstance().getReference("users")
                            database.child(arraylist[position].phone).get()
                                .addOnSuccessListener {
                                    var about2 = it.child("about").value.toString()
                                    intent.putExtra("About", about2)

                                    startActivity(intent)
//                            Toast.makeText(this,arraylist[position].phone,Toast.LENGTH_SHORT).show()
                                }
                            // Handle option 1 click
                            true
                        }
                        R.id.menu_button2 -> {
                            var a=10
                            var b=40
                            a=b
                            b=50

//                            Toast.makeText(this, arraylist2[position].name.toString(), Toast.LENGTH_SHORT).show()
                            // Handle option 2 click
//                            Toast.makeText(this,"Unable To Delete Contact", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.menu_button3 -> {


//


//                            database.child(phoneNumber.toString()).child("Added Contacts").child("Hidden Contacts").child(arraylist2[position].phone).setValue()
//                            arraylist2=arraylist


                            databaseRef = FirebaseDatabase.getInstance().getReference("users")
                            databaseRef.child(PhoneNumber.toString()).child("Added Contacts").child("Hidden Contacts").get().addOnSuccessListener { dataSnapshot ->
                                arraylist.clear()





                                for (snapshot in dataSnapshot.children) {

                                    val contactId = snapshot.key
//                                    Toast.makeText(this,contactId, Toast.LENGTH_SHORT).show()

                                    if (contactId != null) {


                                        if(contactId==arraylist2[position].phone){
//                                            Toast.makeText(this,contactId, Toast.LENGTH_SHORT).show()
                                            databaseRef.child(PhoneNumber.toString()).child("Added Contacts").child(arraylist2[position].phone).setValue(snapshot.value)
                                            databaseRef.child(PhoneNumber.toString()).child("Added Contacts").child("Hidden Contacts").child(contactId).removeValue()



                                        }else{

                                            databaseRef.child(contactId).get()
                                                .addOnSuccessListener { contactSnapshot ->
                                                    var contact =
                                                        contactSnapshot.getValue(startuppagedata::class.java)
                                                    if (contact != null) {


//                                                Toast.makeText(
//                                                    this,
//                                                    contact.name.toString(),
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
                                                        arraylist.add(contact)
                                                        myAdapter.notifyDataSetChanged()
                                                    }}

                                        }}
                                }}
//                            database.child(phoneNumber.toString()).child("Added Contacts").child(arraylist2[position].phone).setValue("none")

//                            Toast.makeText(this,"Contact UnHide", Toast.LENGTH_SHORT).show()
                            true
                        }
                        else -> false
                    }
                }

//                 Finally show the PopupMenu
                popup.show()

                true
                // Return true to indicate the event was handled
            }

//            shimmerView2.stopShimmer()
//            shimmerView2.visibility= View.GONE
            shimmerView.stopShimmer()
            shimmerView.visibility= View.GONE



            val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

            swipeRefreshLayout.setOnRefreshListener {

                val userPhoneNumber = PhoneNumber.toString()
                val databaseRef = FirebaseDatabase.getInstance().getReference("users")
                databaseRef.child(userPhoneNumber).child("Added Contacts").child("Hidden Contacts").get().addOnSuccessListener { dataSnapshot ->
                    arraylist.clear()
                    for (snapshot in dataSnapshot.children) {
                        val contactId = snapshot.key
//                                Toast.makeText(this, contactId, Toast.LENGTH_SHORT).show()
                        if (contactId != null) {

                            databaseRef.child(contactId).get()
                                .addOnSuccessListener { contactSnapshot ->
                                    val contact =
                                        contactSnapshot.getValue(startuppagedata::class.java)
                                    if (contact != null) {
//                                                Toast.makeText(
//                                                    this,
//                                                    contact.name.toString(),
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
                                        arraylist.add(contact)
                                        myAdapter.notifyDataSetChanged()
                                    }
                                }
                        }
                    }


                    layoutManager = LinearLayoutManager(this)
                    layoutManager.reverseLayout = false
                    layoutManager.stackFromEnd = false
                    recyclerView.layoutManager = layoutManager
                    myAdapter = MyAdapter(this, arraylist)
                    recyclerView.adapter = myAdapter
                    myAdapter.notifyDataSetChanged()
                    myAdapter.setItemOnClickListner(object :
                        MyAdapter.onItemClickListner {
                        override fun onItemClick(position: Int) {
                            intent = Intent(this@Hidden_chats, chatPage::class.java)
                            intent.putExtra("Name", arraylist[position].name)



                            intent.putExtra("Photo", arraylist[position].photo)
                            intent.putExtra("Phone", arraylist[position].phone)
                            database =
                                FirebaseDatabase.getInstance().getReference("users")
                            database.child(arraylist[position].phone).get()
                                .addOnSuccessListener {
                                    var about2 = it.child("about").value.toString()
                                    intent.putExtra("About", about2)


//                                Toast.makeText(this@StartUpPage,about2,Toast.LENGTH_SHORT).show()
                                    startActivity(intent)
                                }

                        }


                    })
                    swipeRefreshLayout.isRefreshing = false
                }

            }

        }
        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        var phoneNumber = sharedPref.getString("PhoneNumber", null).toString()

        lifecycleObserver = AppLifecycleObserver(phoneNumber!!)
        lifecycle.addObserver(lifecycleObserver)
    }
}