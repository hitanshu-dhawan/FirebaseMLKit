package com.hitanshudhawan.firebasemlkitexample.custommodels

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
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
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.*
import com.hitanshudhawan.firebasemlkitexample.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_custom_models.*

class CustomModelsActivity : AppCompatActivity() {

    private val outputData = arrayOf("affenpinscher", "afghan hound", "african hunting dog", "airedale", "american staffordshire terrier", "appenzeller", "australian terrier", "basenji", "basset", "beagle", "bedlington terrier", "bernese mountain dog", "black-and-tan coonhound", "blenheim spaniel", "bloodhound", "bluetick", "border collie", "border terrier", "borzoi", "boston bull", "bouvier des flandres", "boxer", "brabancon griffon", "briard", "brittany spaniel", "bull mastiff", "cairn", "cardigan", "chesapeake bay retriever", "chihuahua", "chow", "clumber", "cocker spaniel", "collie", "curly-coated retriever", "dandie dinmont", "dhole", "dingo", "doberman", "english foxhound", "english setter", "english springer", "entlebucher", "eskimo dog", "flat-coated retriever", "french bulldog", "german shepherd", "german short-haired pointer", "giant schnauzer", "golden retriever", "gordon setter", "great dane", "great pyrenees", "greater swiss mountain dog", "groenendael", "ibizan hound", "irish setter", "irish terrier", "irish water spaniel", "irish wolfhound", "italian greyhound", "japanese spaniel", "keeshond", "kelpie", "kerry blue terrier", "komondor", "kuvasz", "labrador retriever", "lakeland terrier", "leonberg", "lhasa", "malamute", "malinois", "maltese dog", "mexican hairless", "miniature pinscher", "miniature poodle", "miniature schnauzer", "newfoundland", "norfolk terrier", "norwegian elkhound", "norwich terrier", "old english sheepdog", "otterhound", "papillon", "pekinese", "pembroke", "pomeranian", "pug", "redbone", "rhodesian ridgeback", "rottweiler", "saint bernard", "saluki", "samoyed", "schipperke", "scotch terrier", "scottish deerhound", "sealyham terrier", "shetland sheepdog", "shih-tzu", "siberian husky", "silky terrier", "soft-coated wheaten terrier", "staffordshire bullterrier", "standard poodle", "standard schnauzer", "sussex spaniel", "tibetan mastiff", "tibetan terrier", "toy poodle", "toy terrier", "vizsla", "walker hound", "weimaraner", "welsh springer spaniel", "west highland white terrier", "whippet", "wire-haired fox terrier", "yorkshire terrier")

    private val imageView by lazy { findViewById<ImageView>(R.id.custom_models_image_view)!! }

    private val bottomSheetButton by lazy { findViewById<FrameLayout>(R.id.bottom_sheet_button)!! }
    private val bottomSheetRecyclerView by lazy { findViewById<RecyclerView>(R.id.bottom_sheet_recycler_view)!! }
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(findViewById(R.id.bottom_sheet)!!) }

    private val customModelsModels = ArrayList<CustomModelsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_models)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bottomSheetButton.setOnClickListener {
            CropImage.activity().start(this)
        }

        bottomSheetRecyclerView.layoutManager = LinearLayoutManager(this)
        bottomSheetRecyclerView.adapter = CustomModelsAdapter(this, customModelsModels)

        Toast.makeText(this, "Take/Pick an image of a Dog", Toast.LENGTH_LONG).show()
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
        customModelsModels.clear()
        bottomSheetRecyclerView.adapter?.notifyDataSetChanged()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showProgress()

        // Configure a local model source
        val localSource = FirebaseLocalModel.Builder("my_local_model")
                .setAssetFilePath("my_model.tflite")
                .build()
        FirebaseModelManager.getInstance().registerLocalModel(localSource)

        // Create an interpreter from your model sources
        val options = FirebaseModelOptions.Builder()
                //.setCloudModelName("my_cloud_model")
                .setLocalModelName("my_local_model")
                .build()
        val interpreter = FirebaseModelInterpreter.getInstance(options)

        // Specify the model's input and output
        val inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
                .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 224, 224, 3))
                .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 120))
                .build()

        // Perform inference on input data
        val input = getImagePixelData(image)
        val inputs = FirebaseModelInputs.Builder()
                .add(input)
                .build()
        interpreter!!.run(inputs, inputOutputOptions)
                .addOnSuccessListener { result ->
                    val output = result.getOutput<Array<FloatArray>>(0)
                    val probabilities = output[0]
                    for ((index, probability) in probabilities.withIndex()) {
                        if (probability > 0.5)
                            customModelsModels.add(CustomModelsModel(outputData[index], probability))
                        customModelsModels.sortWith(Comparator { o1, o2 ->
                            when {
                                o1.confidence == o2.confidence -> 0
                                o1.confidence < o2.confidence -> 1
                                else -> -1
                            }
                        })
                    }

                    imageView.setImageBitmap(image)
                    hideProgress()
                    bottomSheetRecyclerView.adapter?.notifyDataSetChanged()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                .addOnFailureListener {
                    Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show()
                    hideProgress()
                }
    }

    private fun getImagePixelData(image: Bitmap): Array<Array<Array<FloatArray>>> {
        val scaledImage = Bitmap.createScaledBitmap(image, 224, 224, false)

        val batchNum = 0
        val input = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }
        for (x in 0..223) {
            for (y in 0..223) {
                val pixel = scaledImage.getPixel(x, y)
                // Normalize channel values to [0.0, 1.0]. This requirement varies by
                // model. For example, some models might require values to be normalized
                // to the range [-1.0, 1.0] instead.
                input[batchNum][x][y][0] = Color.red(pixel) / 255.0f
                input[batchNum][x][y][1] = Color.green(pixel) / 255.0f
                input[batchNum][x][y][2] = Color.blue(pixel) / 255.0f
            }
        }

        return input
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