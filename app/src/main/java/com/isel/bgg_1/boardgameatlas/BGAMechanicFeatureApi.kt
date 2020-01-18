package com.isel.bgg_1.boardgameatlas

import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.MechanicSearchDto
import com.isel.bgg_1.utils.AppError

interface BGAMechanicFeatureApi {
    fun searchValues(
        name: String,
        page: Int,
        type: String,
        onSuccess: (MechanicSearchDto) -> Unit,
        onError: (AppError) -> Unit
    )
}