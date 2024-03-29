package com.kinzlstanislav.lastfmartists.feature_search.view.adapter

import com.kinzlstanislav.lastfmartists.architecture.core.model.Artist
import com.kinzlstanislav.lastfmartists.architecture.core.model.ArtistAvatarBitmap

interface ArtistItemClickListener {

    fun onArtistItemClicked(artist: Artist, artistAvatarBitmap: ArtistAvatarBitmap)

}