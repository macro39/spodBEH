package com.example.spodbeh.model

import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Kamil Macek on 21.4.2020.
 */
data class Run(
        val runId: Long = 0,
        val name: String = "",
        val date: Date = Date(),
        val location: String = "",
        val distance: Double = 0.0,
        val elevation: Double = 0.0,
        val stands: ArrayList<Stand> = arrayListOf(),
        val runners: ArrayList<Runner> = arrayListOf()
)