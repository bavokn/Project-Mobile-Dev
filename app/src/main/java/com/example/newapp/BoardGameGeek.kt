import android.util.Xml
import kotlinx.coroutines.handleCoroutineException
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.URL
import java.util.*

data class BoardGame(val id: Int, val name: String?, val yearpublished: Int?)
data class BoardGames(val games: List<BoardGame>)

class BoardGameGeek {
    companion object {
        private val BASE_URL = "https://www.boardgameatlas.com/api/"
        private val client_id =  "Pe4QHoDYpF"
        private val ns: String? = null
    }

    fun searchGames(query: String): BoardGames {
        val response = URL(BASE_URL + "search?name=" + query + "&client_id=" + client_id)
        val parser = createParser(response.readText().byteInputStream())
        return readGames(parser)
    }

    private fun createParser(`in`: InputStream): XmlPullParser {
        try {
            val parser = Xml.newPullParser()
            parser.setInput(`in`, null)
            parser.next()
            return parser
        } finally {
            `in`.close()
        }
    }

    private fun readGames(parser: XmlPullParser): BoardGames {
        val games = ArrayList<BoardGame>()
        parser.require(XmlPullParser.START_TAG, ns, "boardgames")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "boardgame") {
                games.add(readGame(parser))
            } else {
                skip(parser)
            }
        }
        return BoardGames(games)
    }

    private fun readGame(parser: XmlPullParser): BoardGame {
        parser.require(XmlPullParser.START_TAG, ns, "boardgame")
        val id: Int = readId(parser)
        var name: String? = null
        var yearpublished: Int? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val tag = parser.name
            if (tag == "name") {
                name = readName(parser)
            } else if (tag == "yearpublished") {
                yearpublished = readYearpublished(parser)
            } else {
                skip(parser)
            }
        }
        return BoardGame(id, name, yearpublished)
    }

    private fun readName(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "name")
        val name = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "name")
        return name
    }

    private fun readYearpublished(parser: XmlPullParser): Int {
        parser.require(XmlPullParser.START_TAG, ns, "yearpublished")
        val yearpublished = readText(parser).toInt()
        parser.require(XmlPullParser.END_TAG, ns, "yearpublished")
        return yearpublished
    }

    private fun readId(parser: XmlPullParser): Int {
        var id = parser.getAttributeValue(null, "objectid").toInt()
        return id
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}