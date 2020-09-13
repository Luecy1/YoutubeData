import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CountDownLatch

fun main() {

    val countDownLatch = CountDownLatch(1)

    val firebaseDatabase = FirebaseProvider.provideFirebaseDatabase()

    val reference = firebaseDatabase.getReference("youtube_data")

    reference.child("yukihanaramili").addListenerForSingleValueEvent(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {

            for (child in snapshot.children) {
                val activity = activityFormSnapshot(child)
                println(activity)
            }

            countDownLatch.countDown()
        }

        override fun onCancelled(error: DatabaseError) {
            error.toException().printStackTrace()
        }
    })

    countDownLatch.await()
}

fun activityFormSnapshot(snapshot: DataSnapshot): Activity {


//    return snapshot.getValue(Activity::class.java)

    return Activity(
        videoId = snapshot.child("videoId").value as String,
        description = snapshot.child("description").value as String,
        publishedAt = snapshot.child("description").value as String,
        thumbnail = snapshot.child("description").value as String,
        title = snapshot.child("description").value as String,
    )
}