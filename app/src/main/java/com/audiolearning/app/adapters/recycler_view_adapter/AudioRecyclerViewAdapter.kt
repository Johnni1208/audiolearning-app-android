package com.audiolearning.app.adapters.recycler_view_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiolearning.app.R
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.ItemSelectListener
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.extensions.toFormattedDate
import com.audiolearning.app.extensions.toTimeString
import kotlinx.android.synthetic.main.audio_item.view.*

class AudioRecyclerViewAdapter(private var listener: ItemSelectListener<Audio>) :
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
    }

    inner class AudioViewHolder(
        view: View,
        listener: ItemSelectListener<Audio>
    ) : BaseSelectableViewHolder(view, listener) {
        override fun setViewSelectedUi() {
            TODO("Not yet implemented")
        }

        override fun setViewDeselectUi() {
            TODO("Not yet implemented")
        }
    }
}