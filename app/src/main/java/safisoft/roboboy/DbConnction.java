package safisoft.roboboy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by SafiSoft on 3/19/2021.
 */

public class DbConnction extends SQLiteOpenHelper {

    String DB_PATH = null;
    private static String DB_NAME = "Robo_Project_Control";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DbConnction(Context context) {
        super(context, DB_NAME, null, 10);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path 1", DB_PATH);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        super.onOpen(myDataBase);
        myDataBase.disableWriteAheadLogging();

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {//fix database problem on api28+
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    public Cursor Query_Data(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query(table,null,null,null,null,null,null);
    }

    public Cursor Row_Query(String Tapel_name,String column_name,String data){
        return myDataBase.rawQuery("SELECT * FROM " + Tapel_name + " WHERE " + column_name + "=?", new String[] { data });
    }

    public Cursor Search_Query(String Tapel_name,String column_name,String val){
        return myDataBase.rawQuery("SELECT * FROM "+Tapel_name+" WHERE "+column_name+" like '%"+val+"%'",null);
    }

    public void InsertNewProjectButtonsControl(String project_name , String project_type , String project_description , String btn_command_1_name , String btn_command_2_name , String btn_command_3_name , String btn_command_4_name , String btn_command_5_name , String btn_command_6_name , String btn_up_key_name, String btn_down_key_name, String btn_left_key_name, String btn_right_key_name, String btn_center_key_name, String btn_up_key_down , String btn_up_key_up , String btn_down_key_down
            , String btn_down_key_up , String btn_left_key_down , String btn_left_key_up , String btn_right_key_down , String btn_right_key_up , String btn_center_key_down , String btn_center_key_up , String btn_com1_key_down , String btn_com1_key_up , String btn_com2_key_down , String btn_com2_key_up , String btn_com3_key_down , String btn_com3_key_up , String btn_com4_key_down , String btn_com4_key_up , String btn_com5_key_down , String btn_com5_key_up , String btn_com6_key_down , String btn_com6_key_up) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("project_name",project_name);
        contentValues.put("project_type",project_type);
        contentValues.put("project_description",project_description);
        contentValues.put("btn_command_1_name",btn_command_1_name);
        contentValues.put("btn_command_2_name",btn_command_2_name);
        contentValues.put("btn_command_3_name",btn_command_3_name);
        contentValues.put("btn_command_4_name",btn_command_4_name);
        contentValues.put("btn_command_5_name",btn_command_5_name);
        contentValues.put("btn_command_6_name",btn_command_6_name);
        contentValues.put("btn_up_key_name",btn_up_key_name);
        contentValues.put("btn_down_key_name",btn_down_key_name);
        contentValues.put("btn_left_key_name",btn_left_key_name);
        contentValues.put("btn_right_key_name",btn_right_key_name);
        contentValues.put("btn_center_key_name",btn_center_key_name);
        contentValues.put("btn_up_key_down",btn_up_key_down);
        contentValues.put("btn_up_key_up",btn_up_key_up);
        contentValues.put("btn_down_key_down",btn_down_key_down);
        contentValues.put("btn_down_key_up",btn_down_key_up);
        contentValues.put("btn_left_key_down",btn_left_key_down);
        contentValues.put("btn_left_key_up",btn_left_key_up);
        contentValues.put("btn_right_key_down",btn_right_key_down);
        contentValues.put("btn_right_key_up",btn_right_key_up);
        contentValues.put("btn_center_key_down",btn_center_key_down);
        contentValues.put("btn_center_key_up",btn_center_key_up);
        contentValues.put("btn_com1_key_down",btn_com1_key_down);
        contentValues.put("btn_com1_key_up",btn_com1_key_up);
        contentValues.put("btn_com2_key_down",btn_com2_key_down);
        contentValues.put("btn_com2_key_up",btn_com2_key_up);
        contentValues.put("btn_com3_key_down",btn_com3_key_down);
        contentValues.put("btn_com3_key_up",btn_com3_key_up);
        contentValues.put("btn_com4_key_down",btn_com4_key_down);
        contentValues.put("btn_com4_key_up",btn_com4_key_up);
        contentValues.put("btn_com5_key_down",btn_com5_key_down);
        contentValues.put("btn_com5_key_up",btn_com5_key_up);
        contentValues.put("btn_com6_key_down",btn_com6_key_down);
        contentValues.put("btn_com6_key_up",btn_com6_key_up);
        database.insert("Project_Buttons_Control", null, contentValues);
        database.close();
    }


    public void InsertNewProjectRotationControl(String project_name , String project_type , String project_description , String min_20 , String min_40 , String min_60 , String min_80 , String min_100 , String min_120 , String min_140, String min_160, String min_180, String plus_20, String plus_40, String plus_60 , String plus_80 , String plus_100
            , String plus_120 , String plus_140 , String plus_160 , String plus_180  ) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("project_name",project_name);
        contentValues.put("project_type",project_type);
        contentValues.put("project_description",project_description);
        contentValues.put("min_20",min_20);
        contentValues.put("min_40",min_40);
        contentValues.put("min_60",min_60);
        contentValues.put("min_80",min_80);
        contentValues.put("min_100",min_100);
        contentValues.put("min_120",min_120);
        contentValues.put("min_140",min_140);
        contentValues.put("min_160",min_160);
        contentValues.put("min_180",min_180);
        contentValues.put("plus_20",plus_20);
        contentValues.put("plus_40",plus_40);
        contentValues.put("plus_60",plus_60);
        contentValues.put("plus_80",plus_80);
        contentValues.put("plus_100",plus_100);
        contentValues.put("plus_120",plus_120);
        contentValues.put("plus_140",plus_140);
        contentValues.put("plus_160",plus_160);
        contentValues.put("plus_180",plus_180);
        database.insert("Project_Rotation_Control", null, contentValues);
        database.close();
    }

