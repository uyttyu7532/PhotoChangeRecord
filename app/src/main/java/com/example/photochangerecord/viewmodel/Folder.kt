package com.example.photochangerecord.viewmodel

import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class Folder( var title: String, var photos: @RawValue ArrayList<Photo>)