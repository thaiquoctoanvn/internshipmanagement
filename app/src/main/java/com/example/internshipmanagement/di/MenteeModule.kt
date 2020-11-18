package com.example.internshipmanagement.di

import com.example.internshipmanagement.data.repository.MenteeRepository
import com.example.internshipmanagement.ui.MenteeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val menteeModule = module {
    single { MenteeRepository(get()) }
    viewModel { MenteeViewModel(get(), get()) }
}