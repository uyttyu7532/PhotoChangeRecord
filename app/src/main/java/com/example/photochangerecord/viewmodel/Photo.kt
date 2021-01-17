package com.example.photochangerecord.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(val absolute_file_path: String) : Parcelable