package com.isel.bgg_1.boardgameatlas

import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.DesignerSearchDTO
import com.isel.bgg_1.utils.AppError

interface BGADesignerFeatureApi {
    fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (DesignerSearchDTO) -> Unit,
        onError: (AppError) -> Unit
    )
}