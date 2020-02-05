package com.example.audiolearning.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.audiolearning.R
import com.example.audiolearning.data.db.entities.Subject

class SubjectArrayAdapter(
    context: Context,
    textViewResourceId: Int,
    private var subjects: List<Subject>,
    private var hasSelectHint: Boolean
) : ArrayAdapter<Subject>(context, textViewResourceId, subjects) {

    companion object {
        fun createWithAddHint(
            context: Context,
            textViewResourceId: Int,
            subjects: List<Subject>,
            hasSelectHint: Boolean
        ): SubjectArrayAdapter {
            val addSubjectItem =
                Subject(context.getString(R.string.nrDialog_add_subject), "").apply {
                    isRealSubject = false
                }

            var subjectsWithAdder = listOf(addSubjectItem) + subjects

            if (hasSelectHint) {
                val subjectHintItem =
                    Subject(
                        context.getString(R.string.subject_spinner_select_subject),
                        ""
                    ).apply {
                        isRealSubject = false
                    }

                subjectsWithAdder = subjectsWithAdder + listOf(subjectHintItem)
            }

            return SubjectArrayAdapter(
                context,
                textViewResourceId,
                subjectsWithAdder,
                hasSelectHint
            )
        }

        fun create(
            context: Context,
            textViewResourceId: Int,
            subjects: List<Subject>,
            hasSelectHint: Boolean
        ): SubjectArrayAdapter {
            var mutableSubjectList = subjects

            if (hasSelectHint) {
                val subjectHintItem =
                    Subject(
                        context.getString(R.string.subject_spinner_select_subject),
                        ""
                    ).apply {
                        isRealSubject = false
                    }

                mutableSubjectList = mutableSubjectList + listOf(subjectHintItem)
            }

            return SubjectArrayAdapter(
                context,
                textViewResourceId,
                mutableSubjectList,
                hasSelectHint
            )
        }
    }

    override fun getCount(): Int {
        return if (hasSelectHint) super.getCount() - 1
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

        makeAddHintBold(label)

        return label
    }

    private fun makeAddHintBold(label: TextView) {
        if (label.text == label.context.getString(R.string.nrDialog_add_subject)) {
            label.typeface = Typeface.DEFAULT_BOLD
        } else label.typeface = Typeface.DEFAULT
    }
}