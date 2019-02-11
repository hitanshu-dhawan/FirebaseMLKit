package com.hitanshudhawan.firebasemlkitexample.languageidentification

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentificationOptions
import com.hitanshudhawan.firebasemlkitexample.R
import kotlinx.android.synthetic.main.activity_language_identification.*

class LanguageIdentificationActivity : AppCompatActivity() {

    private val editText by lazy { findViewById<EditText>(R.id.language_identification_edit_text) }
    private val button by lazy { findViewById<Button>(R.id.language_identification_button) }
    private val singleLanguageTextView by lazy { findViewById<TextView>(R.id.language_identification_single_language_text_view) }
    private val multipleLanguageTextView by lazy { findViewById<TextView>(R.id.language_identification_multiple_language_text_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_identification)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button.setOnClickListener {
            identifyLanguage(editText.text.toString())
        }
    }

    private fun identifyLanguage(text: String) {
        val options = FirebaseLanguageIdentificationOptions.Builder()
                .setConfidenceThreshold(0.1F)
                .build()
        val languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification(options)
        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener {
                    singleLanguageTextView.text = "Language: $it"
                }
                .addOnFailureListener {
                    Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
                }
        languageIdentifier.identifyPossibleLanguages(text)
                .addOnSuccessListener { languages ->
                    var possibleLanguages = ""
                    for ((index, language) in languages.withIndex()) {
                        possibleLanguages += if (index < languages.size - 1) "${language.languageCode}," else language.languageCode
                    }
                    multipleLanguageTextView.text = "Possible Languages: $possibleLanguages"
                }
                .addOnFailureListener {
                    Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
                }
    }
}
