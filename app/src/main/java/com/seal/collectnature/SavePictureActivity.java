package com.seal.collectnature;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class SavePictureActivity extends AppCompatActivity {

    ImageView imgPlant;
    EditText edtScienceName, edtCommonName, edtDescription;
    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_picture);

        addControls();
        addData();
        addEvents();
    }

    private void addData() {
        imgPlant.setImageBitmap(MainActivity.bitmap);
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlant();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SavePictureActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void savePlant() {
        String scienceName = edtScienceName.getText().toString().trim().toUpperCase();
        String commonName = edtCommonName.getText().toString().trim().toUpperCase();
        String description = edtDescription.getText().toString().trim();
        byte[] picture;

        // convert picture from bitmap to byte
        final int QUALITY_IMAGE = 100;
        BitmapDrawable bitmapDrawble = (BitmapDrawable) imgPlant.getDrawable();
        Bitmap bitmap = bitmapDrawble.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY_IMAGE, byteArrayOutputStream);
        picture = byteArrayOutputStream.toByteArray();

        // insert plant to database
        MainActivity.database.insertDatabase(SavePictureActivity.this, scienceName, commonName, description, picture);
        Intent intent = new Intent(SavePictureActivity.this, CollectionActivity.class);
        startActivity(intent);
        finish();
    }

    private void addControls() {
        imgPlant = this.<ImageView>findViewById(R.id.imgPlant);
        edtScienceName = findViewById(R.id.edtScienceName);
        edtCommonName = this.<EditText>findViewById(R.id.edtCommonName);
        edtDescription = this.<EditText>findViewById(R.id.edtDescription);
        btnSave = this.<Button>findViewById(R.id.btnSave);
        btnCancel = this.<Button>findViewById(R.id.btnCancel);
    }
}