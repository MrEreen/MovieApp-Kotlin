package com.example.movieapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movieapp.fragment.HomeFragment
import com.example.movieapp.fragment.ProfileFragment
import com.example.movieapp.fragment.CommentsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getStringExtra("userId") ?: ""
        userName = intent.getStringExtra("userName") ?: "Guest"

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment.newInstance(userId, userName))
                    true
                }
                R.id.navigation_comments -> {
                    loadFragment(CommentsFragment())
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment.newInstance(userId, userName))
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            val profileFragment = ProfileFragment.newInstance(userId, userName)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .commit()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
