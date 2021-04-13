package com.example.attendance;

public class TestInterface {
    private DBHelper helper;



    private void setHelper(DBHelper dbHelper)
    {
        this.helper = dbHelper;
    }

    public boolean testinsert(String EID, String NAME)
    {
        return helper.insert(EID, NAME);
    }

    public boolean testmark(String EID, String date, String status, String CheckIn, String CheckOut)
    {
        return helper.mark(EID,date,status,CheckIn,CheckOut);
    }

    public String testfetchstatus(String EID, String date)
    {
        return helper.Fetch_recent_status(EID, date);
    }

    public String testfetchcheckin(String Eid, String Date)
    {
        return helper.Fetch_Check_IN(Eid, Date);
    }

    public String testfetchcheckout(String EID, String Date)
    {
        return  helper.Fetch_Check_out(EID, Date);
    }




}
