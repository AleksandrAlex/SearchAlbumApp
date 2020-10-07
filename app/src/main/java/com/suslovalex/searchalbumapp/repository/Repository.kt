package com.suslovalex.searchalbumapp.repository

import com.suslovalex.searchalbumapp.model.AlbumResponse
import com.suslovalex.searchalbumapp.model.SongResponse
import com.suslovalex.searchalbumapp.retrofit.ITunesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository {

    suspend fun getResponseByAlbumName(albumName: String): AlbumResponse {
        return withContext(Dispatchers.IO) {
            ITunesApi.api.getAlbumByTitle(albumName)
        }
    }

    suspend fun getAlbumInfoById(collectionId: Int): SongResponse {
        return withContext(Dispatchers.IO) {
            ITunesApi.api.getAlbumInfoById(collectionId)
        }
    }

}