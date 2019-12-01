package com.example.bgg_1.boardgameatlas


import android.content.Context
import android.util.Log
import com.example.bgg_1.boardgameatlas.dto.SearchDto
import com.example.bgg_1.utils.AppError
import com.example.bgg_1.utils.HttpRequests


//TODO adjust these values
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
        var urlBuilder = HOST + "search?"
        when (type) {
            "name" -> urlBuilder += "name=$name"
            "popular" -> urlBuilder += "order_by=average_user_rating"
            "artist" -> urlBuilder += "artist=$name"
            "designer" -> urlBuilder += "designer=$name"
            "publisher" -> urlBuilder += "publisher=$name"
        }

        val pageSkip = page * 30
        val url = "$urlBuilder&client_id=$KEY&limit=30&skip=$pageSkip"

        Log.i(TAG, "Making Request to Uri $url")

        httpRequests.get(url, onSuccess, onError)
    }
}