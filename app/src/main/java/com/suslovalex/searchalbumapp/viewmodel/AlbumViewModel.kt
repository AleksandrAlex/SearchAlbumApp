package com.suslovalex.searchalbumapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suslovalex.searchalbumapp.model.Album
import com.suslovalex.searchalbumapp.model.AlbumResponse
import com.suslovalex.searchalbumapp.model.SongResponse
import com.suslovalex.searchalbumapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList

enum class ApiStatus { LOADING, ERROR, DONE }


class AlbumViewModel(private val repository: Repository) : ViewModel() {

    private val _albums = MutableLiveData<AlbumResponse>()
    val albums: LiveData<AlbumResponse>
        get() = _albums

    private val _info = MutableLiveData<SongResponse>()
    val info: LiveData<SongResponse>
        get() = _info

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    fun getAlbumByTitle(albumName: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _status.value = ApiStatus.LOADING
                _albums.value = repository.getResponseByAlbumName(albumName)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _albums.value = AlbumResponse(0, mutableListOf())
            }
        }
    }

    fun getAlbumInfoById(collectionId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _status.value = ApiStatus.LOADING
                _info.value = repository.getAlbumInfoById(collectionId)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }
}