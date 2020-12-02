package com.example.internshipmanagement.di

import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.MentorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mentorModule = module {
//    single { MentorRepository(get()) }
    viewModel { MentorViewModel(get(), get()) }
}