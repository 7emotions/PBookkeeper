package team.paradise.pbookkeeper;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Bill{
    private String unit;
    private String date;
    private ArrayList<BillItem> list;

    public Bill()
    {

    }

    public Bill(String unit, String date, ArrayList<BillItem> list) {
        this.unit = unit;
        this.date = date;
        this.list = list;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n" +
                "unit:"+this.unit+"\n" +
                "date:"+this.date+"\n";
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<BillItem> getList() {
        return list;
    }

    public void setList(ArrayList<BillItem> list) {
        this.list = list;
    }
}
