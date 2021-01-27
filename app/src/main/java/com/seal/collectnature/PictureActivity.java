package com.seal.collectnature;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class PictureActivity extends AppCompatActivity {
    ImageView imgZoomPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        addControls();
        addData();
    }

    private void addData() {
        int myPicture = PlantActivity.selectedPicture;

        // convert picture form byte[] to bitmap
        byte[] picture = PlantActivity.pictures.get(myPicture);
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        imgZoomPlant.setImageBitmap(bitmap);
    }

    private void addControls() {
        imgZoomPlant = findViewById(R.id.imgZoomPlant);
    }
}