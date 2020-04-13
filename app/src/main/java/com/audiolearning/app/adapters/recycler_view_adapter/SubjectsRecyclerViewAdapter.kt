package com.audiolearning.app.adapters.recycler_view_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.audiolearning.app.R
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.ItemSelectListener
import com.audiolearning.app.extensions.hide
import com.audiolearning.app.extensions.show
import kotlinx.android.synthetic.main.subject_cardview.view.*

class SubjectsRecyclerViewAdapter(private var listener: ItemSelectListener) :
    BaseSelectableRecyclerViewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSelectableViewHolder {
        val subjectCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_cardview, parent, false) as CardView

        return SubjectViewHolder(subjectCardView, listener)
    }

    override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int) {
        holder.itemView.tv_subject_name.text = data[position].name
        holder.itemView.alpha = 1f
        holder.itemView.iv_check_circle.hide()
    }

    inner class SubjectViewHolder(
        private val subjectCardView: CardView,
        listener: ItemSelectListener
    ) : BaseSelectableViewHolder(subjectCardView, listener) {
        override fun setViewSelectedUi() {
            this.subjectCardView.alpha = 0.75f
            this.subjectCardView.iv_check_circle.show()
        }

        override fun setViewDeselectUi() {
            this.subjectCardView.alpha = 1f
            this.subjectCardView.iv_check_circle.hide()
        }
    }
}