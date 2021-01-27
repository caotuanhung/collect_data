package com.seal.collectnature;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.seal.model.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ImageButton ibtnTakePicture, ibtnSelectPicture, ibtnShowCollection;
    final int REQUEST_CODE_CAMERA = 3113;
    final int REQUEST_CODE_FOLDER = 6151;
    final int VERSION_OF_DATABASE = 1;
    public static Bitmap bitmap;
    public static Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addDatabase();
        addEvents();

//        // check the path of database
//        String path = MainActivity.this.getDatabasePath("Plant.db").toString();
//        TextView textView3 = findViewById(R.id.textView3);
//        textView3.setText(path);
    }

    private void addDatabase() {
        database = new Database(MainActivity.this);
    }

    private void addEvents() {
        ibtnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        ibtnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolder();
            }
        });

        ibtnShowCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void openFolder() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOLDER);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(
                requestCode == REQUEST_CODE_CAMERA &&
                resultCode == RESULT_OK &&
                data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");

            // open activity "save picture"
            Intent intent = new Intent(MainActivity.this, SavePictureActivity.class);
            startActivity(intent);
        }

        if(
                requestCode == REQUEST_CODE_FOLDER &&
                resultCode == RESULT_OK &&
                data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);

                // open activity "save picture"
                Intent intent = new Intent(MainActivity.this, SavePictureActivity.class);
                startActivity(intent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addControls() {
        ibtnTakePicture = findViewById(R.id.ibtnTakePicture);
        ibtnSelectPicture = findViewById(R.id.ibtnSelectPicture);
        ibtnShowCollection = findViewById(R.id.ibtnShowCollection);
    }
}