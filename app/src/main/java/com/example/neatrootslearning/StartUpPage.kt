package com.example.neatrootslearning

import AppLifecycleObserver
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class StartUpPage : AppCompatActivity() {
    private lateinit var activityContext: Context

    var arraylist= arrayListOf<startuppagedata>()
    var arraylist2= arrayListOf<startuppagedata>()
    var filteredList = arrayListOf<startuppagedata>()
    var filteredList2 = arrayListOf<startuppagedata>()
    lateinit var database: DatabaseReference
    lateinit var editphone:TextView
    var img2=123
    lateinit var shimmerView:ShimmerFrameLayout
    lateinit var shimmerView2:ShimmerFrameLayout
    lateinit var myAdapter:MyAdapter
    lateinit var myAdapter2:MyAdapter2
    private lateinit var lifecycleObserver: AppLifecycleObserver
    var wallpaper_value=0

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up_page)
        activityContext = this
//        var imageview = findViewById<CircleImageView>(R.id.imageView)
        shimmerView=findViewById(R.id.shimmer_layout)
        shimmerView2=findViewById(R.id.shimmer_layout2)
        var wallpaper=findViewById<ImageView>(R.id.Chats_tab_wallpaper3)
        var chatsbtn=findViewById<TextView>(R.id.editphone)
        var addcontact = findViewById<ImageButton>(R.id.addContacts)
        var menubtn = findViewById<ImageButton>(R.id.menubtn)
        var count=0

        var searchbtn=findViewById<TextInputEditText>(R.id.Search)

        chatsbtn.setOnClickListener(){
            var sharedPref = getSharedPreferences("Hidden_Chats", Context.MODE_PRIVATE)
            var Recent_pass=sharedPref.getString("Hidden_chats_password",null)
            if(Recent_pass!=null){
            intent=Intent(this,Enter_password::class.java)
//            Toast.makeText(this,"Unlocked Hidden Chats",Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        else{

                intent=Intent(this,Password_Check::class.java)
//            Toast.makeText(this,"Unlocked Hidden Chats",Toast.LENGTH_SHORT).show()
                startActivity(intent)
        }}
        addcontact.setOnClickListener() {
            intent = Intent(applicationContext, AddContacts::class.java)
            startActivity(intent)
        }
        menubtn.setOnClickListener() {
            intent = Intent(this, EditingDetails::class.java)
            startActivity(intent)
        }


        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val PhoneNumber = sharedPref.getString("PhoneNumber", null)
