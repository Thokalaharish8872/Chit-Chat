import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.database.FirebaseDatabase

class AppLifecycleObserver(private val phoneNumber: String) : LifecycleObserver {
    private val database = FirebaseDatabase.getInstance().reference

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        database.child("users").child(phoneNumber).child("status").setValue("online")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        Handler(Looper.getMainLooper()).postDelayed({
            database.child("users").child(phoneNumber).child("status").setValue("offline")
        }, 10000) // 15 seconds delay
    }
}
