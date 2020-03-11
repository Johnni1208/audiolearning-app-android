package com.audiolearning.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.audiolearning.app.R
import com.audiolearning.app.data.db.entities.Subject
import kotlinx.android.synthetic.main.subject_cardview.view.*

class SubjectsRecyclerViewAdapter(private var data: List<Subject>) :
    RecyclerView.Adapter<SubjectsRecyclerViewAdapter.SubjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsViewHolder {
        val subjectCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_cardview, parent, false) as CardView

        return SubjectsViewHolder(subjectCardView)
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.subjectCardView.tv_subject_name.text = data[position].name
    }

    override fun getItemCount() = data.size

    fun setData(newData: List<Subject>) {
        data = newData
        notifyDataSetChanged()
    }

    inner class SubjectsViewHolder(val subjectCardView: CardView) :
        RecyclerView.ViewHolder(subjectCardView)
}