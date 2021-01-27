package com.seal.collectnature;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.seal.adapter.ScienceNameAdapter;
import com.seal.model.Database;
import com.seal.model.Plant;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    ImageButton ibtnShowHome, ibtnShowCollection;
    Button btnSearch, btnAll;
    ListView lvScienName;
    EditText edtSearchScienceName;

    String searchScienceName = "";
    ArrayList<Plant> clonePlants = new ArrayList<>(), searchPlants = new ArrayList<>();

    public static ArrayList<Plant> plants;
    public static ScienceNameAdapter scienceNameAdapter;
    public static int selectedPlant;
    Database myDatabase = MainActivity.database;

    public static Activity colectionActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        colectionActivity = CollectionActivity.this;
        addControls();
        addData();
        createLvScienceName();
        addEvents();

        // delete all data from database
//        myDatabase.deleteData(myDatabase.getPLANT_COMMON_NAMES_TABLE(), "");
//        myDatabase.deleteData(myDatabase.getPLANT_DESCRIPTIONS_TABLE(), "");
//        myDatabase.deleteData(myDatabase.getPLANT_PICTURES_TABLE(), "");
//        myDatabase.deleteData(myDatabase.getPLANTS_TABLE(), "");


    }

    private void createLvScienceName() {
        scienceNameAdapter = new ScienceNameAdapter(
                CollectionActivity.this,
                R.layout.item_science_name,
                plants
        );
        lvScienName.setAdapter(scienceNameAdapter);
        scienceNameAdapter.notifyDataSetChanged();

        // add events
        lvScienName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPlant = position;
                Intent intent = new Intent(CollectionActivity.this, PlantActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addData() {
        plants = new ArrayList<>();

//        scienceNameAdapter = new ScienceNameAdapter(
//                CollectionActivity.this,
//                R.layout.item_science_name,
//                plants
//        );
//        lvScienName.setAdapter(scienceNameAdapter);
//        scienceNameAdapter.notifyDataSetChanged();

        // add data to arraylist_plants from database_plant.db
        // add data from table_plants
        Cursor cursor = myDatabase.selectDataOfPlantsTable();
        while (cursor.moveToNext()) {
            ArrayList<String> commonNames = new ArrayList<>();
            ArrayList<String> descriptions = new ArrayList<>();
            ArrayList<byte[]> pictures = new ArrayList<>();
            plants.add(new Plant(
                    cursor.getInt(0),
                    cursor.getString(1),
                    commonNames,
                    descriptions,
                    pictures
            ));
        }

        for(Plant plant : plants) {
            // add data from table plant_common_names
            Cursor cursor1 = myDatabase.selectDataOfCommonNamesTable(plant.getPlantId());
            ArrayList<String> commonNames = new ArrayList<>();
            try {
                while (cursor1.moveToNext()) {
                    String newCommonName = cursor1.getString(2);
                    if(!newCommonName.equals("")) {
                        commonNames.add(newCommonName);
                    }
                }
            } catch (Exception exception) {
            }
            plant.setCommonNames(commonNames);

            // add data from table plant_descriptions
            Cursor cursor2 = myDatabase.selectDataOfDescriptionsTable(plant.getPlantId());
            ArrayList<String> descriptions = new ArrayList<>();
            try {
                while (cursor2.moveToNext()) {
                    String newDescription = cursor2.getString(2);
                    if(!newDescription.equals("")) {
                        descriptions.add(newDescription);
                    }
                }
            } catch (Exception exception) {
            }
            plant.setDescriptions(descriptions);

            // add data from table plant_pictures
            Cursor cursor3 = myDatabase.selectDataOfPicturesTable(plant.getPlantId());
            ArrayList<byte[]> pictures = new ArrayList<>();
            try {
                while (cursor3.moveToNext()) {
                    pictures.add(cursor3.getBlob(2));
                }
            }
            catch (Exception exception) {
            }
            plant.setPictures(pictures);
        }

        // clone plants
        if(plants.size() > 0) {
            for (Plant plant : plants) {
                clonePlants.add(plant);
            }
        }
    }

    private void addEvents() {
        ibtnShowHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollectionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchScienceName = edtSearchScienceName.getText().toString().trim().toUpperCase();

                // sience name == ""
                if(searchScienceName.equals("")) {
                    Toast.makeText(CollectionActivity.this, "Enter Science Name to search ..." + searchScienceName, Toast.LENGTH_SHORT).show();
                }

                // sience name != ""
                else {
                    plants.clear();
                    searchPlants.clear();
                    if(clonePlants.size() > 0) {
                        for(Plant clonePlant : clonePlants) {
                           plants.add(clonePlant);
                        }
                        for(Plant plant : plants) {
                            if(plant.getScienceName().contains(searchScienceName)) {
                                searchPlants.add(plant);
                            }
                        }
                        if(searchPlants.size() == 0) {
                            Toast.makeText(CollectionActivity.this, "This plant has not existed !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            plants.clear();
                            for(Plant searchPlant : searchPlants) {
                                plants.add(searchPlant);
                            }
                        }
                    }
                }
                scienceNameAdapter.notifyDataSetChanged();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plants.clear();
                if(clonePlants.size() > 0) {
                    for(Plant clonePlant : clonePlants) {
                        plants.add(clonePlant);
                    }
                }
                scienceNameAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addControls() {
        ibtnShowHome = this.<ImageButton>findViewById(R.id.ibtnShowHome);
        ibtnShowCollection = this.<ImageButton>findViewById(R.id.ibtnShowCollection);
        btnSearch = this.<Button>findViewById(R.id.btnSearch);
        btnAll = this.<Button>findViewById(R.id.btnAll);
        edtSearchScienceName = this.<EditText>findViewById(R.id.edtSearchScienceName);
        lvScienName = this.<ListView>findViewById(R.id.lvScienName);
    }
}