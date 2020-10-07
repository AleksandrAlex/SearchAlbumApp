package com.suslovalex.searchalbumapp.model




data class AlbumResponse(
    val resultCount: Int,
    val results: List<Album>
)