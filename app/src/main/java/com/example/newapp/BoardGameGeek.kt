import Interfaces.BoardGameInterface
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.fasterxml.jackson.module.kotlin.*
import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONObject
import com.android.volley.RequestQueue
import com.android.volley.toolbox.*
import java.io.File
import android.text.method.ScrollingMovementMethod
import com.google.gson.JsonArray
import org.json.JSONArray

data class BoardGame(val id: String,val image_url : String? ,val name: String?, val yearpublished: Int?, val min_players : Int?,
                     val max_players : Int?, val min_age : Int?, val desc : String?, val company : String?, val creators : Array<String?>, val rating : Double?, val url : String?)
data class BoardGames(var games: List<BoardGame>?)

class BoardGameGeek(): BoardGameInterface {
    companion object {
        private var boardGames =  BoardGames(null)

        val mapper = jacksonObjectMapper()

        private val BASE_URL = "https://www.boardgameatlas.com/api/"
        private val client_id =  "Pe4QHoDYpF"
        // Instantiate the cache
        val cacheDir = File("/local")
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache, network)
    }

    @Override
    override fun onCallback(items: JSONArray){
        for (i in 0 until items.length()){
            val item = items[i] as JSONObject
            val creators =  item.getJSONArray("designers")

            val list = Array(creators.length()) {
                creators.optString(it)
            }
            boardGames.games?.plus(BoardGame(
                item.optString("id"),
                item.optString("image_url"),
                item.optString("name"),
                item.optInt("year_published"),
                item.optInt("min_players"),
                item.optInt("max_players"),
                item.optInt("min_age"),
                item.optString("description"),
                item.optString("primary_publisher"),
                list,
                item.optDouble("average_user_rating"),
                item.optString("rules_url")
            ))
        }
        //TODO pass the boardgames to the adapter
        Log.d("games" , boardGames.games.toString())
    }

    fun fetchJsonResponse(query: String)  {
        val url = String.format(BASE_URL + "search?name=" + query + "&client_id=" + client_id)

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                val items : JSONArray = JSONObject(response).get("games") as JSONArray
                //add all the games here to the list of boardgames and return this object
                onCallback(items)
            },
            Response.ErrorListener { Log.d("error", "That didn't work!") })

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
        requestQueue.start()
    }
}