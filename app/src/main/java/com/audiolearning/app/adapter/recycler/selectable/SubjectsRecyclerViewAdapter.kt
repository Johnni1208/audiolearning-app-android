package com.audiolearning.app.adapter.recycler.selectable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.audiolearning.app.R
import com.audiolearning.app.adapter.recycler.selectable.base.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Subject
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.subject_cardview.view.tv_subject_name

class SubjectsRecyclerViewAdapter(private var listener: ItemSelectListener<Subject>) :
    BaseSelectableRecyclerViewAdapter<Subject>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSelectableViewHolder {
        val subjectCardView: MaterialCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_cardview, parent, false) as MaterialCardView

        return SubjectViewHolder(subjectCardView, listener)
    }

    override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int) {
        holder.itemView.tv_subject_name.text = (data[position]).name
        holder.setViewDeselectUi()
    }

    @Suppress("MagicNumber")
    inner class SubjectViewHolder(
        private val subjectCardView: MaterialCardView,
        listener: ItemSelectListener<Subject>
    ) : BaseSelectableViewHolder(subjectCardView, listener) {
        private val yellow50: Int = ContextCompat.getColor(
            subjectCardView.context,
            R.color.yellow_50
        )

        private val yellow700: Int = ContextCompat.getColor(
            subjectCardView.context,
            R.color.yellow_700
        )

        private val white: Int = ContextCompat.getColor(
            subjectCardView.context,
            android.R.color.white
        )

        private val colorDivider: Int = ContextCompat.getColor(
            subjectCardView.context,
            R.color.colorDivider
        )

        private val colorTextPrimary: Int = ContextCompat.getColor(
            subjectCardView.context,
            R.color.colorTextPrimary
        )

        override fun setViewSelectedUi() {
            this.subjectCardView.setCardBackgroundColor(yellow50)
            this.subjectCardView.strokeColor = yellow700

            this.subjectCardView.tv_subject_name.setTextColor(yellow700)
        }

        override fun setViewDeselectUi() {
            this.subjectCardView.setCardBackgroundColor(white)
            this.subjectCardView.strokeColor = colorDivider

            this.subjectCardView.tv_subject_name.setTextColor(colorTextPrimary)
        }
    }
}
