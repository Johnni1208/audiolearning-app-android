package com.audiolearning.app.adapter.recycler.selectable

import android.view.LayoutInflater
import android.view.ViewGroup
import com.audiolearning.app.R
import com.audiolearning.app.adapter.recycler.selectable.base.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.util.ColorHelper
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.subject_item.view.tv_subject_name

class SubjectsRecyclerViewAdapter(private var listener: ItemSelectListener<Subject>) :
    BaseSelectableRecyclerViewAdapter<Subject>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSelectableViewHolder {
        val subjectCardView: MaterialCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_item, parent, false) as MaterialCardView

        return SubjectViewHolder(subjectCardView, listener)
    }

    override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int) {
        holder.itemView.tv_subject_name.text = (data[position]).name
        holder.showViewDeselectedUi()
    }

    inner class SubjectViewHolder(
        private val subjectCardView: MaterialCardView,
        listener: ItemSelectListener<Subject>
    ) : BaseSelectableViewHolder(subjectCardView, listener) {
        private val colorHelper = ColorHelper(subjectCardView.context)

        override fun showViewSelectedUi() {
            this.subjectCardView.setCardBackgroundColor(colorHelper.yellow50)
            this.subjectCardView.strokeColor = colorHelper.yellow700

            this.subjectCardView.tv_subject_name.setTextColor(colorHelper.yellow700)
        }

        override fun showViewDeselectedUi() {
            this.subjectCardView.setCardBackgroundColor(colorHelper.white)
            this.subjectCardView.strokeColor = colorHelper.colorDivider

            this.subjectCardView.tv_subject_name.setTextColor(colorHelper.colorTextPrimary)
        }
    }
}
