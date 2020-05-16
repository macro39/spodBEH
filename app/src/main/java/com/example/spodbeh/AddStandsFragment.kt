package com.example.spodbeh

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spodbeh.model.Stand
import com.example.spodbeh.net.JsonBuilder
import com.example.spodbeh.net.callRest
import com.example.spodbeh.net.fromJson
import com.google.gson.GsonBuilder
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.RequestBody
import kotlinx.android.synthetic.main.add_stand.view.*
import kotlinx.android.synthetic.main.fragment_add_stands.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat


/**
 * Created by Kamil Macek on 15.5.2020.
 */
class AddStandsFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    val standsNames = arrayListOf<String>()
    lateinit var standsAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_stands, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        standsAdapter = ArrayAdapter(this.context!!, R.layout.stand_list_view_item, standsNames)

        listView_add_stands.adapter = standsAdapter

        button_add_stand_next.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setTitle("Upozornenie")
                    .setMessage("Všetky údaje sú správne. Prajete si vytvoriť novú udalosť?")
                    .setCancelable(false)
                    .setPositiveButton("Áno") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                val newRun = DataHolder.newRun

                                Log.d(TAG, newRun?.date.toString())

                                val json = JsonBuilder(
                                        "name" to newRun?.name!!,
                                        "date" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(newRun.date),
                                        "location" to newRun.location,
                                        "distance" to newRun.distance,
                                        "elevation" to newRun.elevation
                                )

                                val JSON = MediaType.parse("application/json; charset=utf-8")

                                val body: RequestBody = RequestBody.create(JSON, json.toString())

                                callRest("/run/save", "POST", body)
                            } catch (e: Exception) {
                                Log.d(TAG, e.message)
                            }
                        }

                        Toast.makeText(this@AddStandsFragment.context, "Diaľkový beh bol úspešne vytvorený!", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, AddFragment()).commit()
                    }
                    .setNegativeButton("Nie") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    .show()
        }


        button_add_stand.setOnClickListener {
            val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.add_stand, null)

            mDialogView.spinner_add_stand_type.adapter = ArrayAdapter(this.context!!, R.layout.support_simple_spinner_dropdown_item, arrayListOf("mesto", "vrchol"))

            mDialogView.spinner_add_stand_select.adapter = ArrayAdapter(this.context!!, R.layout.support_simple_spinner_dropdown_item, DataHolder.citiesNames!!)

            val mBuilder = AlertDialog.Builder(this.context)
                    .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            mDialogView.button_add_stand_final.setOnClickListener {
                if (mDialogView.editText_add_stand_name.text.trim().isEmpty()) {
                    Toast.makeText(this.context, "Zadajte názov", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (mDialogView.editText_add_stand_time.text.trim().isEmpty()) {
                    Toast.makeText(this.context, "Zadajte časový limit", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val city = DataHolder.cities?.get(mDialogView.spinner_add_stand_select.selectedItemPosition)

                val newStand = Stand(name = mDialogView.editText_add_stand_name.text.toString(),
                        refreshment = mDialogView.refreshment.isChecked, doctor = mDialogView.doctor.isChecked,
                        teamConsultations = mDialogView.team_consultations.isChecked, timeLimit = mDialogView.editText_add_stand_time.text.toString().toDouble(),
                        type = mDialogView.spinner_add_stand_type.selectedItem.toString(), longitude = city?.coordLon!!, latitude = city.coordLat)

                if (DataHolder.newRun?.stands?.isNotEmpty()!!) {
                    calculateNewDistance(DataHolder.newRun?.stands?.last()!!, newStand)
                }

                DataHolder.newRun?.stands?.add(newStand)

                standsNames.add(mDialogView.editText_add_stand_name.text.toString())
                standsAdapter.notifyDataSetChanged()

                mAlertDialog.dismiss()
            }
        }
    }

    private fun calculateNewDistance(oldStand: Stand, newStand: Stand) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = callRest("/run/distance/" + oldStand.latitude + "/" + oldStand.longitude
                        + "/" + newStand.latitude + "/" + newStand.longitude, "GET", null)
                Log.d(TAG, response)

                val distance = GsonBuilder().create().fromJson<Double>(response)

                Log.d(TAG, distance.toString())

                DataHolder.newRun?.distance = DataHolder.newRun?.distance?.plus(distance)!!

                Log.d(TAG, DataHolder.newRun?.distance.toString())

                withContext(Dispatchers.Main) {
                    updateDistance()
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message)
            }
        }
    }

    fun updateDistance() {
        textView_add_stands_distance.text = "%.2f".format(DataHolder.newRun?.distance)
    }

}