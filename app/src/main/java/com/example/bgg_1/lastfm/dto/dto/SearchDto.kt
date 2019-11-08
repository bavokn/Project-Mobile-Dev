package com.example.bgg_1.dtos

import com.google.gson.annotations.SerializedName


class SearchDto(val results: ResultsDto)

class ResultsDto(
    @field:SerializedName("matches")
    val gameMatches: GameMatchesDTO
)

class GameMatchesDTO(val games: Array<GameDTO>)
