package com.hitanshudhawan.mlkitexamples;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FaceDetectionActivity extends AppCompatActivity {

    private ImageView imageView;

    private String mCurrentPhotoPath;
    private static final int CAMERA_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);
        setTitle("Face Detection");

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
                        Uri photoURI = FileProvider.getUriForFile(FaceDetectionActivity.this,
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
            addEmojis(photo);
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

    private void addEmojis(Bitmap photo) {
        final Bitmap emojiPhoto = photo.copy(Bitmap.Config.ARGB_8888, true);
        final Canvas canvas = new Canvas(emojiPhoto);

        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.1f)
                .setTrackingEnabled(false)
                .build();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);
        Task<List<FirebaseVisionFace>> result = detector.detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<FirebaseVisionFace>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionFace> faces) {
                                // Task completed successfully
                                for (FirebaseVisionFace face : faces) {

                                    FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                                    FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
                                    FirebaseVisionFaceLandmark nose = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);
                                    FirebaseVisionFaceLandmark mouth = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_MOUTH);

                                    if (leftEye != null) {
                                        FirebaseVisionPoint leftEyePosition = leftEye.getPosition();
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.eye);
                                        int width = bitmap.getWidth() * 3;
                                        int height = bitmap.getHeight() * 3;
                                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                                        canvas.drawBitmap(bitmap, leftEyePosition.getX() - width / 2, leftEyePosition.getY() - height / 2, null);
                                    }

                                    if (rightEye != null) {
                                        FirebaseVisionPoint rightEyePosition = rightEye.getPosition();
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.eye);
                                        int width = bitmap.getWidth() * 3;
                                        int height = bitmap.getHeight() * 3;
                                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                                        canvas.drawBitmap(bitmap, rightEyePosition.getX() - width / 2, rightEyePosition.getY() - height / 2, null);
                                    }

                                    if (nose != null) {
                                        FirebaseVisionPoint nosePosition = nose.getPosition();
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.nose);
                                        int width = bitmap.getWidth() * 3;
                                        int height = bitmap.getHeight() * 3;
                                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                                        canvas.drawBitmap(bitmap, nosePosition.getX() - width / 2, nosePosition.getY() - height, null);
                                    }

                                    if (mouth != null) {
                                        FirebaseVisionPoint mouthPosition = mouth.getPosition();
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mouth);
                                        int width = bitmap.getWidth() * 4;
                                        int height = bitmap.getHeight() * 4;
                                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                                        canvas.drawBitmap(bitmap, mouthPosition.getX() - width, mouthPosition.getY() - height / 2, null);
                                    }


                                    canvas.save();
                                    imageView.setImageBitmap(emojiPhoto);
                                }

                            }
                        });
    }
}
