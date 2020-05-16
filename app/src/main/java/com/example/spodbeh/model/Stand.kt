package com.example.spodbeh.model


/**
 * Created by Kamil Macek on 21.4.2020.
 */
data class Stand(
        val standId: Int = 0,
        val runId: Int = 0,
        val serialNumber: Int = 0,
        val name: String = "",
        val type: String = "",
//        val description: String = "",
        val timeLimit: Double = 0.0,
        val distanceFromStart: Double = 0.0,
        val longitude: Double = 0.0,
        val latitude: Double = 0.0,
        val refreshment: Boolean = false,
        val doctor: Boolean = false,
        val teamConsultations: Boolean = false
)