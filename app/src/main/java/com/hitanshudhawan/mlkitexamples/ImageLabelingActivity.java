package com.hitanshudhawan.mlkitexamples;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImageLabelingActivity extends AppCompatActivity {

    private ImageView imageView;

    private String mCurrentPhotoPath;
    private static final int CAMERA_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_labeling);
        setTitle("Image Labeling");

        imageView = findViewById(R.id.image_view);

        findViewById(R.id.button_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(ImageLabelingActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            File imageFile = new File(mCurrentPhotoPath);
            Bitmap photo = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            labelImage(photo);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void labelImage(Bitmap photo) {
        if (photo == null) return;
        final Bitmap labelPhoto = photo.copy(Bitmap.Config.ARGB_8888, true);
        final Canvas canvas = new Canvas(labelPhoto);

        FirebaseVisionLabelDetectorOptions options =
                new FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(0.5f)
                        .build();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);
        FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                .getVisionLabelDetector(options);
        Task<List<FirebaseVisionLabel>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionLabel> labels) {
                                        // Task completed successfully
                                        String maxText = "";
                                        float maxConfidence = 0;
                                        for (FirebaseVisionLabel label : labels) {
                                            String text = label.getLabel();
                                            String entityId = label.getEntityId();
                                            float confidence = label.getConfidence();

                                            if (confidence >= maxConfidence) {
                                                maxText = text;
                                                maxConfidence = confidence;
                                            }
                                        }

                                        Paint paint = new Paint();
                                        paint.setColor(Color.RED);
                                        paint.setTextSize(200);
                                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                                        canvas.drawText(maxText, 20, 200, paint);

                                        canvas.save();
                                        imageView.setImageBitmap(labelPhoto);
                                    }
                                });
    }
}