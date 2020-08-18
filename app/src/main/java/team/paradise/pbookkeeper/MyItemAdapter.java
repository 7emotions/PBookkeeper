package team.paradise.pbookkeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class MyItemAdapter extends BaseAdapter {
    private List<BillItem> Datas;
    private Context mContext;

    public MyItemAdapter(List<BillItem> datas, Context mContext){
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

    public void set(int i,BillItem item){
        Datas.set(i,item);
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
        ViewHolder holder=null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.edit_bill_item,viewGroup,false);
            holder=new ViewHolder();
            //Prevent text from overlapping
            holder.uptv=view.findViewById(R.id.uptext);
            holder.downtv=view.findViewById(R.id.downtext);
            view.setTag(holder);
        }else {
            holder=(ViewHolder)view.getTag();
        }

        BillItem item = Datas.get(i);

        String uptxt = "品名：" + item.getName() + "\t备注：" + item.getComment();
        String downtxt = "单价：" + item.getPrice() + "\t数量：" + item.getNumber() + "\t金额：" + item.getTotal();

        holder.uptv.setText(uptxt);
        holder.downtv.setText(downtxt);

        return view;
    }

    private class ViewHolder{
        public TextView uptv;
        public TextView downtv;
    }
}
