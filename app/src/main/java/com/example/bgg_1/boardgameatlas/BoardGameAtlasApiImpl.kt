package com.example.bgg_1.boardgameatlas


import android.content.Context
import android.util.Log
import com.example.bgg_1.boardgameatlas.dto.SearchDto
import com.example.bgg_1.utils.AppError
import com.example.bgg_1.utils.HttpRequests
import java.net.URLEncoder


const val KEY = "Pe4QHoDYpF"
const val HOST = "https://www.boardgameatlas.com/api/"

class BoardGameAtlasApiImpl(ctx: Context) : BoardGameAtlasApi {
    private val TAG = BoardGameAtlasApiImpl::class.java.name
    private val httpRequests = HttpRequests(ctx)

    override fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (SearchDto) -> Unit,
        onError: (AppError) -> Unit
    ) {
        val pageSkip = page * 30
        var urlBuilder = HOST + "search?"
        // val name_encoded = URLEncoder.encode(name, "utf-8")
        when (type) {
            "name" -> urlBuilder +="name=" + URLEncoder.encode(name, "utf-8") + "&skip=$pageSkip&fuzzy_match=true"
            "popular" -> urlBuilder += "order_by=average_user_rating"
            "artist" -> urlBuilder += "artist=" + URLEncoder.encode(name, "utf-8")
            "designer" -> urlBuilder += "designer=" + URLEncoder.encode(name, "utf-8")
            "publisher" -> urlBuilder += "publisher=" + URLEncoder.encode(name, "utf-8")
        }

        val url = "$urlBuilder&client_id=$KEY&limit=30"

        Log.i(TAG, "Making Request to Uri $url")

        httpRequests.get(url, onSuccess, onError)
    }
}