package com.example.PDM.dtos

import com.google.gson.annotations.SerializedName

class SearchDto(val results: ResultsDto)

class ResultsDto(
    @field:SerializedName("opensearch:totalResults")
    val totalResults: Int, @field:SerializedName("gamesmatches")
    val gameMatches: GameMatchesDto
)

class GameMatchesDto(val games: Array<GameDTO>)
