package com.seal.collectnature;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

public class PlantActivity extends AppCompatActivity {
    ListView lvPictures;
    TextView txtScienceName, txtCommonName, txtDescription;
    Button btnUpdate, btnDelete;
    ImageButton ibtnShowHome;

    PictureAdapter pictureAdapter;

    public static ArrayList<byte[]> pictures;
    public static int selectedPicture;

    ArrayList<Plant> myPlants;
    int mySelectPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        addControls();
        addData();
        addEvents();
    }

    private void addData() {
        myPlants = CollectionActivity.plants;
        mySelectPlant = CollectionActivity.selectedPlant;

        // add science name
        String myScienceName = myPlants.get(mySelectPlant).getScienceName();
        txtScienceName.setText(myScienceName);

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
            txtCommonName.setText(myCommonName);
        }
        else {
            txtCommonName.setText("");
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
            txtDescription.setText(myDescription);
        }
        else {
            txtDescription.setText("");
        }

        // add pictures
        // copy data
        pictures = myPlants.get(mySelectPlant).getPictures();

        // create list view pictures
        pictureAdapter = new PictureAdapter(PlantActivity.this, R.layout.item_picture, pictures);
        lvPictures.setAdapter(pictureAdapter);
        pictureAdapter.notifyDataSetChanged();

        // open picture
        lvPictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPicture = position;
                Intent intent = new Intent(PlantActivity.this, PictureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addEvents() {
        ibtnShowHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantActivity.this, UpdatePlantActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add data
                createDialogDelete();
//                Database myDatabase = MainActivity.database;
//                String plantTable = myDatabase.getPLANTS_TABLE();
//                String commonNameTable = myDatabase.getPLANT_COMMON_NAMES_TABLE();
//                String descriptionTable = myDatabase.getPLANT_DESCRIPTIONS_TABLE();
//                String pictureTable = myDatabase.getPLANT_PICTURES_TABLE();
//                int plantId = myPlants.get(mySelectPlant).getPlantId();
//
//                //delete common name
//                myDatabase.deleteData(commonNameTable, "plant_id = " + "'" + plantId + "'");
//
//                // delete description
//                myDatabase.deleteData(descriptionTable, "plant_id = " + "'" + plantId + "'");
//
//                // delete picture
//                myDatabase.deleteData(pictureTable, "plant_id = " + "'" + plantId + "'");
//
//                // delete plant
//                myDatabase.deleteData(plantTable, "id = " + "'" + plantId + "'");
//
//                Toast.makeText(PlantActivity.this, "Delete Success !", Toast.LENGTH_SHORT).show();
//                CollectionActivity.colectionActivity.finish();
//                Intent intent = new Intent(PlantActivity.this, CollectionActivity.class);
//                startActivity(intent);
//                finish();
            }

            private void createDialogDelete() {
                AlertDialog.Builder newBuilder = new AlertDialog.Builder(PlantActivity.this);
                newBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                newBuilder.setTitle("");
                newBuilder.setMessage("Do you really want to delete ?");

                newBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database myDatabase = MainActivity.database;
                        String plantTable = myDatabase.getPLANTS_TABLE();
                        String commonNameTable = myDatabase.getPLANT_COMMON_NAMES_TABLE();
                        String descriptionTable = myDatabase.getPLANT_DESCRIPTIONS_TABLE();
                        String pictureTable = myDatabase.getPLANT_PICTURES_TABLE();
                        int plantId = myPlants.get(mySelectPlant).getPlantId();

                        //delete common name
                        myDatabase.deleteData(commonNameTable, "plant_id = " + "'" + plantId + "'");

                        // delete description
                        myDatabase.deleteData(descriptionTable, "plant_id = " + "'" + plantId + "'");

                        // delete picture
                        myDatabase.deleteData(pictureTable, "plant_id = " + "'" + plantId + "'");

                        // delete plant
                        myDatabase.deleteData(plantTable, "id = " + "'" + plantId + "'");

                        Toast.makeText(PlantActivity.this, "Delete Success !", Toast.LENGTH_SHORT).show();
                        CollectionActivity.colectionActivity.finish();
                        Intent intent = new Intent(PlantActivity.this, CollectionActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }
                });

                newBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

            //newBuilder.setCancelable(false); tac dung co ve giong CancledOnTouchOutside, nhung chua hieu ro

                AlertDialog deleteDialog = newBuilder.create();
                deleteDialog.setCanceledOnTouchOutside(false);
                deleteDialog.show();
            }
        });

    }

    private void openHome() {
        Intent intent = new Intent(PlantActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addControls() {
        lvPictures = this.<ListView>findViewById(R.id.lvPictures);

        txtScienceName = this.<TextView>findViewById(R.id.txtScienceName);
        txtCommonName = this.<TextView>findViewById(R.id.txtCommonName);
        txtDescription = this.<TextView>findViewById(R.id.txtDescription);

        btnUpdate = this.<Button>findViewById(R.id.btnUpdate);
        btnDelete = this.<Button>findViewById(R.id.btnDelete);
        ibtnShowHome = this.<ImageButton>findViewById(R.id.ibtnShowHome);

        // add scrollbar
        txtScienceName.setMovementMethod(new ScrollingMovementMethod());
        txtCommonName.setMovementMethod(new ScrollingMovementMethod());
        txtDescription.setMovementMethod(new ScrollingMovementMethod());
    }
}