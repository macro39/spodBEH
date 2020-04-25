package com.example.spodbeh.model

import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Kamil Macek on 21.4.2020.
 */
data class Runner(
        val runnerId: Int,
        val firstName: String,
        val lastName: String,
        val email: String,
        val telephoneNumber: String,
        val sex: String,
        val dateOfBirth: Date,
        val username: String,
        val password: String,
        val runs: ArrayList<Run>
)