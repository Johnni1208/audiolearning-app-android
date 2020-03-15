package com.audiolearning.app.ui.dialogs.generic_yes_no_dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.audiolearning.app.ui.dialogs.generic_yes_no_dialog.DefaultYesNoDialog.Companion.YES_NO_CALL
import com.audiolearning.app.ui.dialogs.generic_yes_no_dialog.DefaultYesNoDialog.Companion.display
import com.audiolearning.app.util.ArgumentMissingException

/**
 * Shows a default yes/no dialog with given parameters for: title, message, positive button text
 * and negative button text.
 *
 * Receive positive and negative callbacks through [onActivityResult] with [YES_NO_CALL] as requestCode
 *
 * **Use [display] to show the fragment.**
 */
class DefaultYesNoDialog : DialogFragment() {
    private lateinit var dialogContext: Context

    private lateinit var title: String
    private lateinit var message: String
    private lateinit var positiveButtonText: String
    private lateinit var negativeButtonText: String

    companion object {
        const val YES_NO_CALL = 420
        private const val ARG_TITLE = "GenericYesNoDialog.Title"
        private const val ARG_MESSAGE = "GenericYesNoDialog.Message"
        private const val ARG_POSITIVE_BUTTON_TEXT = "GenericYesNoDialog.PositiveButtonText"
        private const val ARG_NEGATIVE_BUTTON_TEXT = "GenericYesNoDialog.NegativeButtonText"

        /**
         * Use this method to show the dialog.
         *
         * @param fragmentManager fragmentManager to show the dialog
         * @param defaultYesNoDialogTexts Instance of [DefaultYesNoDialogTexts].
         * @param targetFragment target fragment which receives callbacks via the [onActivityResult] method.
         */
        fun display(
            fragmentManager: FragmentManager,
            defaultYesNoDialogTexts: DefaultYesNoDialogTexts,
            targetFragment: Fragment
        ) {
            val genericYesNoDialog = DefaultYesNoDialog()

            val args = Bundle().apply {
                putString(ARG_TITLE, defaultYesNoDialogTexts.title)
                putString(ARG_MESSAGE, defaultYesNoDialogTexts.message)
                putString(ARG_POSITIVE_BUTTON_TEXT, defaultYesNoDialogTexts.positiveButtonText)
                putString(ARG_NEGATIVE_BUTTON_TEXT, defaultYesNoDialogTexts.negativeButtonText)
            }
            genericYesNoDialog.arguments = args

            genericYesNoDialog.setTargetFragment(targetFragment, YES_NO_CALL)

            genericYesNoDialog.show(fragmentManager, "GenericYesNoDialog")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        receiveArguments()

        return AlertDialog.Builder(dialogContext)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
            }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
        super.onCancel(dialog)
    }

    private fun receiveArguments() {
        val args = requireArguments()
        title = args.getString(ARG_TITLE) ?: throw ArgumentMissingException(ARG_TITLE)
        message = args.getString(ARG_MESSAGE) ?: throw ArgumentMissingException(ARG_MESSAGE)
        positiveButtonText =
            args.getString(ARG_POSITIVE_BUTTON_TEXT) ?: throw ArgumentMissingException(
                ARG_POSITIVE_BUTTON_TEXT
            )
        negativeButtonText =
            args.getString(ARG_NEGATIVE_BUTTON_TEXT) ?: throw ArgumentMissingException(
                ARG_NEGATIVE_BUTTON_TEXT
            )
    }
}