package com.example.spodbeh

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spodbeh.model.City
import com.example.spodbeh.model.Run
import com.example.spodbeh.net.callRest
import com.example.spodbeh.net.fromJson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Kamil Macek on 23.4.2020.
 */
class AddFragment: Fragment() {

    private val TAG = this::class.java.simpleName

    private lateinit var data: ArrayList<City>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar_add.visibility = View.VISIBLE

        val c: Calendar = Calendar.getInstance()
        val month: Int = c.get(Calendar.MONTH) + 1
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val year: Int = c.get(Calendar.YEAR)

        textView_add_date.text = "" + String.format("%02d", day) + "-" + String.format("%02d", (month + 1)) + "-$year"

        textView_add_date.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val month: Int = c.get(Calendar.MONTH)
            val day: Int = c.get(Calendar.DAY_OF_MONTH)
            val year: Int = c.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(this.context!!, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth -> textView_add_date.text = "" + String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (month + 1)) + "-$year" }, year, month, day)
            datePickerDialog.show()
        }

        button_add_next.setOnClickListener {
            if (editText_add_name.text.trim().isEmpty()) {
                Toast.makeText(this.context, "Zadajte prosím názov behu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            DataHolder.newRun = Run(name = editText_add_name.text.toString(), location = spinner_add_location.selectedItem.toString(), date = SimpleDateFormat("dd-MM-yyyy").parse(textView_add_date.text.toString()))

            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, AddStandsFragment()).commit()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = callRest("/run/getSKCities", "GET", null)
                Log.d(TAG, response)

                data = GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                        .create().fromJson<ArrayList<City>>(response)

                DataHolder.cities = data

                withContext(Dispatchers.Main) {
                    val citiesNames = arrayListOf<String>()

                    for (city in data) {
                        citiesNames.add(city.name)
                    }

                    DataHolder.citiesNames = citiesNames

                    spinner_add_location.adapter = ArrayAdapter(this@AddFragment.context!!, R.layout.support_simple_spinner_dropdown_item, citiesNames)

                    spinner_add_location.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            val selected = data[position]

                            layout_location_info.visibility = View.VISIBLE

                            textView_location_name.text = selected.name
                            textView_location_country.text = selected.countryIsoCode
                            textView_location_latitude.text = selected.coordLat.toString()
                            textView_location_longtitude.text = selected.coordLon.toString()
                        }
                    })


                    progressBar_add.visibility = View.GONE
                }

            } catch (e: Exception) {
                Log.d(TAG, e.message)
            }
        }
    }

}