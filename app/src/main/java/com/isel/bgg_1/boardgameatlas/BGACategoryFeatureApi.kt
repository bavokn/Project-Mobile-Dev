package com.isel.bgg_1.boardgameatlas

import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.CategorySearchDto
import com.isel.bgg_1.utils.AppError

interface BGACategoryFeatureApi {
    fun searchValues(
        name: String,
        page: Int,
        type: String,
        onSuccess: (CategorySearchDto) -> Unit,
        onError: (AppError) -> Unit
    )
}