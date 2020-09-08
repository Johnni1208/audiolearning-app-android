package com.audiolearning.app.adapter.recycler.selectable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.audiolearning.app.R
import com.audiolearning.app.adapter.recycler.selectable.base.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.extension.toFormattedDate
import com.audiolearning.app.extension.toTimeString
import com.audiolearning.app.util.ColorHelper
import kotlinx.android.synthetic.main.audio_item.view.tv_audio_create_date
import kotlinx.android.synthetic.main.audio_item.view.tv_audio_duration
import kotlinx.android.synthetic.main.audio_item.view.tv_audio_name

class AudiosRecyclerViewAdapter(private var listener: ItemSelectListener<Audio>) :
    BaseSelectableRecyclerViewAdapter<Audio>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSelectableViewHolder {
        val audioItem: ConstraintLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.audio_item, parent, false) as ConstraintLayout

        return AudioViewHolder(audioItem, listener)
    }

    override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int) {
        holder.itemView.tv_audio_name.text = (data[position]).name
        holder.itemView.tv_audio_create_date.text =
            (data[position]).createDate.toFormattedDate()
        holder.itemView.tv_audio_duration.text =
            (data[position]).durationInMilliseconds.toTimeString()

        holder.setViewDeselectedUi()
    }

    inner class AudioViewHolder(
        private val audioItemView: ConstraintLayout,
        listener: ItemSelectListener<Audio>
    ) : BaseSelectableViewHolder(audioItemView, listener) {
        private val colorHelper = ColorHelper(audioItemView.context)

        override fun setViewSelectedUi() {
            this.audioItemView.setBackgroundResource(R.drawable.audio_item_selected_background)
            this.audioItemView.tv_audio_name.setTextColor(colorHelper.yellow700)
            this.audioItemView.tv_audio_create_date.setTextColor(colorHelper.yellow700)
            this.audioItemView.tv_audio_duration.setTextColor(colorHelper.yellow700)
        }

        override fun setViewDeselectedUi() {
            this.audioItemView.setBackgroundResource(colorHelper.ripple)
            this.audioItemView.tv_audio_name.setTextColor(colorHelper.colorTextPrimary)
            this.audioItemView.tv_audio_create_date.setTextColor(colorHelper.colorTextSecondary)
            this.audioItemView.tv_audio_duration.setTextColor(colorHelper.colorTextSecondary)
        }
    }
}
