package com.example.internshipmanagement.di

import com.example.internshipmanagement.data.repository.MenteeRepository
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.data.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { MentorRepository(get()) }
    single { MenteeRepository(get()) }
    single { UserRepository(get()) }
}