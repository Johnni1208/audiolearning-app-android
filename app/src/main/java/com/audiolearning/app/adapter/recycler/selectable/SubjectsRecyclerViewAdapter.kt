package com.audiolearning.app.adapter.recycler.selectable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.audiolearning.app.R
import com.audiolearning.app.adapter.recycler.selectable.base.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.extension.hide
import com.audiolearning.app.extension.show
import kotlinx.android.synthetic.main.subject_cardview.view.*

class SubjectsRecyclerViewAdapter(private var listener: ItemSelectListener<Subject>) :
    BaseSelectableRecyclerViewAdapter<Subject>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSelectableViewHolder {
        val subjectCardView: CardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_cardview, parent, false) as CardView

        return SubjectViewHolder(subjectCardView, listener)
    }

    override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int) {
        holder.itemView.tv_subject_name.text = (data[position]).name
        holder.itemView.alpha = 1f
        holder.itemView.iv_check_circle.hide()
    }

    @Suppress("MagicNumber")
    inner class SubjectViewHolder(
        private val subjectCardView: CardView,
        listener: ItemSelectListener<Subject>
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
