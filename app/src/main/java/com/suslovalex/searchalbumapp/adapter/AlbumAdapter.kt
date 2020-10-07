package com.suslovalex.searchalbumapp.adapter

import android.util.Log
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
import java.lang.Exception


class AlbumAdapter(): RecyclerView.Adapter<AlbumAdapter.ViewHolder>(){

    private var albums = mutableListOf<Album>()

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val itemAlbumImage: ImageView = itemView.findViewById(R.id.album_image)
        val itemAlbumTitle: TextView = itemView.findViewById(R.id.album_title)
        val itemArtistName: TextView = itemView.findViewById(R.id.artist_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_album,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumAdapter.ViewHolder, position: Int) {

        holder.itemAlbumTitle.text = albums[position].collectionName
        holder.itemArtistName.text = albums[position].artistName

        // Put a icon in the imageView
        Glide.with(holder.itemAlbumImage)
            .load(albums[position].artworkUrl100)
            .placeholder(R.drawable.progress_bar)
            .into(holder.itemAlbumImage)

        holder.itemView.setOnClickListener {
                val id = albums[position].collectionId
                val action = AlbumSearchFragmentDirections.navigateToAlbumDescriptionFragment(id)
                Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun setAlbumList(newAlbumList: List<Album>){
        albums = newAlbumList as MutableList<Album>
        notifyDataSetChanged()
    }

}