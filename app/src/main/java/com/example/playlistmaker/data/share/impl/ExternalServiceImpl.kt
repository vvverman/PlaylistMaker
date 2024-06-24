package com.example.playlistmaker.data.share.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.data.share.ExternalService
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.utils.DateFormat


class ExternalServiceImpl(private val context: Context) : ExternalService {
    override fun shareLink(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = context.getString(R.string.type_share_text)
        }
        val shareIntent = Intent
            .createChooser(sendIntent, null)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openUserAgreement(url: String) {
        TODO("Not yet implemented")
    }

    override fun openEmail(email: String, subject: String, message: String) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(context.getString(R.string.url_mail))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
    }

}

    override fun sharePlaylist(name: String, description: String, tracks: List<Track>) {
        val message = createPlaylistInfoText(name, description, tracks)

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = context.getString(R.string.type_share_text)
        }
        val shareIntent = Intent
            .createChooser(sendIntent, null)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    private fun createPlaylistInfoText(
        name: String,
        description: String,
        tracks: List<Track>
    ): String {
        val tracksCountPluralEnding = context.resources.getQuantityString(
            R.plurals.track,
            tracks.count(),
            tracks.count()
        )
        val tracksCountText = "${tracks.count()} $tracksCountPluralEnding"
        val tracksInfoText = getTracksInfoText(tracks)
        return "$name\n$description\n$tracksCountText\n$tracksInfoText"
    }

    private fun getTracksInfoText(tracks: List<Track>): String {
        return tracks.mapIndexed { index: Int, track: Track ->
            "${index + 1}. " +
                    "${track.artistName} - " +
                    "${track.trackName} " +
                    "(${DateFormat.formatMillisToString(track.trackTimeMillis)})\n"
        }.joinToString(separator = "")
    }
}