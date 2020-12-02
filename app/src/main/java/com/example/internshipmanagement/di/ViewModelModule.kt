package com.example.internshipmanagement.di

import com.example.internshipmanagement.ui.addtask.AddTaskViewModel
import com.example.internshipmanagement.ui.main.MainViewModel
import com.example.internshipmanagement.ui.applaunching.LaunchingViewModel
import com.example.internshipmanagement.ui.calendar.CalendarViewModel
import com.example.internshipmanagement.ui.dashboard.mentee.MenteeDashBoardViewModel
import com.example.internshipmanagement.ui.dashboard.mentor.MentorDashBoardViewModel
import com.example.internshipmanagement.ui.evaluationcreating.EvaluationCreatingViewModel
import com.example.internshipmanagement.ui.evaluationprofile.EvaluationProfileViewModel
import com.example.internshipmanagement.ui.login.LogInViewModel
import com.example.internshipmanagement.ui.notification.NotificationViewModel
import com.example.internshipmanagement.ui.people.mentee.MenteeSearcViewModel
import com.example.internshipmanagement.ui.people.mentor.MentorSearchViewModel
import com.example.internshipmanagement.ui.profileediting.ProfileEditingViewModel
import com.example.internshipmanagement.ui.searchusers.SearchResultViewModel
import com.example.internshipmanagement.ui.taskdetail.mentee.MenteeTaskDetailViewModel
import com.example.internshipmanagement.ui.taskdetail.mentor.MentorTaskDetailViewModel
import com.example.internshipmanagement.ui.taskreference.TaskReferencesViewModel
import com.example.internshipmanagement.ui.taskreviewing.TaskReviewingViewModel
import com.example.internshipmanagement.ui.tasksubmission.TaskSubmissionViewModel
import com.example.internshipmanagement.ui.userprofile.StatisticViewModel
import com.example.internshipmanagement.ui.userprofile.other.UserProfileViewModel
import com.example.internshipmanagement.ui.userprofile.personal.PersonalViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MenteeDashBoardViewModel(get(), get()) }
    viewModel { MenteeSearcViewModel(get(), get()) }
    viewModel { MenteeTaskDetailViewModel(get(), get()) }
    viewModel { TaskSubmissionViewModel(get(), get()) }

    viewModel { AddTaskViewModel(get(), get()) }
    viewModel { MentorSearchViewModel(get(), get()) }
    viewModel { MentorDashBoardViewModel(get(), get()) }
    viewModel { EvaluationCreatingViewModel(get(), get()) }
    viewModel { EvaluationProfileViewModel(get(), get()) }
    viewModel { MentorTaskDetailViewModel(get()) }
    viewModel { TaskReferencesViewModel(get(), get()) }
    viewModel { TaskReviewingViewModel(get()) }

    viewModel { MainViewModel(get(), get()) }
    viewModel { LaunchingViewModel(get(), get()) }
    viewModel { LogInViewModel(get(), get()) }
    viewModel { CalendarViewModel(get(), get()) }
    viewModel { ProfileEditingViewModel(get(), get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { SearchResultViewModel(get()) }
    viewModel { UserProfileViewModel(get(), get()) }
    viewModel { PersonalViewModel(get(), get()) }
    viewModel { StatisticViewModel(get()) }
}