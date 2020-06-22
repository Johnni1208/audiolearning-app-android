package com.audiolearning.app.ui.dialog.genericyesno

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.audiolearning.app.exception.MissingArgumentException
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialog.Companion.display

/**
 * Shows a generic yes/no dialog with given parameters for: title, message, positive button text
 * and negative button text.
 *
 * Receive positive and negative callbacks through [onActivityResult] with the requestCode from [display]
 *
 * **Use [display] to show the fragment.**
 */
class GenericYesNoDialog(
    private val requestCode: Int,
    private val dataReceiver: DialogDataReceiver
) : DialogFragment() {
    private lateinit var dialogContext: Context

    private lateinit var title: String
    private lateinit var message: String
    private lateinit var positiveButtonText: String
    private lateinit var negativeButtonText: String

    companion object {
        private const val ARG_TITLE = "GenericYesNoDialog.Title"
        private const val ARG_MESSAGE = "GenericYesNoDialog.Message"
        private const val ARG_POSITIVE_BUTTON_TEXT = "GenericYesNoDialog.PositiveButtonText"
        private const val ARG_NEGATIVE_BUTTON_TEXT = "GenericYesNoDialog.NegativeButtonText"

        /**
         * Use this method to show the dialog.
         *
         * @param fragmentManager fragmentManager to show the dialog
         * @param genericYesNoDialogTexts Instance of [GenericYesNoDialogTexts].
         * @param dataReceiver Data receiver to receive data from this dialog via [DialogDataReceiver.onDialogResult]
         */
        fun display(
            fragmentManager: FragmentManager,
            genericYesNoDialogTexts: GenericYesNoDialogTexts,
            dataReceiver: DialogDataReceiver,
            requestCode: Int
        ) {
            val genericYesNoDialog =
                GenericYesNoDialog(
                    requestCode,
                    dataReceiver
                )

            genericYesNoDialog.arguments = Bundle().apply {
                putString(ARG_TITLE, genericYesNoDialogTexts.title)
                putString(ARG_MESSAGE, genericYesNoDialogTexts.message)
                putString(ARG_POSITIVE_BUTTON_TEXT, genericYesNoDialogTexts.positiveButtonText)
                putString(ARG_NEGATIVE_BUTTON_TEXT, genericYesNoDialogTexts.negativeButtonText)
            }

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
                dataReceiver.onDialogResult(requestCode, Activity.RESULT_OK)
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                dataReceiver.onDialogResult(requestCode, Activity.RESULT_CANCELED)
            }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        dataReceiver.onDialogResult(requestCode, Activity.RESULT_CANCELED)
        super.onCancel(dialog)
    }

    private fun receiveArguments() {
        val args = requireArguments()
        title = args.getString(ARG_TITLE) ?: throw MissingArgumentException(
            ARG_TITLE
        )
        message = args.getString(ARG_MESSAGE) ?: throw MissingArgumentException(
            ARG_MESSAGE
        )
        positiveButtonText =
            args.getString(ARG_POSITIVE_BUTTON_TEXT) ?: throw MissingArgumentException(
                ARG_POSITIVE_BUTTON_TEXT
            )
        negativeButtonText =
            args.getString(ARG_NEGATIVE_BUTTON_TEXT) ?: throw MissingArgumentException(
                ARG_NEGATIVE_BUTTON_TEXT
            )
    }
}
