
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neatrootslearning.R
import com.example.neatrootslearning.chatPage
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(
    private val context: Activity,
    private val messages: MutableList<chatPage.Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_NORMAL_MESSAGE = 1
        private const val VIEW_TYPE_SYSTEM_MESSAGE = 2
    }
    interface onItemLongClick {
        fun onItemLongClick(position: Int)
    }
    var onItemLongClickListener: ((Int) -> Boolean)? = null
//    private var onItemLongClickListener: OnItemLongClickListener? = null

    fun setItemOnLongClickListener(listener2: (Int) -> Boolean) {
        onItemLongClickListener = listener2
    }



    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text1: TextView? = null
        var text2: TextView? = null
        var profilesend: ImageView? = null
        var profilerecieve: ImageView? = null
        var text: TextView? = null
        var messageTime: TextView? = null
        var receivrlayout:ConstraintLayout?=null
        var receivedmsgtime:TextView?=null
        var senderlayout:ConstraintLayout?=null

        init {
            if (itemView.findViewById<TextView>(R.id.sendertext) != null) {
                senderlayout=itemView.findViewById(R.id.sender_const_layout)
                receivedmsgtime=itemView.findViewById(R.id.receiver_time)
                receivrlayout=itemView.findViewById(R.id.receiver_const_layout)
                text1 = itemView.findViewById(R.id.sendertext)
                text2 = itemView.findViewById(R.id.receivertext)
                profilesend = itemView.findViewById(R.id.senderprofile)
                profilerecieve = itemView.findViewById(R.id.receiverprofile)
                messageTime = itemView.findViewById(R.id.sender_time)
            } else {
                text = itemView.findViewById(R.id.sendertext1)
            }

            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(adapterPosition)
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSystemMessage) {
            VIEW_TYPE_SYSTEM_MESSAGE
        } else {
            VIEW_TYPE_NORMAL_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SYSTEM_MESSAGE -> {
                val view = inflater.inflate(R.layout.middle_message_layout, parent, false)
                MessageViewHolder(view)
            }
            VIEW_TYPE_NORMAL_MESSAGE -> {
                val view = inflater.inflate(R.layout.item_message, parent, false)
                MessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messages[position]


        if (holder is MessageViewHolder) {
            val sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
            val phoneNumber = sharedPreferences.getString("PhoneNumber", "null")
            val receiverPhoneNumber = sharedPreferences.getString("Phone_of_recieve", null)
            with(sharedPreferences.edit()) {
                putString("Last Message for $receiverPhoneNumber", message.text)
                apply()

            }

            if (message.isSystemMessage) {
                holder.text?.text = message.text
                holder.text?.gravity = Gravity.CENTER
                holder.text?.setBackgroundResource(R.drawable.middle_message_background)
                holder.text1?.visibility = View.GONE
                holder.text2?.visibility = View.GONE
                holder.profilesend?.visibility = View.GONE
                holder.profilerecieve?.visibility = View.GONE
            } else {
                if (message.currentUserId == phoneNumber) {
                    holder.text1?.alpha = 1f
                    holder.senderlayout?.alpha=1f
                    holder.profilesend?.alpha = 1f
                    holder.profilesend?.visibility = View.VISIBLE
                    holder.profilerecieve?.visibility = View.GONE
                    holder.text1?.visibility = View.VISIBLE
                    holder.text2?.visibility = View.GONE

                    holder.profilerecieve?.let {
                        Glide.with(context)
                            .load(message.senderimage)
                            .error(R.drawable.no_profile_pic)
                            .into(it)
                    }
                    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val time = sdf.format(Date(message.timestamp))
                    holder.messageTime?.text = time

                    holder.text1?.gravity = Gravity.END
                    holder.text1?.text = message.text
                } else {
                    val database = FirebaseDatabase.getInstance().getReference("users")
                    database.child(receiverPhoneNumber.toString()).get().addOnSuccessListener {
                        val photo = it.child("photo").value.toString()
                        holder.profilerecieve?.alpha = 1f
                        holder.receivrlayout?.alpha=1f
                        holder.text2?.alpha = 1f
                        holder.profilesend?.visibility = View.GONE
                        holder.profilerecieve?.visibility = View.VISIBLE
                        holder.text1?.visibility = View.GONE
                        holder.text2?.visibility = View.VISIBLE
                        holder.profilerecieve?.let { it1 ->
                            Glide.with(context)
                                .load(photo)
                                .error(R.drawable.no_profile_pic)
                                .into(it1)
                        }

                        holder.text2?.gravity = Gravity.END
                        holder.text2?.text = message.text
                        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        val time = sdf.format(Date(message.timestamp))
                        holder.receivedmsgtime?.text = time
                    }
                }
            }
        }
    }

    override fun getItemCount() = messages.size
}
