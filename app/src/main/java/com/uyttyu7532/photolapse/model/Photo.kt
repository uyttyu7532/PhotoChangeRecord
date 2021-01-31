package com.uyttyu7532.photolapse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(val absolute_file_path: String) : Parcelable