package com.example.spodbeh.model


/**
 * Created by Kamil Macek on 15.5.2020.
 */
data class City(
    val id: Int,
    val name: String,
    val countryIsoCode: String,
    val coordLat: Double,
    val coordLon: Double
)