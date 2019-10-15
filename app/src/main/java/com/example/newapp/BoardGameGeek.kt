import android.util.JsonReader
import android.util.Log
import com.github.salomonbrys.kotson.jsonArray
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import org.json.JSONObject
import kotlin.math.log

data class BoardGame(val id: Int, val name: String?, val yearpublished: Int?)
data class BoardGames(val games: List<BoardGame>?)

class BoardGameGeek {
    companion object {
        private val BASE_URL = "https://www.boardgameatlas.com/api/"
        private val client_id =  "Pe4QHoDYpF"
    }

    fun searchGames(query: String): BoardGames {
        val response = URL(BASE_URL + "search?name=" + query + "&client_id=" + client_id)
        Log.d("test", response.readText())

        return BoardGames(null)
    }

}