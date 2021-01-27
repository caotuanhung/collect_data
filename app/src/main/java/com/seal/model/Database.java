package com.seal.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.seal.collectnature.MainActivity;
import com.seal.collectnature.SavePictureActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Plant.db";
    private final String PLANTS_TABLE = "plants";
    private final String PLANT_COMMON_NAMES_TABLE = "plant_common_names";
    private final String PLANT_DESCRIPTIONS_TABLE = "plant_descriptions";
    private final String PLANT_PICTURES_TABLE = "plant_pictures";


    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        String plantsTable = "CREATE TABLE " + PLANTS_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "science_name STRING)";

        String plantCommonNamesTable = "CREATE TABLE " + PLANT_COMMON_NAMES_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "plant_id INTEGER, " +
                "common_name STRING, " +
                "FOREIGN KEY(plant_id) REFERENCES plants(id) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE)";

        String plantDescriptionsTable = "CREATE TABLE " + PLANT_DESCRIPTIONS_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "plant_id INTEGER, " +
                "description STRING, " +
                "FOREIGN KEY(plant_id) REFERENCES plants(id) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE)";

        String plantPicturesTable = "CREATE TABLE " + PLANT_PICTURES_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "plant_id INTEGER, " +
                "picture BLOB," +
                "FOREIGN KEY(plant_id) REFERENCES plants(id) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE)";

