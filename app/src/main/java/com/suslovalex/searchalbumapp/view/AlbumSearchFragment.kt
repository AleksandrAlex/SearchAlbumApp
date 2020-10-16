package com.suslovalex.searchalbumapp.view

import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suslovalex.searchalbumapp.R
import com.suslovalex.searchalbumapp.adapter.AlbumAdapter
import com.suslovalex.searchalbumapp.model.Album
import com.suslovalex.searchalbumapp.repository.Repository
import com.suslovalex.searchalbumapp.viewmodel.AlbumViewModel
import com.suslovalex.searchalbumapp.viewmodel.AlbumViewModelFactory
import com.suslovalex.searchalbumapp.viewmodel.ApiStatus
import kotlinx.android.synthetic.main.fragment_album_description.view.*
import kotlinx.android.synthetic.main.fragment_album_search.*
import kotlinx.android.synthetic.main.fragment_album_search.view.*
import java.lang.Exception


class AlbumSearchFragment : Fragment() {

    private val repository = Repository()
    private val albumViewModelFactory = AlbumViewModelFactory(repository)
    private lateinit var albumViewModel: AlbumViewModel
    private val albumAdapter by lazy { AlbumAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_album_search, container, false)
        // Initialization viewModel
        initData()
        // Setup recycleViewAdapter
        setUpRecyclerView(view)
        // Check internet connection
        checkInternetConnection()
        // Setup listener for search field
        searchFieldListener(view)
        // Setup new album list in recyclerView
        putDataIntoRecyclerView()
        return view
    }

    private fun checkInternetConnection() {
        albumViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it!!) {
                ApiStatus.LOADING -> {
                    image_connection.visibility = View.VISIBLE
                    image_connection.setImageResource(R.drawable.progress_bar)
                }
                ApiStatus.ERROR -> {
                    image_connection.visibility = View.VISIBLE
                    image_connection.setImageResource(R.drawable.no_signal)
                }
                ApiStatus.DONE -> {
                    image_connection.visibility = View.GONE
                }
            }
        })
    }

    private fun putDataIntoRecyclerView() {
        albumViewModel.albums.observe(viewLifecycleOwner, Observer {
            // If we get not null response the data will be put in the recyclerView
            if (it.resultCount != 0) {
                val albumList = it.results.sortedBy { albums ->
                    albums.collectionName
                }
                albumAdapter.setAlbumList(albumList)
                // If we get null response or the warning will be write in the toast
            } else {
                Toast.makeText(context, "Warning:\n The request is not correct!", Toast.LENGTH_LONG).show()
                albumAdapter.setAlbumList(mutableListOf())
            }

        })
    }

    private fun initData() {
        albumViewModel =
            ViewModelProvider(this, albumViewModelFactory).get(AlbumViewModel::class.java)
    }

    private fun setUpRecyclerView(view: View) {
        view.recyclerView.adapter = albumAdapter
    }

    private fun searchFieldListener(view: View) {
        view.searchView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val albumTitle = searchView.text.toString()
                albumViewModel.getAlbumByTitle(albumTitle)
                hideKeyboard(view)
                return@OnKeyListener true
            }
            false
        })
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}