package com.example.spodbeh

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spodbeh.model.Stand
import kotlinx.android.synthetic.main.add_stand.*
import kotlinx.android.synthetic.main.add_stand.view.*
import kotlinx.android.synthetic.main.add_stand.view.spinner_add_stand_type
import kotlinx.android.synthetic.main.fragment_add_stands.*


/**
 * Created by Kamil Macek on 15.5.2020.
 */
class AddStandsFragment : Fragment() {

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

                        Toast.makeText(this.context, "Diaľkový beh bol úspešne vytvorený!", Toast.LENGTH_SHORT).show()

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

                DataHolder.newRun?.stands?.add(Stand(name = mDialogView.editText_add_stand_name.text.toString(),
                        refreshment = mDialogView.refreshment.isChecked, doctor = mDialogView.doctor.isChecked,
                        teamConsultations = mDialogView.team_consultations.isChecked, timeLimit = mDialogView.editText_add_stand_time.text.toString().toDouble(),
                        type = mDialogView.spinner_add_stand_type.selectedItem.toString()))

                standsNames.add(mDialogView.editText_add_stand_name.text.toString())
                standsAdapter.notifyDataSetChanged()

                mAlertDialog.dismiss()
            }
        }
    }

}