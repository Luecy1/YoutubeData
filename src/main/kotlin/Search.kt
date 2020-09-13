import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ResourceId
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun main() {
    Search().main()
}

class Search {

    /** Global instance of the HTTP transport.  */
    private val HTTP_TRANSPORT: HttpTransport = NetHttpTransport()

    /** Global instance of the JSON factory.  */
    private val JSON_FACTORY: JsonFactory = JacksonFactory()

    /** Global instance of the max number of videos we want returned (50 = upper limit per page).  */
    private val NUMBER_OF_VIDEOS_RETURNED: Long = 25

    /** Global instance of Youtube object to make all API requests.  */

    fun main() {

        try {

            val youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY) {}
                .setApplicationName("youtube-cmdline-search-sample")
                .build()

            val activityList = youtube.Activities().list("id,snippet,contentDetails")
            activityList.channelId = "UCFKOVgVbGmX65RxO3EtH3iw"
            activityList.key = Properties["youtube.apikey"]
            activityList.maxResults = 50

            val activityListResponse = activityList.execute()

            val items = activityListResponse.items

            val list = items.map { activity ->
                Activity(
                    activity.contentDetails.upload.videoId,
                    activity.snippet.description,
                    activity.snippet.publishedAt.toString(),
                    activity.snippet.thumbnails.high.url,
                    activity.snippet.title,
                )
            }

            list.forEach { println(it) }

        } catch (e: GoogleJsonResponseException) {
            System.err.println(
                "There was a service error: " + e.details.code + " : " + e.details.message
            )
            e.printStackTrace()
        } catch (e: IOException) {
            System.err.println("There was an IO error: " + e.cause + " : " + e.message)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private val inputQuery: String
        get() {
            var inputQuery: String
            print("Please enter a search term: ")
            val bReader = BufferedReader(InputStreamReader(System.`in`))
            inputQuery = bReader.readLine()
            if (inputQuery.isEmpty()) {
                // If nothing is entered, defaults to "YouTube Developers Live."
                inputQuery = "YouTube Developers Live"
            }
            return inputQuery
        }

    private fun prettyPrint(iteratorSearchResults: MutableIterator<SearchResult>, query: String) {
        println("\n=============================================================")
        println("   First $NUMBER_OF_VIDEOS_RETURNED videos for search on \"$query\".")
        println("=============================================================\n")
        if (!iteratorSearchResults.hasNext()) {
            println(" There aren't any results for your query.")
        }

        while (iteratorSearchResults.hasNext()) {
            val singleVideo: SearchResult = iteratorSearchResults.next()
            val rId: ResourceId = singleVideo.id

            // Double checks the kind is video.
            if ((rId.kind == "youtube#video")) {

                val thumbnail: Thumbnail = singleVideo.snippet.thumbnails["default"] as Thumbnail
                println(" Video Id" + rId.videoId)
                println(" Title: " + singleVideo.snippet.title)
                println(" Thumbnail: " + thumbnail.url)
                println("\n-------------------------------------------------------------\n")
            }
        }
    }
}

