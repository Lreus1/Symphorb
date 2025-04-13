package com.example.symphorb.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

object SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun reproducirNota(context: Context, @RawRes notaResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, notaResId)
        mediaPlayer?.start()
    }
}