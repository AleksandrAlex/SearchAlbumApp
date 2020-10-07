package com.suslovalex.searchalbumapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.suslovalex.searchalbumapp.R
import com.suslovalex.searchalbumapp.repository.Repository
import com.suslovalex.searchalbumapp.viewmodel.AlbumViewModel
import com.suslovalex.searchalbumapp.viewmodel.AlbumViewModelFactory
import com.suslovalex.searchalbumapp.viewmodel.ApiStatus
import kotlinx.android.synthetic.main.fragment_album_description.*


class AlbumDescriptionFragment : Fragment() {

    private val args: AlbumDescriptionFragmentArgs by navArgs()
    private val repository = Repository()
    private val albumViewModelFactory = AlbumViewModelFactory(repository)
    private lateinit var albumViewModel: AlbumViewModel
    private val noInternetConnection = "No internet connection available!"
    private val errorText = "???"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_album_description, container, false)
        // Initialization viewModel
        initData()
        // Get request from API
        getRequestById()
        // Check internet connection
        checkInternetConnection()
        // Search information changes and write date in the fields
        putDataIntoViews(view)
        return view
    }

    private fun checkInternetConnection() {
        albumViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it!!) {
                ApiStatus.LOADING -> {
                    imageView.setImageResource(R.drawable.progress_bar)
                }
                ApiStatus.ERROR -> {
                    imageView.setImageResource(R.drawable.no_signal)
                    title.text = noInternetConnection
                    artist.text = errorText
                    genre.text = errorText
                    year.text = errorText
                    country.text = errorText
                    price.text = errorText
                }
                ApiStatus.DONE -> {
                    Toast.makeText(context, "Welcome to album description!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun putDataIntoViews(view: View) {
        albumViewModel.info.observe(viewLifecycleOwner, Observer { songResponse ->
            val songList = songResponse.results.filter { it.wrapperType == "track" }
            // Put a image from internet to imageView
            Glide.with(view)
                .load(songList[0].artworkUrl100)
                .placeholder(R.drawable.progress_bar)
                .into(imageView)

            // Put all data into views
            title.text = songList[0].collectionName
            artist.text = songList[0].artistName
            genre.text = songList[0].primaryGenreName
            year.text = songList[0].releaseDate.substring(0, 4)
            country.text = songList[0].country
            price.text = songList[0].collectionPrice.toString()
            val songCount = songList[0].trackCount.toString() + " songs:"
            track_count.text = songCount
            var songString = ""
            songList.forEach {
                songString += it.trackNumber.toString() + " - " + it.trackName + "\n"
            }
            songs.text = songString
        })
    }

    private fun initData() {
        albumViewModel =
            ViewModelProvider(this, albumViewModelFactory).get(AlbumViewModel::class.java)
    }

    private fun getRequestById() {
        val collectionId = getCollectionId()
        albumViewModel.getAlbumInfoById(collectionId)
    }

    private fun getCollectionId(): Int {
        return args.collectionId
    }
}