package com.audiolearning.app.adapter.recycler.selectable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiolearning.app.R
import com.audiolearning.app.adapter.recycler.selectable.base.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.extension.hide
import com.audiolearning.app.extension.show
import com.audiolearning.app.extension.toFormattedDate
import com.audiolearning.app.extension.toTimeString
import kotlinx.android.synthetic.main.audio_item.view.*

class AudiosRecyclerViewAdapter(private var listener: ItemSelectListener<Audio>) :
    BaseSelectableRecyclerViewAdapter<Audio>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSelectableViewHolder {
        val audioItem: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.audio_item, parent, false)

        return AudioViewHolder(audioItem, listener)
    }

    override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int) {
        holder.itemView.tv_audio_name.text = (data[position]).name
        holder.itemView.tv_audio_create_date.text =
            (data[position]).createDate.toFormattedDate()
        holder.itemView.tv_audio_duration.text =
            (data[position]).durationInMilliseconds.toTimeString()
        holder.itemView.iv_audio_check_circle.hide()
    }

    inner class AudioViewHolder(
        private val audioItemView: View,
        listener: ItemSelectListener<Audio>
    ) : BaseSelectableViewHolder(audioItemView, listener) {
        override fun setViewSelectedUi() {
            this.audioItemView.iv_audio_check_circle.show()
        }

        override fun setViewDeselectUi() {
            this.audioItemView.iv_audio_check_circle.hide()
        }
    }
}
