package com.example.PDM.lastfm

import com.example.PDM.dtos.GameDTO
import com.example.PDM.dtos.GetGamesDto
import com.example.PDM.dtos.SearchDto
import isel.leic.i1920.pdm.li51n.utils.AppError
import org.json.JSONArray

interface LastFmWebApi {
    fun searchGames(
        name: String,
        page: Int,
        onSuccess: (SearchDto) -> Unit,
        onError: (AppError) -> Unit
    )
}
