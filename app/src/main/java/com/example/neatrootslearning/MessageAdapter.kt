
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neatrootslearning.R
import com.example.neatrootslearning.chatPage
import com.google.firebase.database.FirebaseDatabase

class MessageAdapter(var context: Activity, private val messages: List<chatPage.Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text1: TextView = itemView.findViewById(R.id.sendertext)
        var text2:TextView=itemView.findViewById(R.id.receivertext)
        var profilesend:ImageView=itemView.findViewById(R.id.senderprofile)
        var profilerecieve:ImageView=itemView.findViewById(R.id.receiverprofile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val message = messages[position]

            val sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
            val Phonenumber = sharedPreferences.getString("PhoneNumber", "null")
            var p=sharedPreferences.getString("Phone_of_recieve",null)

//            Toast.makeText(context,message.phonenum, Toast.LENGTH_SHORT).show()
//        var database=FirebaseDatabase.getInstance().getReference("users")
//        database.child(message.phonenum).get().addOnSuccessListener {
//            var photo=it.child("photo").value.toString()

//        Toast.makeText(context,photo,Toast.LENGTH_SHORT).show()
            // Use the value


            if (message.currentUserId == Phonenumber) {

                holder.text1.alpha=1f
                holder.profilesend.alpha=1f
                holder.profilesend.visibility = View.VISIBLE
                holder.profilerecieve.visibility = View.GONE
                holder.text1.visibility = View.VISIBLE
                holder.text2.visibility = View.GONE
                Glide.with(context)
                    .load(message.senderimage)
                    .error(R.drawable.no_profile_pic)// Use the stored URI here
                    .into(holder.profilesend)
                // This message was sent by the current user. Align it to the right.
                holder.text1.gravity = Gravity.END
                holder.text1.text = message.text

            } else {
//                Toast.makeText(context,p,Toast.LENGTH_SHORT).show()
                var database=FirebaseDatabase.getInstance().getReference("users")
                database.child(p.toString()).get().addOnSuccessListener {
                    var photo=it.child("photo").value.toString()

                holder.profilerecieve.alpha=1f
                holder.text2.alpha=1f
                holder.profilesend.visibility = View.GONE
                holder.profilerecieve.visibility = View.VISIBLE
                holder.text1.visibility = View.GONE
                holder.text2.visibility = View.VISIBLE
                Glide.with(context)
                    .load(photo)
                    .error(R.drawable.no_profile_pic)// Use the stored URI here
                    .into(holder.profilerecieve)
                // This message was sent by another user. Align it to the left.
                holder.text2.gravity = Gravity.END
                holder.text2.text = message.text
            }
        }

        }



    override fun getItemCount() = messages.size
}