package com.example.bgg_1.boardgameatlas.dto

import android.os.Parcel
import android.os.Parcelable

class GameDTO(
    val id: String?,
    val image_url: String?,
    val name: String?,
    val year_published: Int,
    val min_players: Int,
    val max_players: Int,
    val min_age: Int,
    val description: String?,
    val primary_publisher: String?,
    val designers: ArrayList<String?>,
    val artists: ArrayList<String?>,
    val average_user_rating: Double,
    val url: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList() as ArrayList<String?>,
        parcel.createStringArrayList() as ArrayList<String?>,
        parcel.readValue(Double::class.java.classLoader) as Double,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image_url)
        parcel.writeString(name)
        parcel.writeValue(year_published)
        parcel.writeValue(min_players)
        parcel.writeValue(max_players)
        parcel.writeValue(min_age)
        parcel.writeString(description)
        parcel.writeString(primary_publisher)
        parcel.writeStringList(designers)
        parcel.writeStringList(artists)
        parcel.writeValue(average_user_rating)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameDTO> {
        override fun createFromParcel(parcel: Parcel): GameDTO {
            return GameDTO(parcel)
        }

        override fun newArray(size: Int): Array<GameDTO?> {
            return arrayOfNulls(size)
        }
    }


}