package com.example.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Attendance";
    public static final String COLUMN_NAME_EID = "EID";
    public static final String COLUMN_NAME_DATE = "DAT";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_STATUS = "STATUS";
    public static final String COLUMN_NAME_CHECKIN = "CHECKIN";
    public static final String COLUMN_NAME_CHECKOUT = "CHECKOUT";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ATTENDANCE" + "(EID String,DAT String,STATUS String,CHECKIN String, CHECKOUT String)");//table for attendance
        db.execSQL("CREATE TABLE IF NOT EXISTS USERS" + "(EID String PRIMARY KEY,NAME String)");//table for employee list
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ATTENDANCE");
        db.execSQL("DROP TABLE IF EXISTS USERS");
        onCreate(db);

    }

    //Insert is needed before a new user is trying to mark attendance
    public boolean insert(String EID, String NAME) {

        SQLiteDatabase dbr = this.getReadableDatabase();

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("EID", EID);
            contentValues.put("NAME", NAME);
            db.insert("USERS", null, contentValues);
            return true;

        }catch (Exception e){
            return false;
        }

    }

    //method for entering in status of attendance
    public boolean mark(String EID, String date, String status, String CheckIn, String CheckOut) {

        SQLiteDatabase dbr = this.getReadableDatabase();
        Cursor res = dbr.rawQuery("select * from ATTENDANCE where EID = '"+EID+"' and DAT ='"+date+"'", null);
        System.out.println("Res count for mark"+res.getCount());
        if(res.getCount() > 0)
        {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("STATUS", status);
            contentValues.put("CHECKIN",CheckIn);
            contentValues.put("CHECKOUT",CheckOut);
            db.update("ATTENDANCE",contentValues,"EID = ? and DAT = ?" ,new String[]{EID,date});
            return false;
        }
        else
            {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("EID", EID);
            contentValues.put("DAT", date);
            contentValues.put("STATUS", status);
            contentValues.put("CHECKIN",CheckIn);
            contentValues.put("CHECKOUT",CheckOut);
            db.insert("ATTENDANCE", null, contentValues);
            return true;
        }

    }

    public boolean StraightCheckoutMark(String EID, String date, String status, String CheckOut)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EID", EID);
        contentValues.put("DAT", date);
        contentValues.put("STATUS", status);
        contentValues.put("CHECKIN","-");
        contentValues.put("CHECKOUT",CheckOut);
        db.insert("ATTENDANCE", null, contentValues);
        return true;
    }





    public boolean CheckOut(String EID ,String date,String CheckOut)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("STATUS","Present");
        contentValues.put("CHECKOUT",CheckOut);
        db.update("ATTENDANCE",contentValues,"EID = ? and DAT = ?" ,new String[]{EID,date});
        return true;
    }

    //Returns arraylist containing status
    public ArrayList<AttendancePojo> getStatus(String EID) {
        ArrayList<AttendancePojo> array_list = new ArrayList<AttendancePojo>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ATTENDANCE where EID = '"+EID+"'", null);

        while (res.moveToNext()) {
            AttendancePojo ap = new AttendancePojo();
            ap.setStatus(res.getString(res.getColumnIndex(COLUMN_NAME_STATUS)));
            ap.setDate(res.getString(res.getColumnIndex(COLUMN_NAME_DATE)));
            ap.setId(res.getString(res.getColumnIndex(COLUMN_NAME_EID)));
            ap.setCheckInTime(res.getString(res.getColumnIndex(COLUMN_NAME_CHECKIN)));
            ap.setCheckOutTime(res.getString(res.getColumnIndex(COLUMN_NAME_CHECKOUT)));
            array_list.add(ap);
        }
        res.close();
        return array_list;
    }

    //returns  employee list with names and id
    public ArrayList<AttendancePojo> getUsers() {
        ArrayList<AttendancePojo> array_list = new ArrayList<AttendancePojo>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from users", null);

        while (res.moveToNext()) {
            AttendancePojo ap = new AttendancePojo();
            ap.setName((res.getString(res.getColumnIndex(COLUMN_NAME_NAME))));
            ap.setId(res.getString(res.getColumnIndex(COLUMN_NAME_EID)));
            array_list.add(ap);
        }
        return array_list;
    }

    public String Fetch_recent_status(String EID,String Date)
    {
        String statuspojo = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ATTENDANCE where EID = '"+EID+"' and DAT = '"+Date+"'",null);
        while (res.moveToNext()){
            statuspojo = res.getString(res.getColumnIndex(COLUMN_NAME_STATUS));
        }
        System.out.println("Size of aaaa"+ res.getCount());
        System.out.println("Result:"+statuspojo);
        return statuspojo;
    }

    public String Fetch_Check_IN(String EID,String Date)
    {
        String checkin = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ATTENDANCE where EID = '"+EID+"' and DAT = '"+Date+"'",null);
        while (res.moveToNext()){
            checkin = res.getString(res.getColumnIndex(COLUMN_NAME_CHECKIN));
        }
        return checkin;
    }

    public String Fetch_Check_out(String EID,String Date)
    {
        String checkout = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ATTENDANCE where EID = '"+EID+"' and DAT = '"+Date+"'",null);
        while (res.moveToNext()){
            checkout = res.getString(res.getColumnIndex(COLUMN_NAME_CHECKOUT));
        }
        System.out.println("Result:"+checkout);
        return checkout;
    }


}
