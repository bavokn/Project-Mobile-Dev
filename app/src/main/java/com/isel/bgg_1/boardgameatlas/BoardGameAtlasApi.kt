package com.isel.bgg_1.boardgameatlas

import com.isel.bgg_1.boardgameatlas.dto.SearchDto
import com.isel.bgg_1.utils.AppError

interface BoardGameAtlasApi {
    fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (SearchDto) -> Unit,
        onError: (AppError) -> Unit
    )
}
