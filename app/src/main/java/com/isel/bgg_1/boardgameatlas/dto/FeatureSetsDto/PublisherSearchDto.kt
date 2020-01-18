package com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto

import com.google.gson.annotations.SerializedName


class PublisherSearchDto(val results: PublisherResultsDto)

class PublisherResultsDto(
    @field:SerializedName("matches")
    val gameMatches: PublisherMatchesDTO
)

class PublisherMatchesDTO(val games: Array<PublisherDTO>)
