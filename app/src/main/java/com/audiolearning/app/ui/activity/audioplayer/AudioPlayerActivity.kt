package com.audiolearning.app.ui.activity.audioplayer

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.audiolearning.app.R
import com.audiolearning.app.databinding.ActivityAudioPlayerBinding
import com.audiolearning.app.extension.toTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AudioPlayerActivity : AppCompatActivity() {
    private val dataViewModel: AudioPlayerDataViewModel by viewModels()
    private val controlsViewModel: AudioPlayerControlsViewModel by viewModels()
    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_up, R.anim.scale_down)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_player)
        binding.lifecycleOwner = this
        binding.dataViewModel = dataViewModel
        binding.controlsViewModel = controlsViewModel

        dataViewModel.mediaButtonRes.observe(this, { res ->
            binding.btnPlayPauseAudio.setImageResource(res)
        })

        binding.btnPlayPauseAudio.setOnClickListener {
            dataViewModel.mediaMetaData.value?.let {
                MainScope().launch { controlsViewModel.playAudioId(it.id) }
            }
        }

        dataViewModel.mediaPosition.observe(this, { pos ->
            binding.tvCurrentAudioPosition.text = pos.toTimeString()
            binding.seekBar.progress = pos.toInt()
        })

        dataViewModel.mediaMetaData.observe(this, { data ->
            binding.seekBar.max = data.duration.toInt()
            binding.tvDuration.text = data.duration.toTimeString()
        })

        setupToolbar()
        setupSeekBar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbAudioPlayer)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbAudioPlayer.setNavigationIcon(R.drawable.ic_arrow_down)
        binding.tbAudioPlayer.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) controlsViewModel.seekTo(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No need to implement
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No need to implement
            }
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.scale_normal, R.anim.slide_out_down)
    }
}
