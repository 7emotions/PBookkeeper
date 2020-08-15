package team.paradise.pbookkeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class MyAdapter extends BaseAdapter {
    private List<BillItem> Datas;
    private Context mContext;

    public MyAdapter(List<BillItem> datas, Context mContext){
        this.Datas = datas;
        this.mContext = mContext;
    }

    public void AddItem(BillItem i){
        Datas.add(i);
        notifyDataSetChanged();
    }

    public void removeItem(int i){
        Datas.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Datas == null?0: Datas.size();
    }

    @Override
    public Object getItem(int i) {
        return Datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.edit_bill_item,viewGroup,false);

        TextView tv_name = view.findViewById(R.id.name);
        TextView tv_comment = view.findViewById(R.id.comment);
        TextView tv_number = view.findViewById(R.id.number);
        TextView tv_price = view.findViewById(R.id.price);
        TextView tv_total = view.findViewById(R.id.total);

        String name = tv_name.getText().toString() + Datas.get(i).getName();
        String comment = tv_comment.getText().toString() + Datas.get(i).getComment();
        String number = tv_number.getText().toString() + Datas.get(i).getNumber();
        String price = tv_price.getText().toString() + Datas.get(i).getPrice();
        String total = tv_total.getText().toString() + Datas.get(i).getTotal();

        tv_name.setText(name);
        tv_comment.setText(comment);
        tv_number.setText(number);
        tv_price.setText(price);
        tv_total.setText(total);

        return view;
    }
}
