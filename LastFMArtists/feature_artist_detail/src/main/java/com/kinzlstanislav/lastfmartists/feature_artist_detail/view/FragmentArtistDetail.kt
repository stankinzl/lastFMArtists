package com.kinzlstanislav.lastfmartists.feature_artist_detail.view

import android.os.Build
import android.text.Html
import com.kinzlstanislav.lastfmartists.architecture.core.model.Artist
import com.kinzlstanislav.lastfmartists.architecture.core.model.ArtistAvatarBitmap
import com.kinzlstanislav.lastfmartists.architecture.core.model.ArtistInfo
import com.kinzlstanislav.lastfmartists.base.ArgumentConstants.EXTRAS_ARTIST
import com.kinzlstanislav.lastfmartists.base.ArgumentConstants.EXTRAS_ARTIST_AVATAR
import com.kinzlstanislav.lastfmartists.base.extension.appear
import com.kinzlstanislav.lastfmartists.base.extension.bindArgument
import com.kinzlstanislav.lastfmartists.base.extension.observe
import com.kinzlstanislav.lastfmartists.base.view.BaseFragment
import com.kinzlstanislav.lastfmartists.feature_artist_detail.R
import com.kinzlstanislav.lastfmartists.feature_artist_detail.viewmodel.ArtistDetailViewModel
import com.kinzlstanislav.lastfmartists.feature_artist_detail.viewmodel.ArtistDetailViewModel.ArtistDetailInfoState.*
import kotlinx.android.synthetic.main.fragment_artist_detail.*
import kotlinx.android.synthetic.main.fragment_artist_detail_toolbar.*
import kotlinx.android.synthetic.main.view_artist_detail_content.*
import javax.inject.Inject
import com.kinzlstanislav.lastfmartists.feature_artist_detail.R.id.generic_error_refresh_button as genericErrButton
import com.kinzlstanislav.lastfmartists.feature_artist_detail.R.id.network_error_refresh_button as networkErrButton
import kotlinx.android.synthetic.main.fragment_artist_detail.artist_detail_flipper as flipper

class FragmentArtistDetail : BaseFragment() {

    @Inject
    lateinit var viewModel: ArtistDetailViewModel

    private val artist: Artist by bindArgument(EXTRAS_ARTIST)
    private val artistAvatar: ArtistAvatarBitmap by bindArgument(EXTRAS_ARTIST_AVATAR)

    override val layoutResourceId = R.layout.fragment_artist_detail

    override fun onFragmentCreated() {
        observe(viewModel.artistInfoState, ::handleState)
        viewModel.fetchLastfmArtistById(artist.id)
        setUpViewsOnStart()
        setUpToolbar()
        flipper.show(artist_detail_content_view)
    }

    private fun setUpToolbar() {
        artist_detail_title.text = artist.name
        artist_detail_title.appear()
    }

    private fun setUpViewsOnStart() {
        artist_detail_img.apply {
            artistAvatar.bitmap?.let { setImageBitmap(it) }
            appear()
        }
    }

    private fun handleState(state: ArtistDetailViewModel.ArtistDetailInfoState) = when (state) {
        is ArtistInfoLoaded -> onArtistInfoLoaded(state.artistInfo)
        is LoadingArtistInfo -> flipper.show(artist_detail_loader)
        is FetchingArtistInfoNE -> flipper.show(artist_detail_network_error)
        is FetchingArtistInfoGE -> flipper.show(artist_detail_generic_error)
    }

    private fun onArtistInfoLoaded(artistInfo: ArtistInfo) {
        flipper.show(artist_detail_content_view)
        if (artistInfo.summary.isNotEmpty()) {
            artist_detail_summary_headline.appear()
            artist_detail_summary.appear()
            artist_detail_summary.text = artistInfo.summary.formatFromHtl()
        }

        if (artistInfo.content.isNotEmpty()) {
            artist_detail_content_headline.appear()
            artist_detail_content.appear()
            artist_detail_content_headline.text = getString(R.string.more_about_artist_text, artist.name)
            artist_detail_content.text = artistInfo.content.formatFromHtl()
        }
    }

    private fun String.formatFromHtl() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(this)
        }
}