import java.util.Properties as SystemProperties

object Properties {

    var data: Map<String, String>

    init {
        val reader = Properties::class.java.classLoader.getResourceAsStream("settings.properties")

        val systemProperties = SystemProperties()

        systemProperties.load(reader)

        data = systemProperties.entries.map { (key, value) ->
            (key as String) to (value as String)
        }.toMap()
    }


    operator fun get(key: String): String? {
        return data[key]
    }
}