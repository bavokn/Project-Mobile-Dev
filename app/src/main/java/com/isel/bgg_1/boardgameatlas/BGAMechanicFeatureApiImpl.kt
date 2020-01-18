package com.isel.bgg_1.boardgameatlas

import android.content.Context
import android.util.Log
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.MechanicSearchDto
import com.isel.bgg_1.utils.AppError
import com.isel.bgg_1.utils.HttpRequests

class BGAMechanicFeatureApiImpl(ctx: Context) : BGAMechanicFeatureApi {
    private val TAG = BGAMechanicFeatureApiImpl::class.java.name
    private val httpRequests = HttpRequests(ctx)

    override fun searchValues(
        name: String,
        page: Int,
        type: String,
        onSuccess: (MechanicSearchDto) -> Unit,
        onError: (AppError) -> Unit
    ) {
        val url = "${HOST}game/mechanics?client_id=$KEY"

        Log.i(TAG, "Making Request to Uri $url")

        httpRequests.get(url, onSuccess, onError)
    }
}