import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase

object FirebaseProvider {

    fun provideFirebaseDatabase(): FirebaseDatabase {
        val default = GoogleCredentials.getApplicationDefault()

        val options = FirebaseOptions.Builder()
            .setCredentials(default)
            .setDatabaseUrl("https://data-api-10cce.firebaseio.com/")
            .build()

        val firebaseApp = FirebaseApp.initializeApp(options)

        return FirebaseDatabase.getInstance(firebaseApp)
    }
}