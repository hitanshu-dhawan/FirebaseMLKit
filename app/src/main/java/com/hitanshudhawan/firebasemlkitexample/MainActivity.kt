package com.hitanshudhawan.firebasemlkitexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.firebase_ml_kit)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        main_activity_recycler_view.layoutManager = LinearLayoutManager(this)
        main_activity_recycler_view.adapter = MainActivityAdapter(this)
    }
}
