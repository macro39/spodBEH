package com.example.spodbeh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat


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

        textView_home_full_name.text = DataHolder.loggedUser?.firstName + " " + DataHolder.loggedUser?.lastName
        textView_home_email.text = DataHolder.loggedUser?.email
        textView_home_sex.text = DataHolder.loggedUser?.sex
        textView_home_telephone.text = DataHolder.loggedUser?.telephoneNumber
        textView_home_birthday.text = SimpleDateFormat("dd/MM/yyyy").format(DataHolder.loggedUser?.dateOfBirth)
    }
}