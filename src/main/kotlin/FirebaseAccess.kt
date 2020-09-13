import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch

val countDownLatch = CountDownLatch(1)

fun main() {

    val default = GoogleCredentials.getApplicationDefault()

    val options = FirebaseOptions.Builder()
        .setCredentials(default)
        .setDatabaseUrl("https://data-api-10cce.firebaseio.com/")
        .build()

    val firebaseApp = FirebaseApp.initializeApp(options)

    val firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp)

    val reference = firebaseDatabase.getReference("youtube_data")

    val now = LocalDateTime.now().toString()

    val newReference = reference.child("yukihanaramili").push()
    val activity = Activity("videoid", "description", now, "path", "title")

    newReference.setValue(activity) { error, ref ->
        println("complete")
        countDownLatch.countDown()
    }


    countDownLatch.await()
}