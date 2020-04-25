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
import android.widget.Toast
import com.example.spodbeh.model.Run
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

        viewHolder.button?.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setTitle("Upozornenie")
                    .setMessage("Naozaj si prajete prihlásiť sa?")
                    .setCancelable(false)
                    .setPositiveButton("Áno") { dialog: DialogInterface, _: Int ->
                        val selected = getItem(position)
                        Toast.makeText(activity, "REGISTERED " + selected.name, Toast.LENGTH_SHORT).show()

                        items.remove(selected)
                        activity.findViewById<TextView>(R.id.textView_search_results_count).text = Constants.RESULTS_TEXT + items.size

                        if (items.size == 0) {
                            activity.findViewById<TextView>(R.id.textView_search_no_data).visibility = View.VISIBLE
                        }

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

    fun changeData(list: ArrayList<Run>) {
        for (item in items) {
            items.remove(item)
        }
        items.addAll(list)
        this.notifyDataSetChanged()
    }
}