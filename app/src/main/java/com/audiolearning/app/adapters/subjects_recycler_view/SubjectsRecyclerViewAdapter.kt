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
    private var subjectEventListener: SubjectEventListener
) : RecyclerView.Adapter<SubjectsRecyclerViewAdapter.SubjectViewHolder>() {
    var isDataInitialized = false
    var isSelecting = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val subjectCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_cardview, parent, false) as CardView

        return SubjectViewHolder(subjectCardView, subjectEventListener)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
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
    fun initializeData(data: List<Subject>) {
        check(!isDataInitialized) { "Already initialized. Use updateData()" }

        this.data = ArrayList(data)
        isDataInitialized = true
        notifyDataSetChanged()
    }

    /**
     * Use this function to update the data of the adapter (remove/add)
     *
     * @param newData List of new Subjects
     *
     * @return [SubjectsRecyclerViewAdapterEvent] Whether it deleted or added an item
     *
     * @throws IllegalStateException If the data has not yet been initialized.
     */
    fun updateData(newData: List<Subject>): SubjectsRecyclerViewAdapterEvent {
        check(isDataInitialized) { "Not yet initialized. Use initializeData()" }

        // Adds items
        if (newData.size > data.size) {
            addItem(newData.last())
            return SubjectsRecyclerViewAdapterEvent.ITEMS_ADDED
        }

        // Removes items
        val positionsOfDeletedItems: ArrayList<Int> = arrayListOf()
        for (i in 0 until data.size) {
            if (!newData.contains(data[i])) positionsOfDeletedItems.add(i)
        }
        positionsOfDeletedItems.reversed().forEach { position ->
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

    inner class SubjectViewHolder(
        val subjectCardView: CardView,
        private val listener: SubjectEventListener
    ) : RecyclerView.ViewHolder(subjectCardView), View.OnClickListener, View.OnLongClickListener {
        init {
            this.subjectCardView.setOnClickListener(this@SubjectViewHolder)
            this.subjectCardView.setOnLongClickListener(this@SubjectViewHolder)
        }

        override fun onClick(v: View?) {
            if (isSelecting) onLongClick(v)
            else listener.onSubjectItemClick(data[adapterPosition].id!!.toInt())
        }

        override fun onLongClick(v: View?): Boolean {
            data[adapterPosition].id?.let {
                if (this.subjectCardView.isSelected) {
                    setCardViewDeselectState()
                    listener.onSubjectItemDeselect(it.toInt())
                } else {
                    setCardViewSelectedState()
                    listener.onSubjectItemSelect(it.toInt())
                }
                return true
            }

            return false
        }

        private fun setCardViewSelectedState() {
            this.subjectCardView.isSelected = true
            this.subjectCardView.alpha = 0.75f
            this.subjectCardView.iv_check_circle.show()
        }

        private fun setCardViewDeselectState() {
            this.subjectCardView.isSelected = false
            this.subjectCardView.alpha = 1f
            this.subjectCardView.iv_check_circle.hide()
        }
    }

    interface SubjectEventListener {
        fun onSubjectItemDeselect(id: Int)
        fun onSubjectItemSelect(id: Int): Boolean
        fun onSubjectItemClick(id: Int)
    }
}