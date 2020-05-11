package com.example.spodbeh

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spodbeh.model.Run
import com.example.spodbeh.net.callRest
import com.example.spodbeh.net.fromJson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Kamil Macek on 23.4.2020.
 */
class SearchFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    var data: ArrayList<Run> = arrayListOf()
    var filteredData: MutableList<Run> = mutableListOf()

    lateinit var adapter: RunAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initFilterFields()

        textView_filter_date.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val month: Int = c.get(Calendar.MONTH)
            val day: Int = c.get(Calendar.DAY_OF_MONTH)
            val year: Int = c.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(this.context!!, OnDateSetListener { _, year, month, dayOfMonth -> textView_filter_date.text = "" + String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (month + 1)) + "/$year" }, year, month, day)
            datePickerDialog.show()
        }

        button_search.setOnClickListener {
            filterList()
        }

        updateData(true)
    }

    private fun filterList() {
        filteredData.clear()

        if (!chip_search_name.isChecked && !chip_search_date.isChecked && !chip_search_location.isChecked) {
            filteredData.addAll(data)
        } else {
            for (run in data) {
                var probablyToAdd: Run? = null

                if (chip_search_name.isChecked && run.name.toLowerCase().contains(editText_filter_name.text?.trim().toString().toLowerCase())) {
                    probablyToAdd = run
                }

                if (chip_search_date.isChecked) {
                    probablyToAdd = if (SimpleDateFormat("dd/MM/yyyy").format(run.date) == textView_filter_date.text) {
                        run
                    } else {
                        null
                    }
                }

                if (chip_search_location.isChecked) {
                    probablyToAdd = if (run.location.toLowerCase().contains(ditText_filter_location.text?.trim().toString().toLowerCase())) {
                        run
                    } else {
                        null
                    }
                }

                if (probablyToAdd != null) {
                    filteredData.add(run)
                }
            }
        }

        textView_search_results_count.text = Constants.RESULTS_TEXT + filteredData.size

        if (filteredData.isEmpty()) {
            textView_search_no_data.visibility = View.VISIBLE
        } else {
            textView_search_no_data.visibility = View.GONE
        }

        listView.adapter = null

        adapter = RunAdapter(this.activity!!, filteredData as ArrayList<Run>)

        listView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_reload) {
            initFilterFields()
            updateData(true)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFilterFields() {
        val c: Calendar = Calendar.getInstance()
        val month: Int = c.get(Calendar.MONTH) + 1
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val year: Int = c.get(Calendar.YEAR)

        textView_filter_date.text = "" + String.format("%02d", day) + "/" + String.format("%02d", (month + 1)) + "/$year"

        editText_filter_name.text?.clear()
        ditText_filter_location.text?.clear()

        chip_search_name.isChecked = false
        chip_search_date.isChecked = false
        chip_search_location.isChecked = false
    }

    private fun updateData(updateListView: Boolean) {
        progressBar_search.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = callRest("/run/getAll", "GET", null)
                Log.d(TAG, response)

                data = GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                        .create().fromJson<ArrayList<Run>>(response)

                if (updateListView) {
                    GlobalScope.launch(Dispatchers.Main) {
                        progressBar_search.visibility = View.GONE

                        textView_search_results_count.text = Constants.RESULTS_TEXT + data.size

                        if (data.isEmpty()) {
                            textView_search_no_data.visibility = View.VISIBLE
                        } else {
                            textView_search_no_data.visibility = View.GONE
                        }

                        adapter = RunAdapter(this@SearchFragment.activity!!, data)
                        listView.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message)
            }
        }
    }
}
