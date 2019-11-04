import android.os.Parcel
import Interfaces.BoardGameInterface
import android.os.Parcelable
import android.util.Log
import com.fasterxml.jackson.module.kotlin.*
import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONObject
import com.android.volley.RequestQueue
import com.android.volley.toolbox.*
import java.io.File
import org.json.JSONArray

data class BoardGame(
    val id: String?, val image_url: String?, val name: String?,
    val year_published: Int?, val min_players: Int?, val max_players: Int?,
    val min_age: Int?, val desc: String?, val company: String?,
    val creators: Array<String?>, val rating: Double?, val url: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArray() as Array<String?>,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image_url)
        parcel.writeString(name)
        parcel.writeValue(year_published)
        parcel.writeValue(min_players)
        parcel.writeValue(max_players)
        parcel.writeValue(min_age)
        parcel.writeString(desc)
        parcel.writeString(company)
        parcel.writeStringArray(creators)
        parcel.writeValue(rating)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoardGame

        if (!creators.contentEquals(other.creators)) return false

        return true
    }

    override fun hashCode(): Int {
        return creators.contentHashCode()
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

class BoardGameGeek : BoardGameInterface {
    // val boardGames =  mutableBoardGames(ListOf())
    val boardGameList = mutableListOf<BoardGame>()
    var boardGames : BoardGames? = null

    companion object {
        val mapper = jacksonObjectMapper()

        private val BASE_URL = "https://www.boardgameatlas.com/api/"
        private val client_id =  "Pe4QHoDYpF"
        // Instantiate the cache
        val cacheDir = File("/localStorage")
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache, network)
    }

    @Override
    override fun onCallback(items: JSONArray) : BoardGames{
        for (i in 0 until items.length()){
            val item = items[i] as JSONObject
            Log.e("item:", item.toString())
            val designers =  item.getJSONArray("designers")
            val developers = item.getJSONArray("developers")
            val artists = item.getJSONArray("artists")

            var creators = Array(designers.length()) {
                designers.optString(it)
            }
             creators += Array(developers.length()) {
                developers.optString(it)
            }
            creators += Array(artists.length()) {
                artists.optString(it)
            }

            val currentBoardGame = BoardGame( item.optString("id"),
                item.optString("image_url"),
                item.optString("name"),
                item.optInt("year_published"),
                item.optInt("min_players"),
                item.optInt("max_players"),
                item.optInt("min_age"),
                item.optString("description"),
                item.optString("primary_publisher"),
                creators,
                item.optDouble("average_user_rating"),
                item.optString("rules_url"))
            boardGameList += currentBoardGame
        }

        boardGames = BoardGames(boardGameList)

        return boardGames as BoardGames
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