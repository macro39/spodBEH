package com.example.spodbeh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_bottom_nav.*

class BottomNavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SearchFragment()).commit()

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            var selected: Fragment? = null

            when (item.itemId) {
//                R.id.nav_home -> selected = HomeFragment()
                R.id.nav_add -> selected = AddFragment()
                R.id.nav_search -> selected = SearchFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selected!!).commit()

            true
        }
    }
}
