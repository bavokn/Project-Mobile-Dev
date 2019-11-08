package com.example.PDM.lastfm


import android.content.Context
import android.util.Log
import com.example.PDM.dtos.GameDTO
import com.example.PDM.dtos.SearchDto
import com.example.PDM.utils.HttpRequests
import isel.leic.i1920.pdm.li51n.utils.AppError
import org.json.JSONArray


//TODO adjust these values
const val KEY = "Pe4QHoDYpF"
const val HOST = "https://www.boardgameatlas.com/api/"

class LastfmWebApiImpl(ctx: Context) : LastFmWebApi {
    val TAG = LastfmWebApiImpl::class.java.name
    val httpRequestes = HttpRequests(ctx)

    override fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (SearchDto) -> Unit,
        onError: (AppError) -> Unit)
    {
        var url_builder = HOST + "search?"
        when (type) {
            "name" -> url_builder += "name=$name"
            "popular" -> url_builder += "order_by=average_user_rating"
            "artist" -> url_builder += "artist=$name"
            "designer" -> url_builder += "designer=$name"
            "publisher" -> url_builder += "publisher=$name"
        }

        val url = "$url_builder&client_id=Pe4QHoDYpF&limit=30"

        // val url = String.format(" https://www.boardgameatlas.com/api/search?name=%s&client_id=Pe4QHoDYpF", name)

        Log.i(TAG, "Making Request to Uri $url")


        httpRequestes.get(url, onSuccess, onError)
    }
}