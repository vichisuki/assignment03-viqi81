# COS30017 - Assignment 3 - Keeping Track

This repository contains my submission for Assignment 3 of COS30017 - Software Development for Mobile Devices at Swinburne University of Technology.

## API Usage
This application takes heavy usage of [IGDB (Internet Game Database)](https://www.igdb.com/) and its [associated API](https://www.igdb.com/api). In order to use this API, you will need to have a Twitch account in order to generate a Client ID, Client Secret and Access Token. Further instructions can be found [here](https://api-docs.igdb.com/#getting-started).

<mark>Note: For this assignment, both a Client-ID and Authentication Token have been provided for the sake of ease of marking. This is <b>BAD PRACTICE</b> and should not be done otherwise. For suggestions on how this could be done otherwise, view the [Dotenv-Kotlin Github Repository](https://github.com/cdimascio/dotenv-kotlin) and its associated instructions.</mark>

## Plugins and Gradle Dependencies
This project takes use of the following dependencies:
- [Volley](https://google.github.io/volley/), an HTTP Request Library for Kotlin
- [Gson](https://github.com/google/gson), a JSON serialization/deserialization library made by Google
- [Picasso](https://square.github.io/picasso/), an image downloading/caching library for Android Studio
- [Room](https://developer.android.com/jetpack/androidx/releases/room), a data abstraction layer to link SQLite with Android Studio
(Note that to load this dependency, [KSP](https://github.com/google/ksp) or [KAPT (Deprecated)](https://kotlinlang.org/docs/kapt.html) are required to be loaded in "plugins")
- [AndroidX's Lifecycle Components](https://developer.android.com/jetpack/androidx/releases/lifecycle), a library of components that allow users to create lifecycle-aware programming.
### build.gradle.kts (Project)
If using [KAPT](https://kotlinlang.org/docs/kapt.html), skip this step.
```kts
plugins{
    id("com.google.devtools.ksp") version "1.9.0-1.0.12"
}
```

### build.gradle.kts (Module: app)
```kts
plugins {
    ...
    id("com.google.devtools.ksp")
}

dependencies {
    ...
    //Third Party Libraries (Volley, GSON and Picasso)
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.picasso:picasso:2.8")

    //Room Components
    val room_version = "2.6.1"
    
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    //Lifecycle Components (Specifically only the LiveData module is required.)
    val lifecycle_version = "2.8.6"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

}
```
