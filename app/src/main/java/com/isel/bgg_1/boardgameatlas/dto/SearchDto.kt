package com.isel.bgg_1.boardgameatlas.dto

import com.google.gson.annotations.SerializedName


class SearchDto(val results: ResultsDto)

class ResultsDto(
    @field:SerializedName("matches")
    val gameMatches: GameMatchesDTO
)

class GameMatchesDTO(val games: Array<GameDTO>)
