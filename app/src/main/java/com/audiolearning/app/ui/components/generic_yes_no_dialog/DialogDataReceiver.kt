package com.audiolearning.app.ui.components.generic_yes_no_dialog

interface DialogDataReceiver {
    fun onDialogResult(requestCode: Int, resultCode: Int)
}