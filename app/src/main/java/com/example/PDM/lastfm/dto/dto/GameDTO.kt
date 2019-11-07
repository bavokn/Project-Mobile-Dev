package com.example.PDM.dtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GameDTO(
    val id: String?,
    val image_url: String?,
    val name: String?,
    val year_published: Int?,
    val min_players: Int?,
    val max_players: Int?,
    val min_age: Int?,
    val desc: String?,
    val company: String?,
    val creators: Array<String?>,
    val rating: Double?, val url: String?)