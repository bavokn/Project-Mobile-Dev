package com.isel.bgg_1.boardgameatlas

import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.PublisherSearchDto
import com.isel.bgg_1.utils.AppError

interface BGAPublisherFeatureApi {
    fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (PublisherSearchDto) -> Unit,
        onError: (AppError) -> Unit
    )
}