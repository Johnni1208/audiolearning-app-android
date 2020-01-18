package com.example.audiolearning.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.audiolearning.data.db.entities.Subject

class SubjectArrayAdapter(
    context: Context,
    textViewResourceId: Int,
    private var subjects: List<Subject>
) : ArrayAdapter<Subject>(context, textViewResourceId, subjects) {

    override fun getCount(): Int {
        /* When the last item is not a real subject, then it's the selection item,
         which we don't need to display in the dropdown.*/
        return if (!subjects.last().isRealSubject) subjects.size - 1
        else super.getCount()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = subjects[position].name

        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = subjects[position].name

        return label
    }
}