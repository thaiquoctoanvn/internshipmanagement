package com.example.internshipmanagement.di

import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.UserViewModel
import com.example.internshipmanagement.util.FCMHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    single { FCMHelper() }
    single { UserRepository(get()) }
    viewModel { UserViewModel(get(), get()) }
}