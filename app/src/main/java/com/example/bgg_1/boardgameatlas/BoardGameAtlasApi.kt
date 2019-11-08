package com.example.bgg_1.boardgameatlas

import com.example.bgg_1.boardgameatlas.dto.SearchDto
import com.example.bgg_1.utils.AppError

interface BoardGameAtlasApi {
    fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (SearchDto) -> Unit,
        onError: (AppError) -> Unit
    )
}
