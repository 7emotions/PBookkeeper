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

        if (db.insert(dbHelper.TB_NAME,null,values)==-1){
            Log.i("DAO","Save failed.");
        }else {
            Log.i("DAO","Save successfully.");
        }
    }

    public ArrayList<Bill> queryBill(){
        ArrayList<Bill> bills=new ArrayList<>();
        db=dbHelper.getWritableDatabase();

        Cursor cursor=db.query(dbHelper.TB_NAME,new String[]{
                "unit",
                "date",
                "bill_data"
        },null,null,null,null,null);
        while (cursor.moveToNext()){
            Gson gson=new Gson();
            Bill bill=new Bill();
            bill.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
            bill.setDate(cursor.getString(cursor.getColumnIndex("date")));
            String bill_data=cursor.getString(cursor.getColumnIndex("bill_data"));
            Type type=new TypeToken<ArrayList<BillItem>>(){}.getType();
            ArrayList<BillItem> billItems=gson.fromJson(bill_data,type);
            bill.setList(billItems);
            bills.add(bill);
        }
        cursor.close();
        return bills;
    }

    public void editBill(Bill bill){
        db=dbHelper.getWritableDatabase();
        deleteBill(bill);
        saveBill(bill);
    }

    public void deleteBill(Bill bill){
        db=dbHelper.getWritableDatabase();
        if(
                db.delete(dbHelper.TB_NAME,"unit=? and date=?",new String[]{
                        bill.getUnit(),
                        bill.getDate()
                })>0
        ){
            Log.i("DAO","Delete filed.");
        }else{
            Log.i("DAO","Delete Successfully.");
        }
    }
}
