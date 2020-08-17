package team.paradise.pbookkeeper.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public void SaveBill(ArrayList<BillItem> datas, String unit, String date){
        String TB_NAME = "tb_" + unit + "_" + date.replace("-","_");
        db=dbHelper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + " (_id integer primary key autoincrement," +
                "name varchar(20)," +
                "number integer," +
                "price integer," +
                "total integer," +
                "comment varchar(10))");

        for(BillItem e:datas){
            ContentValues values=new ContentValues();
            values.put("name", e.getName());
            values.put("number",e.getNumber());
            values.put("price",e.getPrice());
            values.put("total",e.getTotal());
            values.put("comment",e.getComment());

            long row_id = db.insert(TB_NAME,null,values);
            if(row_id==-1){
                Log.i("BillItemDao","Save Data Filed.");
            } else {
              Log.i("BillItemDao","Save Data Successfully.");
            }
        }
    }

    public void DeleteBillTable(String unit, String date){
        String TB_NAME = "tb_" + unit + "_" + date.replace("-","_");
        db=dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
    }

    public ArrayList<Bill> queryBill() {
        db=dbHelper.getWritableDatabase();
        return null;
    }
}
