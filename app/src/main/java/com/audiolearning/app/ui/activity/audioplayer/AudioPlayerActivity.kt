package com.audiolearning.app.ui.activity.audioplayer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.audiolearning.app.R
import com.audiolearning.app.databinding.ActivityAudioPlayerBinding
import com.audiolearning.app.exception.MissingArgumentException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class AudioPlayerActivity : AppCompatActivity() {
    private val viewModel: AudioPlayerActivityViewModel by viewModels()
    private lateinit var binding: ActivityAudioPlayerBinding

    companion object {
        const val EXTRA_AUDIO_ID = "extra_audio_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_up, R.anim.scale_down)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_player)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupAudio()
        setupToolbar()
    }

    private fun setupAudio() = runBlocking {
        viewModel.setAudio(
            intent.extras?.getInt(EXTRA_AUDIO_ID)
                ?: throw MissingArgumentException(EXTRA_AUDIO_ID)
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbAudioPlayer)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbAudioPlayer.setNavigationIcon(R.drawable.ic_baseline_keyboard_arrow_down_24dp)
        binding.tbAudioPlayer.setNavigationOnClickListener { onBackPressed() }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.scale_normal, R.anim.slide_out_down)
    }
}
