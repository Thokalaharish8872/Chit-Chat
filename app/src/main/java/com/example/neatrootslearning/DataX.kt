package com.example.neatrootslearning

data class DataX(
    val albumOfTrack: AlbumOfTrack,
    val artists: Artists,
    val contentRating: ContentRating,
    val duration: Duration,
    val id: String,
    val name: String,
    val playability: Playability,
    val uri: String
)