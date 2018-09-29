package com.hitanshudhawan.firebasemlkitexample.imagelabeling

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.hitanshudhawan.firebasemlkitexample.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_image_labeling.*

class ImageLabelingActivity : AppCompatActivity() {

    private val imageView by lazy { findViewById<ImageView>(R.id.image_labeling_image_view)!! }

    private val bottomSheetButton by lazy { findViewById<FrameLayout>(R.id.bottom_sheet_button)!! }
    private val bottomSheetRecyclerView by lazy { findViewById<RecyclerView>(R.id.bottom_sheet_recycler_view)!! }
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(findViewById(R.id.bottom_sheet)!!) }

    private val imageLabelingModels = ArrayList<ImageLabelingModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_labeling)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bottomSheetButton.setOnClickListener {
            CropImage.activity().start(this)
        }

        bottomSheetRecyclerView.layoutManager = LinearLayoutManager(this)
        bottomSheetRecyclerView.adapter = ImageLabelingAdapter(this, imageLabelingModels)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                val imageUri = result.uri
                analyzeImage(MediaStore.Images.Media.getBitmap(contentResolver, imageUri))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "There was some error : ${result.error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun analyzeImage(image: Bitmap?) {
        if (image == null) {
            Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
            return
        }

        imageView.setImageBitmap(null)
        imageLabelingModels.clear()
        bottomSheetRecyclerView.adapter?.notifyDataSetChanged()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showProgress()

        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
        val options = FirebaseVisionLabelDetectorOptions.Builder()
                .setConfidenceThreshold(0.7F)
                .build()
        val labelDetector = FirebaseVision.getInstance().getVisionLabelDetector(options)
        labelDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener {
                    val mutableImage = image.copy(Bitmap.Config.ARGB_8888, true)

                    labelImage(it, mutableImage)

                    imageView.setImageBitmap(mutableImage)
                    hideProgress()
                    bottomSheetRecyclerView.adapter?.notifyDataSetChanged()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                .addOnFailureListener {
                    Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
                    hideProgress()
                }
    }

    private fun labelImage(labels: List<FirebaseVisionLabel>?, image: Bitmap?) {
        if (labels == null || image == null) {
            Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
            return
        }

        for (label in labels) {

            imageLabelingModels.add(ImageLabelingModel(label.label, label.confidence))
        }
    }

    private fun showProgress() {
        findViewById<View>(R.id.bottom_sheet_button_image).visibility = View.GONE
        findViewById<View>(R.id.bottom_sheet_button_progress).visibility = View.VISIBLE
    }

    private fun hideProgress() {
        findViewById<View>(R.id.bottom_sheet_button_image).visibility = View.VISIBLE
        findViewById<View>(R.id.bottom_sheet_button_progress).visibility = View.GONE
    }

}
