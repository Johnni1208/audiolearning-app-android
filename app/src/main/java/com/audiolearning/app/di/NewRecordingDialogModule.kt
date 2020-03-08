package com.audiolearning.app.di

import androidx.lifecycle.ViewModel
import com.audiolearning.app.ui.dialogs.new_recording.NewRecordingDialog
import com.audiolearning.app.ui.dialogs.new_recording.NewRecordingDialogViewModel
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
    internal abstract fun newRecoridngDialog(): NewRecordingDialog

    @Binds
    @IntoMap
    @ViewModelKey(NewRecordingDialogViewModel::class)
    abstract fun bindViewModel(viewModel: NewRecordingDialogViewModel): ViewModel
}