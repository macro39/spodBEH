package com.example.spodbeh

import com.example.spodbeh.model.City
import com.example.spodbeh.model.Run
import com.example.spodbeh.model.Runner


/**
 * Created by Kamil Macek on 26.4.2020.
 */
object DataHolder {
    var loggedUser: Runner? = null
    var cities: ArrayList<City>? = null
    var citiesNames: ArrayList<String>? = null
    var newRun: Run? = null
}