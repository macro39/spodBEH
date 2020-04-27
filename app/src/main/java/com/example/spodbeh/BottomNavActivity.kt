package com.example.spodbeh

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_bottom_nav.*

class BottomNavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            var selected: Fragment? = null

            when (item.itemId) {
                R.id.nav_home -> selected = HomeFragment()
                R.id.nav_add -> selected = AddFragment()
                R.id.nav_search -> selected = SearchFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selected!!).commit()

            true
        }
    }

    override fun onBackPressed() {
        logout()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_logout -> {
                logout()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        DataHolder.loggedUser = null

        Toast.makeText(this, "Boli ste odhlásený", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
