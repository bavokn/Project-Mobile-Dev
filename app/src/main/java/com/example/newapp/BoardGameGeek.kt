import android.os.Parcel
import android.os.Parcelable
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
import org.json.JSONArray

data class BoardGame(val id: String?, val name: String?, val yearpublished: Int?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeValue(yearpublished)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardGame> {
        override fun createFromParcel(parcel: Parcel): BoardGame {
            return BoardGame(parcel)
        }

        override fun newArray(size: Int): Array<BoardGame?> {
            return arrayOfNulls(size)
        }
    }
}

data class BoardGames(var games: List<BoardGame>?)

class BoardGameGeek {
    companion object {
        private var bg = BoardGame("1","test",2014)
        private var bgs =  arrayListOf<BoardGame>(bg)
        private var boardGames =  BoardGames(bgs)

        val mapper = jacksonObjectMapper()

        private val BASE_URL = "https://www.boardgameatlas.com/api/"
        private val client_id =  "Pe4QHoDYpF"
        // Instantiate the cache
        val cacheDir = File("")
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache, network)
    }

    fun fetchJsonResponse(query: String, view : TextView) : BoardGames  {
        val url = String.format(BASE_URL + "search?name=" + query + "&client_id=" + client_id)

        // Request a string response from the provided URL.
        var jsonObject = JSONObject()
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                val items : JSONArray = JSONObject(response).get("games") as JSONArray
                //add all the games here to the list of boardgames and return this object
                for (i in 0 until items.length()){
                   val item = items[i] as JSONObject
                    val id = item.get("id") as String
                    val name = item.get("name") as String
                    Log.d("id + name" , id + " -  " + name)
                    boardGames.games?.plus(BoardGame(id,name,2012))
                }
                view.setMovementMethod(ScrollingMovementMethod())
            },
            Response.ErrorListener { Log.d("error", "That didn't work!") })

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
        requestQueue.start()
        return boardGames
    }
}