package team.paradise.pbookkeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import team.paradise.pbookkeeper.utils.BillItemDao;

public class MyBillAdapter extends BaseAdapter {

    private ArrayList<Bill> bills;
    private LayoutInflater inflater;

    public MyBillAdapter(Context context){
        this.bills= new BillItemDao(context).queryBill();
        this.inflater=LayoutInflater.from(context);
    }

    public ArrayList<Bill> getBills() {
        return bills;
    }

    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }
    @Override
    public int getCount() {
        return (bills==null)?0:bills.size();
    }

    @Override
    public Object getItem(int i) {
        return bills.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void notifyDataSetChanged() {
        this.bills=new BillItemDao(inflater.getContext()).queryBill();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if(view==null){
            view=inflater.inflate(R.layout.recent_list_item,null);
            holder=new ViewHolder();
            holder.date=view.findViewById(R.id.tv_date);
            holder.unit=view.findViewById(R.id.tv_unit);
            view.setTag(holder);
        }else {
            holder=(ViewHolder) view.getTag();
        }

        holder.date.setText(bills.get(i).getDate());
        holder.unit.setText(bills.get(i).getUnit());

        return view;
    }

    private class ViewHolder{
        public TextView unit;
        public TextView date;
    }
}


