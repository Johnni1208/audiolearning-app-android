package com.audiolearning.app.adapter

import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.core.app.ApplicationProvider
import com.audiolearning.app.R
import com.audiolearning.app.adapter.recycler.selectable.AudiosRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.extension.toFormattedDate
import com.audiolearning.app.extension.toTimeString
import com.audiolearning.app.util.ColorHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.audio_item.view.tv_audio_create_date
import kotlinx.android.synthetic.main.audio_item.view.tv_audio_duration
import kotlinx.android.synthetic.main.audio_item.view.tv_audio_name
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AudioRecyclerViewAdapterTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val realData = ArrayList<Audio>().apply {
        add(Audio("1", "", 0, 0, System.currentTimeMillis()))
        add(Audio("2", "", 0, 0, System.currentTimeMillis()))
        add(Audio("3", "", 0, 0, System.currentTimeMillis()))
        add(Audio("4", "", 0, 0, System.currentTimeMillis()))
    }
    private val colorHelper = ColorHelper(context)
    private val mockAudioEventListener: ItemSelectListener<Audio> = mock()
    private val mockConstraintLayout: ConstraintLayout = mock()

    private lateinit var audioRecyclerViewAdapterWithRealData: AudiosRecyclerViewAdapter

    private lateinit var audioViewHolder: AudiosRecyclerViewAdapter.AudioViewHolder
    private val tvAudioName: TextView = mock()
    private val tvAudioCreateDate: TextView = mock()
    private val tvAudioDuration: TextView = mock()

    @Before
    fun setup() {
        whenever(mockConstraintLayout.context).thenReturn(context)

        audioRecyclerViewAdapterWithRealData =
            AudiosRecyclerViewAdapter(
                mockAudioEventListener
            ).apply {
                initializeData(realData)
            }

        audioViewHolder = audioRecyclerViewAdapterWithRealData.AudioViewHolder(
            mockConstraintLayout,
            mockAudioEventListener
        ).apply {
            whenever(this.itemView.tv_audio_name).thenReturn(tvAudioName)
            whenever(this.itemView.tv_audio_create_date).thenReturn(tvAudioCreateDate)
            whenever(this.itemView.tv_audio_duration).thenReturn(tvAudioDuration)
        }
    }

    @Test
    fun onBindViewHolder_ShouldSetCorrectText() {
        val position = 0

        audioRecyclerViewAdapterWithRealData.onBindViewHolder(audioViewHolder, position)
        verify(tvAudioName).text = realData[position].name
        verify(tvAudioCreateDate).text = realData[position].createDate.toFormattedDate()
        verify(tvAudioDuration).text = realData[position].durationInMilliseconds.toTimeString()
    }

    @Test
    fun onBindViewHolder_ShouldApplyDeselectedStyleToViewHolder() {
        val spyViewHolder = spy(
            audioRecyclerViewAdapterWithRealData.AudioViewHolder(
                mockConstraintLayout,
                mockAudioEventListener
            )
        )
        audioRecyclerViewAdapterWithRealData.onBindViewHolder(spyViewHolder, 0)

        verify(spyViewHolder, times(1)).setViewDeselectedUi()
    }

    @Test
    fun setViewSelectedUi_ShouldApplyCorrectStyleToCardView() {
        audioViewHolder.setViewSelectedUi()

        verify((audioViewHolder.itemView as ConstraintLayout), times(1)).setBackgroundResource(
            R.drawable.audio_item_selected_background
        )
        verify(audioViewHolder.itemView.tv_audio_name).setTextColor(colorHelper.yellow700)
        verify(audioViewHolder.itemView.tv_audio_create_date).setTextColor(colorHelper.yellow700)
        verify(audioViewHolder.itemView.tv_audio_duration).setTextColor(colorHelper.yellow700)
    }

    @Test
    fun setViewDeselectedUi_ShouldApplyCorrectStyleToCardView() {
        audioViewHolder.setViewDeselectedUi()

        verify((audioViewHolder.itemView as ConstraintLayout), times(1)).setBackgroundResource(
            colorHelper.ripple
        )
        verify(audioViewHolder.itemView.tv_audio_name).setTextColor(colorHelper.colorTextPrimary)
        verify(audioViewHolder.itemView.tv_audio_create_date).setTextColor(colorHelper.colorTextSecondary)
        verify(audioViewHolder.itemView.tv_audio_duration).setTextColor(colorHelper.colorTextSecondary)
    }
}
