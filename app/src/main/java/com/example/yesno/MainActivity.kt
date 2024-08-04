package com.avitech.yesno

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayerYes: MediaPlayer
    private lateinit var mediaPlayerNo: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mediaPlayerYes = MediaPlayer.create(this, R.raw.yes)
        mediaPlayerNo = MediaPlayer.create(this, R.raw.no)
    }

    @Synchronized fun sayYes(view: View) {

        if (mediaPlayerYes.isPlaying)
        {
            mediaPlayerYes.seekTo(0)
        }

        mediaPlayerYes.start()
    }

    @Synchronized fun sayNo(view: View) {

        if (mediaPlayerNo.isPlaying)
        {
            mediaPlayerNo.seekTo(0)
        }

        mediaPlayerNo.start()
    }
}