//        String plantCommonNamesTable = "CREATE TABLE " + PLANT_COMMON_NAMES_TABLE + "(" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "plant_id INTEGER, " +
//                "common_name STRING) ";
//
//        String plantDescriptionsTable = "CREATE TABLE " + PLANT_DESCRIPTIONS_TABLE + "(" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "plant_id INTEGER, " +
//                "description STRING) ";
//
//        String plantPicturesTable = "CREATE TABLE " + PLANT_PICTURES_TABLE + "(" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "plant_id INTEGER, " +
//                "picture BLOB)";

        db.execSQL(plantsTable);
        db.execSQL(plantCommonNamesTable);
        db.execSQL(plantDescriptionsTable);
        db.execSQL(plantPicturesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLANTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLANT_COMMON_NAMES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLANT_DESCRIPTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLANT_PICTURES_TABLE);
        onCreate(db);
    }

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public String getPLANTS_TABLE() {
        return PLANTS_TABLE;
    }

    public String getPLANT_COMMON_NAMES_TABLE() {
        return PLANT_COMMON_NAMES_TABLE;
    }

    public String getPLANT_DESCRIPTIONS_TABLE() {
        return PLANT_DESCRIPTIONS_TABLE;
    }

    public String getPLANT_PICTURES_TABLE() {
        return PLANT_PICTURES_TABLE;
    }

    public Cursor selectData(String tableName) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM " + tableName;
        return database.rawQuery(sql, null);

    }
    public Cursor selectData(String tableName, String condition) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM " + tableName + " WHERE " + condition;
        return database.rawQuery(sql, null);
    }

    public Cursor selectDataOfPlantsTable() {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM " + PLANTS_TABLE + " WHERE science_name IS NOT NULL";
        return database.rawQuery(sql, null);
    }

    public Cursor selectDataOfCommonNamesTable(long plantId) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM " + PLANT_COMMON_NAMES_TABLE + " WHERE plant_id = " + plantId;
        return database.rawQuery(sql, null);
    }

    public Cursor selectDataOfDescriptionsTable(long plantId) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM " + PLANT_DESCRIPTIONS_TABLE + " WHERE plant_id = " + plantId;
        return database.rawQuery(sql, null);
    }

    public Cursor selectDataOfPicturesTable(long plantId) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM " + PLANT_PICTURES_TABLE + " WHERE plant_id = " + plantId;
        return database.rawQuery(sql, null);
    }

    public boolean checkScienceName(String scienceName) {
        Cursor cursor = selectData(PLANTS_TABLE, "science_name = " + "'" + scienceName + "'");
        ArrayList<String> scienceNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            scienceNames.add(cursor.getString(1));
        }
        int size = scienceNames.size();
        boolean isDup = size > 0 ? true : false;
        return isDup;
    }

    public void insertDatabase(Context context, String scienceName, String commonName, String description, byte[] picture) {
        SQLiteDatabase database =getWritableDatabase();

        // check if the plant is existed
        Cursor cursor = selectData(PLANTS_TABLE, "science_name = " + "'" + scienceName + "'");
        ArrayList<Plant> plants = new ArrayList<>();
        while (cursor.moveToNext()) {
            plants.add(new Plant(
                    cursor.getInt(0),
                    cursor.getString(1)
            ));
        }
        int size = plants.size();
        if (size == 0) {
            // insert data to table plants
            String sql = "INSERT INTO " + PLANTS_TABLE + " VALUES(null, ?)";
            SQLiteStatement statement = database.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(1, scienceName);
            statement.executeInsert();

            // check if the plant is existed
            cursor = selectData(PLANTS_TABLE, "science_name = " + "'" + scienceName + "'");
            while (cursor.moveToNext()) {
                plants.add(new Plant(
                        cursor.getInt(0),
                        cursor.getString(1)
                ));
            }
            size = plants.size(); // size = 1 (allways)
            int plantId = plants.get(0).getPlantId();

            // insert data to table plant_common_names
            if(!commonName.equals("")) {
                String sql1 = "INSERT INTO " + PLANT_COMMON_NAMES_TABLE + " VALUES(null, ?, ?)";
                SQLiteStatement statement1 = database.compileStatement(sql1);
                statement1.clearBindings();
                statement1.bindLong(1, plantId);
                statement1.bindString(2, commonName);
                statement1.executeInsert();
            }

            // insert data to table plant_descriptions
            if(!description.equals("")) {
                String sql2 = "INSERT INTO " + PLANT_DESCRIPTIONS_TABLE + " VALUES(null, ?, ?)";
                SQLiteStatement statement2 = database.compileStatement(sql2);
                statement2.clearBindings();
                statement2.bindLong(1, plantId);
                statement2.bindString(2, description);
                statement2.executeInsert();
            }

            // insert data to table plant_pictures
            String sql3 = "INSERT INTO " + PLANT_PICTURES_TABLE + " VALUES(null, ?, ?)";
            SQLiteStatement statement3 = database.compileStatement(sql3);
            statement3.clearBindings();
            statement3.bindLong(1, plantId);
            statement3.bindBlob(2, picture);
            statement3.executeInsert();

        }
        else {
            size = plants.size(); // size = 1 (allways)
            int plantId = plants.get(0).getPlantId();

            // insert data to table plant_common_names
            if(!commonName.equals("")) {
                // check if save duplicate data
                ArrayList<String> commonNames = new ArrayList<>();
                boolean isDup = false;
                Cursor cursor1 = selectDataOfCommonNamesTable(plantId);
                while (cursor1.moveToNext()) {
                    String newCommonName = cursor1.getString(2);
                    commonNames.add(newCommonName);
                }
                for(String element : commonNames) {
                    if(element.equals(commonName)) {
                        isDup = true;
                        break;
                    }
                }

                // insert data if not duplicate
                if(!isDup) {
                    String sql1 = "INSERT INTO " + PLANT_COMMON_NAMES_TABLE + " VALUES(null, ?, ?)";
                    SQLiteStatement statement1 = database.compileStatement(sql1);
                    statement1.clearBindings();
                    statement1.bindLong(1, plantId);
                    statement1.bindString(2, commonName);
                    statement1.executeInsert();
                }
            }

            // insert data to table plant_descriptions
            if(!description.equals("")) {
                // check if save duplicate data
                ArrayList<String> descriptions = new ArrayList<>();
                boolean isDup = false;
                Cursor cursor1 = selectDataOfDescriptionsTable(plantId);
                while (cursor1.moveToNext()) {
                    String newDescription = cursor1.getString(2);
                    descriptions.add(newDescription);
                }
                for(String element : descriptions) {
                    if(element.equals(description)) {
                        isDup = true;
                        break;
                    }
                }

                // insert data if not duplicate
                if(!isDup) {
                    String sql2 = "INSERT INTO " + PLANT_DESCRIPTIONS_TABLE + " VALUES(null, ?, ?)";
                    SQLiteStatement statement2 = database.compileStatement(sql2);
                    statement2.clearBindings();
                    statement2.bindLong(1, plantId);
                    statement2.bindString(2, description);
                    statement2.executeInsert();
                }
            }

            // insert data to table plant_pictures
            String sql3 = "INSERT INTO " + PLANT_PICTURES_TABLE + " VALUES(null, ?, ?)";
            SQLiteStatement statement3 = database.compileStatement(sql3);
            statement3.clearBindings();
            statement3.bindLong(1, plantId);
            statement3.bindBlob(2, picture);
            statement3.executeInsert();
        }
        Toast.makeText(context, "Saved Success", Toast.LENGTH_SHORT).show();

    }

    public void addNewCommonName(int plantId, String newCommonName) {
        if(!newCommonName.equals("")) {
            SQLiteDatabase database =getWritableDatabase();
            String sql = "INSERT INTO " + PLANT_COMMON_NAMES_TABLE + " VALUES(null, ?, ?)";
            SQLiteStatement statement = database.compileStatement(sql);
            statement.clearBindings();
            statement.bindLong(1, plantId);
            statement.bindString(2, newCommonName);
            statement.executeInsert();
        }
    }

    public void addNewDescription(int plantId, String newDescription) {
        if(!newDescription.equals("")) {
            SQLiteDatabase database =getWritableDatabase();
            String sql = "INSERT INTO " + PLANT_DESCRIPTIONS_TABLE + " VALUES(null, ?, ?)";
            SQLiteStatement statement = database.compileStatement(sql);
            statement.clearBindings();
            statement.bindLong(1, plantId);
            statement.bindString(2, newDescription);
            statement.executeInsert();
        }
    }

    public void updateScienceName(Context context, int plantId, String newScienceName) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE " + PLANTS_TABLE + " SET science_name = " + "'" + newScienceName + "'" + " WHERE id = " + plantId;
        try {
            database.execSQL(sql);
        }
        catch (Exception exception) {
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(String tableName, String condition) {
        SQLiteDatabase database =getWritableDatabase();
        if(condition.equals("")) {
            String sql = "DELETE FROM " + tableName;
            database.execSQL(sql);
        }
        else {
            String sql = "DELETE FROM " + tableName + " WHERE " + condition;
            database.execSQL(sql);
        }
        database.close();
    }
}
