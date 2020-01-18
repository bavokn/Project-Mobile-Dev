package com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto

import android.os.Parcel
import android.os.Parcelable

class DesignerDTO(
    val designers: ArrayList<String?>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList() as ArrayList<String?>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(designers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DesignerDTO> {
        override fun createFromParcel(parcel: Parcel): DesignerDTO {
            return DesignerDTO(parcel)
        }

        override fun newArray(size: Int): Array<DesignerDTO?> {
            return arrayOfNulls(size)
        }
    }
}
