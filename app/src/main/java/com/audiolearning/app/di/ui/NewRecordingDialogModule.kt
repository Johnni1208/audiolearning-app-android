package com.audiolearning.app.di.ui

import androidx.lifecycle.ViewModel
import com.audiolearning.app.di.ViewModelBuilder
import com.audiolearning.app.di.ViewModelKey
import com.audiolearning.app.ui.dialog.newrecording.NewRecordingDialog
import com.audiolearning.app.ui.dialog.newrecording.NewRecordingDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class NewRecordingDialogModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun newRecordingDialog(): NewRecordingDialog

    @Binds
    @IntoMap
    @ViewModelKey(NewRecordingDialogViewModel::class)
    abstract fun bindViewModel(viewModel: NewRecordingDialogViewModel): ViewModel
}
