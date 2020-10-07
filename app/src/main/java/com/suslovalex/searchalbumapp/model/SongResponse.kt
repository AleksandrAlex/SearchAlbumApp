package com.suslovalex.searchalbumapp.model


data class SongResponse(
    val resultCount: Int,
    val results: List<Song>
)