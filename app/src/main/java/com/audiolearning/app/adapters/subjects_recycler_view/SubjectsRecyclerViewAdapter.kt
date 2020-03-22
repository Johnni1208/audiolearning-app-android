package com.audiolearning.app.adapters.subjects_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.audiolearning.app.R
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.extensions.hide
import com.audiolearning.app.extensions.show
import kotlinx.android.synthetic.main.subject_cardview.view.*

class SubjectsRecyclerViewAdapter(
    private var data: ArrayList<Subject>,
    private var subjectClickListener: SubjectClickListener
) : RecyclerView.Adapter<SubjectsRecyclerViewAdapter.SubjectsViewHolder>() {
    var dataInitialized = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsViewHolder {
        val subjectCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_cardview, parent, false) as CardView

        return SubjectsViewHolder(subjectCardView, subjectClickListener)
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.subjectCardView.tv_subject_name.text = data[position].name
        holder.subjectCardView.alpha = 1f
        holder.subjectCardView.iv_check_circle.hide()
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = position

    /**
     * Sets the initial data for this adapter. For more modifications use [updateData]
     *
     * @param data Initial data
     *
     * @throws IllegalStateException If the adapter's data has already been initialized
     */
    fun setInitialData(data: List<Subject>) {
        check(!dataInitialized) { "Already initialized. Use updateData()" }

        this.data = ArrayList(data)
        dataInitialized = true
        notifyDataSetChanged()
    }

    /**
     * Use this function to update the data of the adapter (remove/add)
     *
     * @param newData List of new Subjects
     *
     * @return [SubjectsRecyclerViewAdapterEvent] Whether it deleted or added an item
     *
     * @throws IllegalStateException If the data has not yet been initialised.
     */
    fun updateData(newData: List<Subject>): SubjectsRecyclerViewAdapterEvent {
        check(dataInitialized) { "Not yet initialized. Use setInitialData()" }

        // Adds items
        if (newData.size > data.size) {
            addItem(newData.last())
            return SubjectsRecyclerViewAdapterEvent.ITEMS_ADDED
        }

        // Removes items
        val positionOfDeletedItems: ArrayList<Int> = arrayListOf()
        for (i in 0 until data.size) {
            if (!newData.contains(data[i])) positionOfDeletedItems.add(i)
        }
        positionOfDeletedItems.reversed().forEach { position ->
            deleteItem(position)
        }
        return SubjectsRecyclerViewAdapterEvent.ITEMS_DELETED
    }

    private fun addItem(subject: Subject) {
        data.add(subject)
        notifyItemInserted(data.size - 1)
        notifyItemRangeInserted(data.size - 1, 1)
    }

    private fun deleteItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, data.size)
    }

    inner class SubjectsViewHolder(
        val subjectCardView: CardView,
        private val listener: SubjectClickListener
    ) : RecyclerView.ViewHolder(subjectCardView), View.OnClickListener, View.OnLongClickListener {
        init {
            this.subjectCardView.setOnClickListener(this@SubjectsViewHolder)
            this.subjectCardView.setOnLongClickListener(this@SubjectsViewHolder)
        }

        override fun onClick(v: View?) {
            data[adapterPosition].id?.let {
                this.subjectCardView.alpha = 1f
                this.subjectCardView.iv_check_circle.hide()
                listener.onSubjectItemClick(it.toInt())
            }
        }

        override fun onLongClick(v: View?): Boolean {
            data[adapterPosition].id?.let {
                this.subjectCardView.alpha = 0.75f
                this.subjectCardView.iv_check_circle.show()
                return listener.onSubjectItemLongClick(it.toInt())
            }

            return true
        }
    }

    interface SubjectClickListener {
        fun onSubjectItemClick(id: Int)
        fun onSubjectItemLongClick(id: Int): Boolean
    }
}