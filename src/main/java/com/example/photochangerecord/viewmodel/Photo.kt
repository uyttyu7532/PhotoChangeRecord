package com.example.photochangerecord.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(val resourceID: Int, val date: String) : Parcelable