package com.example.spodbeh.model


/**
 * Created by Kamil Macek on 21.4.2020.
 */
data class Stand(
        val standId: Int,
        val runId: Int,
        val serialNumber: Int,
        val name: String,
        val type: String,
        val description: String,
        val timeLimit: Double,
        val distanceFromStart: Double,
        val gps: String,
        val refreshment: Boolean,
        val doctor: Boolean,
        val teamConsultations: Boolean
)