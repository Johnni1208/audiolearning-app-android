package com.example.audiolearning.ui.dialogs.new_recording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.audiolearning.R
import kotlinx.android.synthetic.main.dialog_new_recording.*

class NewRecordingDialog(
//    var newRecordingDialogButtonsListener: NewRecordingDialogButtonsListener
) : DialogFragment() {

    companion object {
        fun display(fragmentManager: FragmentManager) =
            NewRecordingDialog().show(fragmentManager, "newrecordingdialog")
    }

    override fun onStart() {
        super.onStart()
        val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
        val height: Int = ViewGroup.LayoutParams.MATCH_PARENT

        dialog!!.window!!.let {
            it.setLayout(width, height)
            it.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_new_recording, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nr_toolbar.let {
            it.setTitle(R.string.nrDialog_title)
            it.setNavigationOnClickListener { dismiss() }
        }
    }
}