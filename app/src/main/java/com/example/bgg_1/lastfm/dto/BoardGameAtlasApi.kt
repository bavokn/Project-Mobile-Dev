package com.example.bgg_1.lastfm

import com.example.bgg_1.dtos.SearchDto
import isel.leic.i1920.pdm.li51n.utils.AppError

interface BoardGameAtlasApi {
    fun searchGames(
        name: String,
        page: Int,
        type: String,
        onSuccess: (SearchDto) -> Unit,
        onError: (AppError) -> Unit
    )
}