    public void InsertNewProjectVoiceControl(String project_name , String project_type , String project_description , String voice_1 , String com_1 , String voice_2 , String com_2 , String voice_3 , String com_3 , String voice_4, String com_4, String voice_5, String com_5, String voice_6, String com_6 , String voice_7 , String com_7
            , String voice_8 , String com_8 , String voice_9 , String com_9  ) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("project_name",project_name);
        contentValues.put("project_type",project_type);
        contentValues.put("project_description",project_description);
        contentValues.put("voice_1",voice_1);
        contentValues.put("com_1",com_1);
        contentValues.put("voice_2",voice_2);
        contentValues.put("com_2",com_2);
        contentValues.put("voice_3",voice_3);
        contentValues.put("com_3",com_3);
        contentValues.put("voice_4",voice_4);
        contentValues.put("com_4",com_4);
        contentValues.put("voice_5",voice_5);
        contentValues.put("com_5",com_5);
        contentValues.put("voice_6",voice_6);
        contentValues.put("com_6",com_6);
        contentValues.put("voice_7",voice_7);
        contentValues.put("com_7",com_7);
        contentValues.put("voice_8",voice_8);
        contentValues.put("com_8",com_8);
        contentValues.put("voice_9",voice_9);
        contentValues.put("com_9",com_9);
        database.insert("Project_Voice_Control", null, contentValues);
        database.close();
    }

    public void InsertNewProjectDetails(String project_name , String project_type , String project_description ) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("project_name",project_name);
        contentValues.put("project_type",project_type);
        contentValues.put("project_description",project_description);
        database.insert("Projects_List", null, contentValues);
        database.close();
    }

    public void UpdateButtonControlRecord(String COLUMN_NAME,String COLUMN_VAL,String PROJECT_NAME) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,COLUMN_VAL);
        database.update("Project_Buttons_Control", contentValues, "project_name" + " = ?", new String[]{PROJECT_NAME});
        database.close();
    }

    public void UpdateRotationControlRecord(String COLUMN_NAME,String COLUMN_VAL,String PROJECT_NAME) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,COLUMN_VAL);
        database.update("Project_Rotation_Control", contentValues, "project_name" + " = ?", new String[]{PROJECT_NAME});
        database.close();
    }

    public void UpdateVoiceControlRecord(String COLUMN_NAME,String COLUMN_VAL,String PROJECT_NAME) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,COLUMN_VAL);
        database.update("Project_Voice_Control", contentValues, "project_name" + " = ?", new String[]{PROJECT_NAME});
        database.close();
    }

    public void updateRecordlogin(String COLUMN_NAME1,String COLUMN_VAL1,String COLUMN_NAME2,String COLUMN_VAL2,String COLUMN_NAME3,String COLUMN_VAL3,String PROJECT_NAME) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME1,COLUMN_VAL1);
        contentValues.put(COLUMN_NAME2,COLUMN_VAL2);
        contentValues.put(COLUMN_NAME3,COLUMN_VAL3);
        database.update("Project_Buttons_Control", contentValues, "project_name" + " = ?", new String[]{PROJECT_NAME});
        database.close();
    }

    public void deleteRecordAlternate(String TABLE_NAME,String COLUMN_ID,String ID_NUM) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_ID + " = '" + ID_NUM + "'");
        database.close();
    }

    public void deleteAllRecord(String TABLE_NAME) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.execSQL("delete from " + TABLE_NAME );
        database.close();
    }

    public Cursor sortRecord_type( ) {
      return    myDataBase.rawQuery("SELECT * FROM qr_data Order By des1 DESC ",null);
    }

    public Cursor sortRecord_name( ) {
        return    myDataBase.rawQuery("SELECT * FROM qr_data Order By val1 ASC ",null);
    }

    public Cursor sortRecord_id( ) {
        return    myDataBase.rawQuery("SELECT * FROM Projects_List Order By _id DESC",null);
    }




}