package com.hitanshudhawan.firebasemlkitexample.facedetection

import android.app.Activity
import android.content.Intent
import android.graphics.*
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
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import com.hitanshudhawan.firebasemlkitexample.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_face_detection.*

class FaceDetectionActivity : AppCompatActivity() {

    private val imageView by lazy { findViewById<ImageView>(R.id.face_detection_image_view)!! }

    private val bottomSheetButton by lazy { findViewById<FrameLayout>(R.id.bottom_sheet_button)!! }
    private val bottomSheetRecyclerView by lazy { findViewById<RecyclerView>(R.id.bottom_sheet_recycler_view)!! }
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(findViewById(R.id.bottom_sheet)!!) }

    private val faceDetectionModels = ArrayList<FaceDetectionModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bottomSheetButton.setOnClickListener {
            CropImage.activity().start(this)
        }

        bottomSheetRecyclerView.layoutManager = LinearLayoutManager(this)
        bottomSheetRecyclerView.adapter = FaceDetectionAdapter(this, faceDetectionModels)
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
        faceDetectionModels.clear()
        bottomSheetRecyclerView.adapter?.notifyDataSetChanged()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showProgress()

        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
        val options = FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build()
        val faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        faceDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener {
                    val mutableImage = image.copy(Bitmap.Config.ARGB_8888, true)

                    detectFaces(it, mutableImage)

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

    private fun detectFaces(faces: List<FirebaseVisionFace>?, image: Bitmap?) {
        if (faces == null || image == null) {
            Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
            return
        }

        val canvas = Canvas(image)
        val facePaint = Paint()
        facePaint.color = Color.RED
        facePaint.style = Paint.Style.STROKE
        facePaint.strokeWidth = 8F
        val faceTextPaint = Paint()
        faceTextPaint.color = Color.RED
        faceTextPaint.textSize = 40F
        faceTextPaint.typeface = Typeface.DEFAULT_BOLD
        val landmarkPaint = Paint()
        landmarkPaint.color = Color.RED
        landmarkPaint.style = Paint.Style.FILL
        landmarkPaint.strokeWidth = 8F

        for ((index, face) in faces.withIndex()) {

            canvas.drawRect(face.boundingBox, facePaint)
            canvas.drawText("Face$index", (face.boundingBox.centerX() - face.boundingBox.width() / 2) + 8F, (face.boundingBox.centerY() + face.boundingBox.height() / 2) - 8F, faceTextPaint)

            if (face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE) != null) {
                val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)!!
                canvas.drawCircle(leftEye.position.x, leftEye.position.y, 8F, landmarkPaint)
            }
            if (face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE) != null) {
                val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)!!
                canvas.drawCircle(rightEye.position.x, rightEye.position.y, 8F, landmarkPaint)
            }
            if (face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE) != null) {
                val nose = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)!!
                canvas.drawCircle(nose.position.x, nose.position.y, 8F, landmarkPaint)
            }
            if (face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR) != null) {
                val leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)!!
                canvas.drawCircle(leftEar.position.x, leftEar.position.y, 8F, landmarkPaint)
            }
            if (face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR) != null) {
                val rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR)!!
                canvas.drawCircle(rightEar.position.x, rightEar.position.y, 8F, landmarkPaint)
            }
            if (face.getLandmark(FirebaseVisionFaceLandmark.LEFT_MOUTH) != null && face.getLandmark(FirebaseVisionFaceLandmark.BOTTOM_MOUTH) != null && face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_MOUTH) != null) {
                val leftMouth = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_MOUTH)!!
                val bottomMouth = face.getLandmark(FirebaseVisionFaceLandmark.BOTTOM_MOUTH)!!
                val rightMouth = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_MOUTH)!!
                canvas.drawLine(leftMouth.position.x, leftMouth.position.y, bottomMouth.position.x, bottomMouth.position.y, landmarkPaint)
                canvas.drawLine(bottomMouth.position.x, bottomMouth.position.y, rightMouth.position.x, rightMouth.position.y, landmarkPaint)
            }

            faceDetectionModels.add(FaceDetectionModel(index, "Smiling Probability  ${face.smilingProbability}"))
            faceDetectionModels.add(FaceDetectionModel(index, "Left Eye Open Probability  ${face.leftEyeOpenProbability}"))
            faceDetectionModels.add(FaceDetectionModel(index, "Right Eye Open Probability  ${face.rightEyeOpenProbability}"))
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
