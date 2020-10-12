package com.suslovalex.searchalbumapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suslovalex.searchalbumapp.R
import com.suslovalex.searchalbumapp.model.Album
import com.suslovalex.searchalbumapp.view.AlbumSearchFragmentDirections


class AlbumAdapter() : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private var albums = mutableListOf<Album>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemAlbumImage: ImageView = itemView.findViewById(R.id.album_image)
        private val itemAlbumTitle: TextView = itemView.findViewById(R.id.album_title)
        private val itemArtistName: TextView = itemView.findViewById(R.id.artist_name)
        private var collectionId: Int = 0

        init {
            itemView.setOnClickListener {
                val action = AlbumSearchFragmentDirections.navigateToAlbumDescriptionFragment(collectionId)
                Navigation.findNavController(it).navigate(action)
            }
        }

        fun bind(album: Album) {
            val id = album.collectionId
            collectionId = id
            // Fill view with our data
            itemAlbumTitle.text = album.collectionName
            itemArtistName.text = album.artistName
            // Put a icon in the imageView
            Glide.with(itemAlbumImage)
                .load(album.artworkUrl100)
                .placeholder(R.drawable.progress_bar)
                .into(itemAlbumImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumAdapter.ViewHolder, position: Int) {
        // get album by position
        val album = albums[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun setAlbumList(newAlbumList: List<Album>) {
        albums = newAlbumList as MutableList<Album>
        notifyDataSetChanged()
    }

}