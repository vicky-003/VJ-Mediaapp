package com.example.social_mediavj

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.social_mediavj.databinding.ActivityMainBinding
import com.example.social_mediavj.ui.HomeFragment
import com.example.social_mediavj.ui.NotificationsFragment
import com.example.social_mediavj.ui.ProfileFragment
import com.example.social_mediavj.ui.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    internal var selectedfragment: Fragment? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                selectedfragment = HomeFragment()
            }
            R.id.nav_search -> {
                selectedfragment = SearchFragment()
            }
            R.id.nav_add_post -> {
                item.isChecked = false
               // startActivity(Intent(this@MainActivity, AddPostActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notification -> {
                selectedfragment = NotificationsFragment()
            }
            R.id.nav_profile -> {
                selectedfragment = ProfileFragment()
            }
        }

        if(selectedfragment != null){
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                selectedfragment!!
            ).commit()
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
       // moveToFragment(HomeFragment())
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            HomeFragment()
        ).commit()

    }

    private fun moveToFragment(fragment: Fragment)
    {
        val fragmentTrans = supportFragmentManager.beginTransaction()
      //  fragmentTrans.replace(R.id.fragment_container, fragment)
      //  fragmentTrans.commit()
    }
}