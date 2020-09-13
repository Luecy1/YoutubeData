import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import java.util.concurrent.CountDownLatch

fun main() {
    val youtube = YouTube.Builder(NetHttpTransport(), JacksonFactory()) {}
        .setApplicationName("youtube-cmdline-search-sample")
        .build()

    val activityList = youtube.Activities().list("id,snippet,contentDetails")
    activityList.channelId = "UCFKOVgVbGmX65RxO3EtH3iw"
    activityList.key = Properties["youtube.apikey"]
    activityList.maxResults = 50

    val activityListResponse = activityList.execute()

    val items = activityListResponse.items

    val list = items.map { activity ->
        activity.contentDetails.upload.videoId to Activity(
            activity.contentDetails.upload.videoId,
            activity.snippet.description,
            activity.snippet.publishedAt.toString(),
            activity.snippet.thumbnails.high.url,
            activity.snippet.title,
        )
    }.toMap()

    println(list)

    // data insert

    val firebaseDatabase = FirebaseProvider.provideFirebaseDatabase()

    val reference = firebaseDatabase.getReference("youtube_data")

    val newReference = reference.child("yukihanaramili").push()
//    val activity = Activity("videoid", "description", now, "path", "title")

    val countDownLatch = CountDownLatch(1)

//    for (activity in list) {
//        reference.child("yukihanaramili").push().setValue(activity) { error, ref ->
//            println("complete")
//            countDownLatch.countDown()
//        }
//    }

    reference.child("yukihanaramili").setValue(list) { error, ref ->
        println("complete")
        countDownLatch.countDown()
    }

    println("come here")
    countDownLatch.await()

}