import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch

private val countDownLatch = CountDownLatch(1)

fun main() {

    val firebaseDatabase = FirebaseProvider.provideFirebaseDatabase()

    val reference = firebaseDatabase.getReference("youtube_data")

    val now = LocalDateTime.now().toString()

    val newReference = reference.child("yukihanaramili").push()
    val activity = Activity("videoid", "description", now, "path", "title")

    newReference.setValue(activity) { error, ref ->
        println("complete")
        countDownLatch.countDown()
    }
    println("come here")
    countDownLatch.await()
}