package com.example.spodbeh

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.spodbeh.model.Run
import com.example.spodbeh.net.callRest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat


/**
 * Created by Kamil Macek on 24.4.2020.
 */

class RunAdapter(private var activity: Activity, private var items: ArrayList<Run>) : BaseAdapter() {
    private class ViewHolder(row: View?) {
        var name: TextView? = null
        var date: TextView? = null
        var location: TextView? = null
        var button: Button? = null

        init {
            this.name = row?.findViewById(R.id.textView_name)
            this.date = row?.findViewById(R.id.textView_date)
            this.location = row?.findViewById(R.id.textView_location)
            this.button = row?.findViewById(R.id.button_register)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.listview_row, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val run = items[position]
        viewHolder.name?.text = run.name

        val date = SimpleDateFormat("dd/MM/yyyy").format(run.date)
        viewHolder.date?.text = date
        viewHolder.location?.text = run.location

        val isAlreadyRegistered = DataHolder.loggedUser?.runs?.firstOrNull { s -> s.runId == run.runId } != null

        var dialogString = "Naozaj si prajete prihlásiť sa?"

        if (isAlreadyRegistered) {
//            Toast.makeText(activity.applicationContext, "Už si prihlásený", Toast.LENGTH_SHORT).show()
            viewHolder.button?.text = "Odhlásiť sa"
            dialogString = "Naozaj si prajete odhlásiť sa?"
        } else {
            viewHolder.button?.text = "Prihlásiť sa"
        }

        viewHolder.button?.setOnClickListener {

            AlertDialog.Builder(activity)
                    .setTitle("Upozornenie")
                    .setMessage(dialogString)
                    .setCancelable(false)
                    .setPositiveButton("Áno") { dialog: DialogInterface, _: Int ->
                        if (isAlreadyRegistered) {
                            val run = DataHolder.loggedUser?.runs?.first { s -> s.runId == run.runId }
                            DataHolder.loggedUser?.runs?.remove(run)
                        } else {
                            DataHolder.loggedUser?.runs?.add(getItem(position))
                        }

                        registerRun(isAlreadyRegistered, getItem(position).runId)

                        this.notifyDataSetChanged()

                        dialog.dismiss()
                    }
                    .setNegativeButton("Nie") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    .show()
        }
        return view
    }

    override fun getItem(i: Int): Run {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    private fun registerRun(isAlreadyRegistered: Boolean, runId: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (isAlreadyRegistered) {
                    callRest("/run/unregister/" + runId + "/" + DataHolder.loggedUser?.runnerId, "GET", null)
                } else {
                    callRest("/run/register/" + runId + "/" + DataHolder.loggedUser?.runnerId, "GET", null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}