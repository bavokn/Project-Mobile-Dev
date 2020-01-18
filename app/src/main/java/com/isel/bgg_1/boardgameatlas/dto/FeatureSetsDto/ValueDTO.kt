package com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto

import android.os.Parcel
import android.os.Parcelable

class ValueDTO(
    val id: String?,
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ValueDTO> {
        override fun createFromParcel(parcel: Parcel): ValueDTO {
            return ValueDTO(parcel)
        }

        override fun newArray(size: Int): Array<ValueDTO?> {
            return arrayOfNulls(size)
        }
    }


}