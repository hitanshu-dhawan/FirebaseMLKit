package com.hitanshudhawan.firebasemlkitexample.languageidentification

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.hitanshudhawan.firebasemlkitexample.R

import kotlinx.android.synthetic.main.activity_language_identification.*

class LanguageIdentificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_identification)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

}
