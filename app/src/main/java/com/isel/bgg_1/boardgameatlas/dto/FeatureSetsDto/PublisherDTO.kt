package com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto

import android.os.Parcel
import android.os.Parcelable

class PublisherDTO(
    val publishers: ArrayList<String?>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList() as ArrayList<String?>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(publishers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PublisherDTO> {
        override fun createFromParcel(parcel: Parcel): PublisherDTO {
            return PublisherDTO(parcel)
        }

        override fun newArray(size: Int): Array<PublisherDTO?> {
            return arrayOfNulls(size)
        }
    }
}
