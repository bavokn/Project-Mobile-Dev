package com.isel.bgg_1.boardgameatlas

import android.content.Context
import android.util.Log
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.PublisherSearchDto
import com.isel.bgg_1.utils.AppError
import com.isel.bgg_1.utils.HttpRequests
import java.net.URLEncoder

class BGAPublisherFeatureApiImpl(ctx: Context) : BGAPublisherFeatureApi {
    private val TAG = BGAPublisherFeatureApiImpl::class.java.name
    private val httpRequests = HttpRequests(ctx)

    override fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (PublisherSearchDto) -> Unit,
        onError: (AppError) -> Unit
    ) {
        val url = HOST + "search?publisher=&client_id=$KEY"

        Log.i(TAG, "Making Request to Uri $url")

        httpRequests.get(url, onSuccess, onError)
    }
}