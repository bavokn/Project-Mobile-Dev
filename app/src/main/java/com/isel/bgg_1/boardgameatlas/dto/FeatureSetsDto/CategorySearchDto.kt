package com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto

import com.google.gson.annotations.SerializedName


class CategorySearchDto(val results: CategoryResultsDto)

class CategoryResultsDto(
    @field:SerializedName("matches")
    val gameMatches: CategoryMatchesDTO
)

class CategoryMatchesDTO(val categories: Array<ValueDTO>)
