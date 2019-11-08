package com.example.bgg_1.lastfm


import android.content.Context
import android.util.Log
import com.example.bgg_1.dtos.SearchDto
import com.example.bgg_1.utils.HttpRequests
import isel.leic.i1920.pdm.li51n.utils.AppError


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

        val url = "$urlBuilder&client_id=$KEY&limit=30"

        Log.i(TAG, "Making Request to Uri $url")

        httpRequests.get(url, onSuccess, onError)
    }
}