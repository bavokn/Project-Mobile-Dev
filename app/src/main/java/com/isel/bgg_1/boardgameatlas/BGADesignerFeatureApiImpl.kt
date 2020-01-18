package com.isel.bgg_1.boardgameatlas

import android.content.Context
import android.util.Log
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.DesignerSearchDTO
import com.isel.bgg_1.utils.AppError
import com.isel.bgg_1.utils.HttpRequests

class BGADesignerFeatureApiImpl(ctx: Context) : BGADesignerFeatureApi {
    private val TAG = BGADesignerFeatureApiImpl::class.java.name
    private val httpRequests = HttpRequests(ctx)

    override fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (DesignerSearchDTO) -> Unit,
        onError: (AppError) -> Unit
    ) {
        val url = HOST + "search?designer=&client_id=$KEY"

        Log.i(TAG, "Making Request to Uri $url")

        httpRequests.get(url, onSuccess, onError)
    }
}