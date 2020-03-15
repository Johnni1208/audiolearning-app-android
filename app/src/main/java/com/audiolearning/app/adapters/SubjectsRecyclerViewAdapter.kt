package com.audiolearning.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.audiolearning.app.R
import com.audiolearning.app.data.db.entities.Subject
import kotlinx.android.synthetic.main.subject_cardview.view.*

class SubjectsRecyclerViewAdapter(
    private var data: List<Subject>,
    private var subjectClickListener: SubjectClickListener
) :
    RecyclerView.Adapter<SubjectsRecyclerViewAdapter.SubjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsViewHolder {
        val subjectCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_cardview, parent, false) as CardView

        return SubjectsViewHolder(subjectCardView, subjectClickListener)
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.subjectCardView.tv_subject_name.text = data[position].name
    }

    override fun getItemCount() = data.size

    /**
     * Use this function to apply new data for the adapter.
     *
     * @param newData List of new Subjects
     */
    fun setData(newData: List<Subject>) {
        data = newData
        notifyDataSetChanged()
    }


    inner class SubjectsViewHolder(
        val subjectCardView: CardView,
        private val listener: SubjectClickListener
    ) :
        RecyclerView.ViewHolder(subjectCardView), View.OnClickListener, View.OnLongClickListener {

        init {
            this.subjectCardView.setOnClickListener(this@SubjectsViewHolder)
            this.subjectCardView.setOnLongClickListener(this@SubjectsViewHolder)
        }

        override fun onClick(v: View?) {
            data[adapterPosition].id?.let {
                listener.onSubjectItemClick(it.toInt())
            }
        }


        override fun onLongClick(v: View?): Boolean {
            data[adapterPosition].id?.let {
                return listener.onSubjectItemLongClick(it.toInt())
            }

            return true
        }
    }

    interface SubjectClickListener {
        fun onSubjectItemClick(position: Int)
        fun onSubjectItemLongClick(position: Int): Boolean
    }
}