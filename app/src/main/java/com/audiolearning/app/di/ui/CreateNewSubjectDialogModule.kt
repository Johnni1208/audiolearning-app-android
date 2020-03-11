package com.audiolearning.app.di.ui

import androidx.lifecycle.ViewModel
import com.audiolearning.app.di.ViewModelBuilder
import com.audiolearning.app.di.ViewModelKey
import com.audiolearning.app.ui.dialogs.create_new_subject.CreateNewSubjectDialog
import com.audiolearning.app.ui.dialogs.create_new_subject.CreateNewSubjectDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CreateNewSubjectDialogModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun createNewSubjectDialog(): CreateNewSubjectDialog

    @Binds
    @IntoMap
    @ViewModelKey(CreateNewSubjectDialogViewModel::class)
    abstract fun bindViewModel(viewModel: CreateNewSubjectDialogViewModel): ViewModel
}