package com.suslovalex.searchalbumapp.retrofit

import com.suslovalex.searchalbumapp.model.AlbumResponse
import com.suslovalex.searchalbumapp.model.SongResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://itunes.apple.com"
private const val ALBUM = "album"
private const val ATTRIBUTE = "albumTerm"
private const val LIMIT = 200
private const val SONG = "song"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ITunesApiService {
    @GET("search")
    suspend fun getAlbumByTitle(
        @Query("term") albumName: String,
        @Query("entity") entity: String = ALBUM,
        @Query("attribute") attribute: String = ATTRIBUTE,
        @Query("limit") limit: Int = LIMIT
    ): AlbumResponse

    @GET("lookup")
    suspend fun getAlbumInfoById(
        @Query("id") collectionId: Int,
        @Query("entity") entity: String = SONG
    ): SongResponse
}

object ITunesApi {
    val api: ITunesApiService by lazy {
        retrofit.create(ITunesApiService::class.java)
    }
}