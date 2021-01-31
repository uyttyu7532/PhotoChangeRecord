package com.uyttyu7532.photolapse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class Folder( var title: String, var photos: @RawValue ArrayList<Photo>) : Parcelable