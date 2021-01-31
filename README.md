# PhotoLapse

## Introduction

This is an android application that records photos from the same location.

## Open-source libraries

- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
- JetPack
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.

- Coil - loading images.
- RangeSeekBar - SeekBar what supports single, range, steps.



## Permissions

On Android versions prior to Android 7.0, PhotoLapse requires the following permissions:
- android.permission.CAMERA

## Development Environment

- Android Studio @4.1.1
- Kotlin @1.4.21

## Application Version

- minSdkVersion : 24
- targetSdkVersion : 29

## screenshot

<img src = "./image/gif.gif" width="400px">
