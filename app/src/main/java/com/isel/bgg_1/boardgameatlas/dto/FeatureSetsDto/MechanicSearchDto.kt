package com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto

import com.google.gson.annotations.SerializedName


class MechanicSearchDto(val results: MechanicResultsDto)

class MechanicResultsDto(
    @field:SerializedName("matches")
    val gameMatches: MechanicMatchesDTO
)

class MechanicMatchesDTO(val mechanics: Array<ValueDTO>)
