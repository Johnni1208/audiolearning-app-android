package com.audiolearning.app.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.audiolearning.app.R
import com.audiolearning.app.adapter.SubjectSpinnerAdapter.Companion.create
import com.audiolearning.app.adapter.SubjectSpinnerAdapter.Companion.createWithAddHint
import com.audiolearning.app.data.db.entities.Subject

/**
 * ArrayAdapter for a spinner. Displays a list of given subjects.
 *
 * __IMPORTANT__: Use [createWithAddHint] or [create] to initialize this adapter.
 */
class SubjectSpinnerAdapter(
    context: Context,
    private var subjects: List<Subject>,
    private var hasSelectHint: Boolean
) : ArrayAdapter<Subject>(context, R.layout.subject_spinner_item, subjects) {
    companion object {
        /**
         * Creates the adapter with an "Add new subject..." hint at the first position.
         *
         * @param context Context
         * @param subjects List of Subjects to be displayed.
         * @param hasSelectHint If true, adds an "Select subject..." hint *at the end*.
         *
         * @return [SubjectSpinnerAdapter] with add hint.
         */
        fun createWithAddHint(
            context: Context,
            subjects: List<Subject>,
            hasSelectHint: Boolean
        ): SubjectSpinnerAdapter {
            val addSubjectItem =
                Subject(context.getString(R.string.nrDialog_add_subject), "").apply {
                    isRealSubject = false
                }

            var subjectsWithAddHint = listOf(addSubjectItem) + subjects

            if (hasSelectHint) {
                val subjectHintItem =
                    Subject(
                        context.getString(R.string.subject_spinner_select_subject),
                        ""
                    ).apply {
                        isRealSubject = false
                    }

                subjectsWithAddHint = subjectsWithAddHint + listOf(subjectHintItem)
            }

            return SubjectSpinnerAdapter(
                context,
                subjectsWithAddHint,
                hasSelectHint
            )
        }

        /**
         * Creates an normal [SubjectSpinnerAdapter]. Currently used for testing purposes.
         *
         * @param context Context
         * @param subjects List of Subjects to be displayed.
         * @param hasSelectHint If true, adds an "Select subject..." hint *at the end*.
         *
         * @return Normal [SubjectSpinnerAdapter]
         */
        fun create(
            context: Context,
            subjects: List<Subject>,
            hasSelectHint: Boolean
        ): SubjectSpinnerAdapter {
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

            return SubjectSpinnerAdapter(
                context,
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
