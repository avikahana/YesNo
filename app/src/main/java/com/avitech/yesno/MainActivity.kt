package com.avitech.yesno

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var yesButton: ImageButton
    private lateinit var noButton: ImageButton
    private lateinit var langToggle: Button

    private var yesPlayer: MediaPlayer? = null
    private var noPlayer: MediaPlayer? = null
    private var lang: String = LANG_EN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        yesButton = findViewById(R.id.yesButton)
        noButton = findViewById(R.id.noButton)
        langToggle = findViewById(R.id.langToggle)

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        lang = prefs.getString(KEY_LANG, deviceDefaultLang()) ?: LANG_EN

        langToggle.setOnClickListener { toggleLanguage() }
        applyLanguage()
    }

    /** First-run default follows the phone's language; a Hebrew locale ("iw"/"he") starts in Hebrew. */
    private fun deviceDefaultLang(): String {
        val language = resources.configuration.locales[0].language
        return if (language == "iw" || language == "he") LANG_HE else LANG_EN
    }

    private fun toggleLanguage() {
        lang = if (lang == LANG_HE) LANG_EN else LANG_HE
        prefs.edit().putString(KEY_LANG, lang).apply()
        applyLanguage()
    }

    /** Swaps button art, audio and the toggle label to match the current language. */
    private fun applyLanguage() {
        val hebrew = lang == LANG_HE
        yesButton.setImageResource(if (hebrew) R.drawable.yes_he else R.drawable.yes_en)
        noButton.setImageResource(if (hebrew) R.drawable.no_he else R.drawable.no_en)
        langToggle.text = getString(if (hebrew) R.string.switch_to_en else R.string.switch_to_he)

        releasePlayers()
        yesPlayer = MediaPlayer.create(this, if (hebrew) R.raw.yes_he else R.raw.yes_en)
        noPlayer = MediaPlayer.create(this, if (hebrew) R.raw.no_he else R.raw.no_en)
    }

    @Synchronized fun sayYes(view: View) = play(yesPlayer)

    @Synchronized fun sayNo(view: View) = play(noPlayer)

    private fun play(player: MediaPlayer?) {
        player ?: return
        if (player.isPlaying) player.seekTo(0)
        player.start()
    }

    private fun releasePlayers() {
        yesPlayer?.release(); yesPlayer = null
        noPlayer?.release(); noPlayer = null
    }

    override fun onDestroy() {
        releasePlayers()
        super.onDestroy()
    }

    companion object {
        private const val PREFS = "settings"
        private const val KEY_LANG = "lang"
        private const val LANG_EN = "en"
        private const val LANG_HE = "he"
    }
}