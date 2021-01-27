package com.seal.collectnature;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seal.adapter.PictureAdapter;
import com.seal.model.Database;
import com.seal.model.Plant;

import java.util.ArrayList;

public class UpdatePlantActivity extends AppCompatActivity {
    ListView lvPictures;
    PictureAdapter pictureAdapter;
    EditText edtScienceName, edtCommonName, edtDescription;
    Button btnSave, btnCancel;
    ArrayList<byte[]> pictures;

    ArrayList<Plant> myPlants;
    int mySelectPlant;
    Database myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plant);

        addControls();
        addData();
        addEvents();
    }
    private void addData() {
        myPlants = CollectionActivity.plants;
        mySelectPlant = CollectionActivity.selectedPlant;
        myDatabase = MainActivity.database;

        // add science name
        String myScienceName = myPlants.get(mySelectPlant).getScienceName();
        edtScienceName.setText(myScienceName);

        // add common names
        String myCommonName = new String();
        ArrayList<String> commonNames = myPlants.get(mySelectPlant).getCommonNames();
        int size = commonNames.size();
        if(size > 0) {
            for(int i = 0; i < size - 1; i++) {
                myCommonName += commonNames.get(i) + " - ";
            }
            myCommonName += commonNames.get(size-1);
            myCommonName.trim();
            edtCommonName.setText(myCommonName);
        }
        else {
            edtCommonName.setText("");
        }

        // add descriptions
        String myDescription = new String();
        ArrayList<String> descriptions = myPlants.get(mySelectPlant).getDescriptions();
        int size1 = descriptions.size();
        if(size1 > 0) {
            for(int i = 0; i < size1 - 1; i++) {
                myDescription += descriptions.get(i) + " \n";
            }
            myDescription += descriptions.get(size1-1);
            myDescription.trim();
            edtDescription.setText(myDescription);
        }
        else {
            edtDescription.setText("");
        }

        // add pictures
        pictures = myPlants.get(mySelectPlant).getPictures(); // copy data

        // create list view pictures
        pictureAdapter = new PictureAdapter(UpdatePlantActivity.this, R.layout.item_picture, pictures);
        lvPictures.setAdapter(pictureAdapter);
        pictureAdapter.notifyDataSetChanged();
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from edit text
                String newScienceName = edtScienceName.getText().toString().trim().toUpperCase();
                String newCommonName = edtCommonName.getText().toString().trim().toUpperCase();
                String newDescription = edtDescription.getText().toString().trim();

                // delete old data
                boolean isDup = myDatabase.checkScienceName(newScienceName); // check if change science name

                String oldScienceName = myPlants.get(mySelectPlant).getScienceName();
                String commonNameTable = myDatabase.getPLANT_COMMON_NAMES_TABLE();
                String descriptionTable = myDatabase.getPLANT_DESCRIPTIONS_TABLE();
                int plantId = myPlants.get(mySelectPlant).getPlantId();

                if(!isDup) { // if change science name///////////////////////////////////////////////////////////////////////////////////////////
                    myDatabase.updateScienceName(UpdatePlantActivity.this, plantId, newScienceName);
                }
                //delete old common name
                myDatabase.deleteData(commonNameTable, "plant_id = " + "'" + plantId + "'");

                // delete old description
                myDatabase.deleteData(descriptionTable, "plant_id = " + "'" + plantId + "'");

                // insert new common name
                myDatabase.addNewCommonName(plantId, newCommonName);

                // insert new description
                myDatabase.addNewDescription(plantId, newDescription);

                Toast.makeText(UpdatePlantActivity.this, "Update Success !", Toast.LENGTH_SHORT).show();
                CollectionActivity.colectionActivity.finish();
                Intent intent = new Intent(UpdatePlantActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void openHome() {
        Intent intent = new Intent(UpdatePlantActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addControls() {
        lvPictures = this.<ListView>findViewById(R.id.lvPictures);

        edtCommonName = this.findViewById(R.id.edtCommonName);
        edtScienceName = this.findViewById(R.id.edtScienceName);
        edtDescription = this.findViewById(R.id.edtDescription);

        btnSave = this.<Button>findViewById(R.id.btnSave);
        btnCancel = this.<Button>findViewById(R.id.btnCancel);
    }
}