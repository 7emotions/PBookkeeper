package team.paradise.pbookkeeper.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import team.paradise.pbookkeeper.Bill;
import team.paradise.pbookkeeper.BillItem;

public class BillItemDao {
    /*
    *
    * DAO class(Data Access Object)
    *
    * */
    private static String DB_NAME="bill_db";
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private int LEN = 4;

    public BillItemDao(Context context){
        dbHelper=new DBHelper(context,DB_NAME,null,1);
    }

    public void saveBill(Bill bill){
        db=dbHelper.getWritableDatabase();
        String unit=bill.getUnit();
        String date=bill.getDate();
        ArrayList<BillItem> billItems=bill.getList();
        Gson gson=new Gson();
        String bill_data=gson.toJson(billItems);

        ContentValues values=new ContentValues();
        values.put("unit",unit);
        values.put("date",date);
        values.put("bill_data",bill_data);

        db.insert(dbHelper.TB_NAME,null,values);
    }

    public ArrayList<Bill> queryBill(){
        ArrayList<Bill> bills=new ArrayList<>();
        db=dbHelper.getWritableDatabase();

        Gson gson=new Gson();
        Bill bill=new Bill();

        Cursor cursor=db.query(dbHelper.TB_NAME,new String[]{
                "unit",
                "date",
                "bill_data"
        },null,null,null,null,null);
        while (cursor.moveToNext()){
            bill.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
            bill.setDate(cursor.getString(cursor.getColumnIndex("date")));
            String bill_data=cursor.getString(cursor.getColumnIndex("bill_data"));
            Type type=new TypeToken<ArrayList<BillItem>>(){}.getType();
            ArrayList<BillItem> billItems=gson.fromJson(bill_data,type);
            bill.setList(billItems);
            bills.add(bill);
        }
        return bills;
    }
}
