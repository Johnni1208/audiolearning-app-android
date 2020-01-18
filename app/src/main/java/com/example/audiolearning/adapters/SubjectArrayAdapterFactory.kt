package com.example.audiolearning.adapters

import android.content.Context
import com.example.audiolearning.R
import com.example.audiolearning.data.db.entities.Subject

class SubjectArrayAdapterFactory {
    companion object {
        fun createWithAddHint(
            context: Context,
            textViewResourceId: Int,
            subjects: List<Subject>
        ): SubjectArrayAdapter {
            val selectSubjectItem =
                Subject(context.getString(R.string.subject_spinner_select_subject), "").apply {
                    isRealSubject = false
                }

            val addSubjectItem =
                Subject(context.getString(R.string.nrDialog_add_subject), "").apply {
                    isRealSubject = false
                }

            val subjectsWithAdder = listOf(addSubjectItem) + subjects + listOf(selectSubjectItem)

            return SubjectArrayAdapter(context, textViewResourceId, subjectsWithAdder)
        }
    }
}