package com.example.PDM.dtos

import android.os.Parcel
import android.os.Parcelable
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
    val rating: Double?, val url: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArray() as Array<String?>,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image_url)
        parcel.writeString(name)
        parcel.writeValue(year_published)
        parcel.writeValue(min_players)
        parcel.writeValue(max_players)
        parcel.writeValue(min_age)
        parcel.writeString(desc)
        parcel.writeString(company)
        parcel.writeStringArray(creators)
        parcel.writeValue(rating)
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