//                Toast.makeText(this,PhoneNumber.toString(),Toast.LENGTH_SHORT).show()
        database= FirebaseDatabase.getInstance().getReference("users")
        database.child(PhoneNumber.toString()).get().addOnSuccessListener {
            intent = Intent(this, StartUpPage::class.java)
            var wallpaper2 = it.child("updated_wallpaper_chat_tab").value

            intent.putExtra("imageUri",wallpaper.toString())

            if(wallpaper2!=null) {
//                Toast.makeText(this,"7",Toast.LENGTH_SHORT).show()



// Use the imageUri to load the image

// Use the imageUri to load the image
//                Toast.makeText(this@chatPage, imageUri.toString(), Toast.LENGTH_SHORT).show()

                Picasso.get().load(wallpaper2.toString()).into(wallpaper)
            }}


        database = FirebaseDatabase.getInstance().getReference("users")

        // Initialize the ArrayList here, outside the database call
        arraylist = ArrayList()
        arraylist2 = ArrayList()
        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        var phoneNumber = sharedPref.getString("PhoneNumber", null)
        var password = sharedPref.getString("Password", null)
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(phoneNumber.toString()).get().addOnSuccessListener {
            var uriimg = it.child("photo").value
            if (uriimg != null && uriimg.toString().isNotEmpty()) {
//                Picasso.get().load(uriimg.toString()).error(R.drawable.pic2).into(imageview)
            } else {
                // Load a default image or leave the ImageView empty
//                imageview.setImageResource(R.drawable.pic2)
            }

        }
        var databaseRef = FirebaseDatabase.getInstance().getReference("users")
        databaseRef.child(phoneNumber.toString()).child("Added Contacts").get().addOnSuccessListener { dataSnapshot2 ->
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
//                            var ref2=FirebaseDatabase.getInstance().getReference("Last Mesages")
//                            ref2.addValueEventListener(object : ValueEventListener {
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    for(Data in snapshot.children){
//                                        Toast.makeText(this@StartUpPage,Data.child("Last Message text").value.toString(),Toast.LENGTH_SHORT).show()
//                                    }
//
//
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//
//                                }
//                            })

//                            databaseRef.child(phoneNumber.toString()).child("Added Contacts").addChildEventListener(object :
//                                ChildEventListener {
//                                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                                    // Handle child added
//                                    val contact = snapshot.getValue(startuppagedata::class.java)
//                                    if (contact != null) {
//                                        arraylist.add(contact)
//                                        myAdapter.notifyItemInserted(arraylist.size - 1)
//                                    }
//                                }
//
//                                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                                    // Handle child changed
//                                    val contact = snapshot.getValue(startuppagedata::class.java)
//                                    if (contact != null) {
//                                        val index = arraylist.indexOfFirst { it.phone == contact.phone }
//                                        if (index != -1) {
//                                            arraylist[index] = contact
//                                            myAdapter.notifyItemChanged(index)
//                                        }
//                                    }
//                                }
//
//                                override fun onChildRemoved(snapshot: DataSnapshot) {
//                                    // Handle child removed
//                                    val contact = snapshot.getValue(startuppagedata::class.java)
//                                    if (contact != null) {
//                                        val index = arraylist.indexOfFirst { it.phone == contact.phone }
//                                        if (index != -1) {
//                                            arraylist.removeAt(index)
//                                            myAdapter.notifyItemRemoved(index)
//                                        }
//                                    }
//                                }
//
//                                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                                    // Handle child moved
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//                                    // Handle database error
//                                }
//                            })
//
//
//
//
//
//
//
//
//
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
                    Toast.makeText(this@StartUpPage,arraylist2[position].name,Toast.LENGTH_SHORT).show()

                }})

        }
         databaseRef = FirebaseDatabase.getInstance().getReference("users")
        databaseRef.child(phoneNumber.toString()).child("Added Contacts").get().addOnSuccessListener { dataSnapshot ->
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
                        intent=Intent(this,Ads_page::class.java)
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
                            var sharedPref2 = getSharedPreferences("Wallpaper", Context.MODE_PRIVATE)
                            var wallpaper=sharedPref2.getString("PhoneNumber ${arraylist[position].phone}",null)

                                intent = Intent(this@StartUpPage, chatPage::class.java)
                                intent.putExtra("Name", arraylist[position].name)
                                intent.putExtra("Photo", arraylist[position].photo)
                                intent.putExtra("Phone", arraylist[position].phone)

                                intent.putExtra("imageUri",wallpaper.toString())
                                if(wallpaper!=null) {
                                    intent.putExtra("isuploaded2", "Yes")
                                }
                                else{
                                    intent.putExtra("isuploaded2","no")
                                }



                                sharedPref =
                                    getSharedPreferences("UserDetails", Context.MODE_PRIVATE)


//                                var database2 = FirebaseDatabase.getInstance().getReference("users")
//                                database2.child(arraylist[position].phone).get()
//                                    .addOnSuccessListener {
//                                        var addedprof = it.child("IsPhotoUploaded").value.toString()
//                                        intent.putExtra("IsPhotoUploaded", addedprof)
//
//
//
//
//                                        database =
//                                            FirebaseDatabase.getInstance().getReference("users")
//                                        database.child(arraylist[position].phone).get()
//                                            .addOnSuccessListener {
//                                                var about2 = it.child("about").value.toString()
//                                                intent.putExtra("About", about2)
//                                Toast.makeText(this@StartUpPage,about2,Toast.LENGTH_SHORT).show()
                                                startActivity(intent)
                                            }


                    })
            myAdapter.setItemOnLongClickListener { position ->
                // Get the view of the item at the position
                val v = recyclerView.layoutManager?.findViewByPosition(position)

                // Create a PopupMenu object
                val popup = PopupMenu(activityContext, v)

                // Inflate your menu resource into the PopupMenu
                popup.menuInflater.inflate(R.menu.menubtn, popup.menu)

                // Set a click listener for menu item click
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_button -> {
                            intent=Intent(this,Edit_Contact_details::class.java)

                            intent.putExtra("Name", arraylist[position].name)



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
                            // Handle option 2 click
                            database.child(phoneNumber.toString()).child("Added Contacts").child(arraylist[position].phone).removeValue()
                            Toast.makeText(this,"Contact Deleted Successfully",Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.menu_button3 -> {

//                            database.child(phoneNumber.toString()).child("Added Contacts").child("Hidden Contacts").child(arraylist2[position].phone).setValue()

                            databaseRef = FirebaseDatabase.getInstance().getReference("users")
                            databaseRef.child(phoneNumber.toString()).child("Added Contacts").get().addOnSuccessListener { dataSnapshot ->
                                arraylist.clear()
                                for (snapshot in dataSnapshot.children) {
                                    val contactId = snapshot.key
//                                Toast.makeText(this, contactId, Toast.LENGTH_SHORT).show()
                                    if (contactId != null) {
                                        if(contactId==arraylist2[position].phone){
                                            Toast.makeText(this,contactId,Toast.LENGTH_SHORT).show()
                                            database.child(phoneNumber.toString()).child("Added Contacts").child("Hidden Contacts").child(arraylist2[position].phone).setValue(snapshot.value)
                                            database.child(phoneNumber.toString()).child("Added Contacts").child(contactId).removeValue()



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

                            Toast.makeText(this,"Contact Hidden",Toast.LENGTH_SHORT).show()
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

            shimmerView2.stopShimmer()
            shimmerView2.visibility=View.GONE
            shimmerView.stopShimmer()
            shimmerView.visibility=View.GONE



                    val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

                    swipeRefreshLayout.setOnRefreshListener {
                        val userPhoneNumber = phoneNumber.toString()
                        val databaseRef = FirebaseDatabase.getInstance().getReference("users")
                        databaseRef.child(userPhoneNumber).child("Added Contacts").get().addOnSuccessListener { dataSnapshot ->
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
                                        intent = Intent(this@StartUpPage, chatPage::class.java)
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
        searchbtn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                filterContacts(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        lifecycleObserver = AppLifecycleObserver(phoneNumber!!)
        lifecycle.addObserver(lifecycleObserver)
            }
    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
            val phoneNumber = sharedPref.getString("PhoneNumber", null)
            val phone = sharedPref.getString("Phone_of_recieve", null)
            database.child(phoneNumber.toString()).child("Added Contacts").child(phone.toString()).child("isPlaying").setValue(false)
            sharedPref = getSharedPreferences("UserDetails", MODE_PRIVATE)
            val userId = sharedPref.getString("PhoneNumber", null)
            database=FirebaseDatabase.getInstance().getReference("users")
            database.child(userId.toString()).child("Is_Online").setValue("False")
            super.onBackPressed()
            return
        } else {

            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
//    override fun onDestroy() {
//        super.onDestroy()
//        val sharedPref = getSharedPreferences("UserDetails", MODE_PRIVATE)
//        val userId = sharedPref.getString("PhoneNumber", null)
//
//        // Set user status to offline when the app is closed
//        if (userId != null) {
//            val userStatusRef = database.child("users").child(userId)
//            userStatusRef.child("is_online").setValue(false)
//            userStatusRef.child("last_active").setValue(System.currentTimeMillis())
//        }
//    }



private fun filterContacts(query: String) {
    filteredList.clear()
    filteredList2.clear()
    if (query.isNotEmpty()) {
        for (contact in arraylist) {
            if (contact.name.contains(query, ignoreCase = true) || contact.phone.contains(query, ignoreCase = true)) {
                filteredList.add(contact)
            }
        }
        for (contact in arraylist2) {
            if (contact.name.contains(query, ignoreCase = true) || contact.phone.contains(query, ignoreCase = true)) {
                filteredList2.add(contact)
            }
        }
    } else {
        filteredList.addAll(arraylist)
    }
    myAdapter?.updateList(filteredList)
    myAdapter2?.updateList(filteredList2)
}

}
