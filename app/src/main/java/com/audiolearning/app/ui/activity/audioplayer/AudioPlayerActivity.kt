package com.audiolearning.app.ui.activity.audioplayer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.audiolearning.app.R
import com.audiolearning.app.databinding.ActivityAudioPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AudioPlayerActivity : AppCompatActivity() {
    private val audioPlayerDataViewModel: AudioPlayerDataViewModel by viewModels()
    private val audioPlayerControlsViewModel: AudioPlayerControlsViewModel by viewModels()
    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_up, R.anim.scale_down)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_player)
        binding.lifecycleOwner = this
        binding.dataViewModel = audioPlayerDataViewModel
        binding.controlsViewModel = audioPlayerControlsViewModel

        audioPlayerDataViewModel.mediaButtonRes.observe(this, Observer { res ->
            binding.btnPlayPauseAudio.setImageResource(res)
        })

        binding.btnPlayPauseAudio.setOnClickListener {
            audioPlayerDataViewModel.mediaMetaData.value?.let {
                MainScope().launch { audioPlayerControlsViewModel.playAudioId(it.id) }
            }
        }

        setupToolbar()
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
