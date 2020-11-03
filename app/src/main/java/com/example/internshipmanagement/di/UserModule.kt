package com.example.internshipmanagement.di

import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    single { UserRepository(get()) }
    viewModel { UserViewModel(get(), get()) }
}