package com.example.spodbeh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * Created by Kamil Macek on 23.4.2020.
 */
class HomeFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView_home_first_name.text = DataHolder.loggedUser?.firstName
        textView_home_last_name.text = DataHolder.loggedUser?.lastName
    }
}