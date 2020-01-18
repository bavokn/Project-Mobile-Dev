package com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto

import com.google.gson.annotations.SerializedName


class DesignerSearchDTO(val results: DesignerResultsDto)

class DesignerResultsDto(
    @field:SerializedName("matches")
    val gameMatches: DesignerMatchesDTO
)

class DesignerMatchesDTO(val games: Array<DesignerDTO>)
