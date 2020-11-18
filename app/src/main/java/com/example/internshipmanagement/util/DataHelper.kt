package com.example.internshipmanagement.util

import com.example.internshipmanagement.data.entity.Criterion

class DataHelper {
    companion object {
        fun provideCriteria(): MutableList<Criterion> {
            return mutableListOf(
                Criterion("Behavior", "0"),
                Criterion("Knowledge", "0")
            )
        }
    }
}