package com.example.spodbeh.model

import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Kamil Macek on 21.4.2020.
 */
data class Run(
        val id: Int,
        val name: String,
        val date: Date,
        val location: String,
        val distance: Double,
        val elevation: Double,
        val stands: ArrayList<Stand> = arrayListOf(),
        val runners: ArrayList<Runner> = arrayListOf()
)