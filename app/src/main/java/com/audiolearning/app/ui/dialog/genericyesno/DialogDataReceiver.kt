package com.audiolearning.app.ui.dialog.genericyesno

interface DialogDataReceiver {
    fun onDialogResult(requestCode: Int, resultCode: Int)
}
