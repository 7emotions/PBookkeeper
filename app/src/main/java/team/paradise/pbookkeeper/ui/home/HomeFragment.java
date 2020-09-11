package team.paradise.pbookkeeper.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.Serializable;
import java.util.ArrayList;

import team.paradise.pbookkeeper.AddBillActivity;
import team.paradise.pbookkeeper.Bill;
import team.paradise.pbookkeeper.BillItem;
import team.paradise.pbookkeeper.EditBillActivity;
import team.paradise.pbookkeeper.MainActivity;
import team.paradise.pbookkeeper.MyBillAdapter;
import team.paradise.pbookkeeper.R;
import team.paradise.pbookkeeper.utils.BillItemDao;

public class HomeFragment extends Fragment {

    private SwipeMenuListView listView;
    private SwipeMenuCreator creator;
    private View root;
    private ArrayList<Bill> bills;
    private MyBillAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final FloatingActionMenu fab = root.findViewById(R.id.fab);
        fab.setClosedOnTouchOutside(true);
        FloatingActionButton fab_new = root.findViewById(R.id.fab_new);
        fab_new.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Add Bill
                Intent i = new Intent(getContext(), AddBillActivity.class);
                startActivity(i);

                if (i.getBooleanExtra("isOK",false)){
                    adapter.notifyDataSetChanged();
                }
            }
        });

        listView=root.findViewById(R.id.recent_list);
        adapter = new MyBillAdapter(root.getContext());
        bills=adapter.getBills();
        createSwipeMenu();
        listView.setMenuCreator(creator);
        listView.setAdapter(adapter);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        final int id = position;
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("您正在删除一个账单！");
                        builder.setMessage("您确定要删除该账单吗？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new BillItemDao(getContext()).deleteBill(bills.get(id));
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog dialog=builder.create();
                        dialog.show();
                        break;
                    case 1:
                        Intent intent=new Intent(getContext(), EditBillActivity.class);
                        ArrayList<BillItem> bill_data=bills.get(position).getList();
                        intent.putExtra("bill_data",(Serializable)bill_data);
                        intent.putExtra("recvUnit",bills.get(position).getUnit());
                        intent.putExtra("date",bills.get(position).getDate());
                        startActivity(intent);
                        adapter.notifyDataSetChanged();
                        break;
                }

                return false;
            }
        });
        return root;
    }

    private void createSwipeMenu(){
        creator = new SwipeMenuCreator() {
            private int dp2px(int dp){
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        dp,getResources().getDisplayMetrics());
            }

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem delItem = new SwipeMenuItem(root.getContext());
                delItem.setBackground(new ColorDrawable(Color.rgb(0xc9,0xc9,0xce)));
                delItem.setWidth(dp2px(90));
                delItem.setTitle("删除");
                delItem.setTitleSize(18);
                delItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(delItem);

                SwipeMenuItem edtItem = new SwipeMenuItem(root.getContext());
                edtItem.setBackground(new ColorDrawable(Color.rgb(0xc9,0xc9,0xce)));
                edtItem.setWidth(dp2px(90));
                edtItem.setTitle("编辑");
                edtItem.setTitleSize(18);
                edtItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(edtItem);
            }
        };
    }
}