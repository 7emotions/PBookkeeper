package team.paradise.pbookkeeper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import team.paradise.pbookkeeper.utils.BillItemDao;

/*
*
* 2020/8/15 Paradise 实现 Item新建与显示
*
* */
public class EditBillActivity extends Activity {
    private SwipeMenuListView bill_list;
    private ArrayList<BillItem> lists;
    private MyItemAdapter adapter;
    private SwipeMenuCreator creator;

    /*
    *
    * TODO:
    * (Realized by 7emotions)i.我的自定义适配器(res/edit_bill_item)右侧有两个Button，我觉得做左滑显示更好
    * (Realized by 7emotions)ii.两个Button分别是编辑Item与删除Item，我还没实现
    * iii.数据保存我还没做
    *
    * 以上的features你有时间就实现一下，实现了就在序号的前面做个标记
    *
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);

        final Intent i = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.edit_bill);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                /*
                 *
                 * TODO:
                 * 除了数据保存我觉得还可以实现以下几点
                 * i.撤销
                 * ii.重做
                 *
                 * */
                switch (item.getItemId()){
                    case R.id.save:{
                        //收货单位
                        String recvUnit = i.getStringExtra("recvUnit");
                        String date = i.getStringExtra("date");

                        BillItemDao dao=new BillItemDao(getApplicationContext());
                        if(i.getBooleanExtra("isEdit",true)){
                            dao.editBill(new Bill(recvUnit,date,lists));
                        }else {
                            dao.saveBill(new Bill(recvUnit,date,lists));
                        }
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });

        if(i.getBooleanExtra("isEdit",true)){
            lists = (ArrayList<BillItem>) i.getSerializableExtra("bill_data");
        }else {
            lists = new ArrayList<>();
        }

        adapter = new MyItemAdapter(lists,EditBillActivity.this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //悬浮按钮按下
            @Override
            public void onClick(View view) {
                newAlertDialog();
            }
        });

        createSwipeMenu();
        bill_list = findViewById(R.id.bill_item_list);
        bill_list.setMenuCreator(creator);
        bill_list.setAdapter(adapter);
        bill_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case  0:{
                        //edit
                        editAlertDialog(position);
                        break;
                    }
                    case 1:{
                        //delete
                        adapter.removeItem(position);
                        break;
                    }
                }
                return false;
            }
        });
        bill_list.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    private void newAlertDialog() {
        //自定义dialog的View
        final BillItem item=new BillItem();
        LayoutInflater inflater = LayoutInflater.from(EditBillActivity.this);
        final View sampleView = inflater.inflate(R.layout.bill_dialog, null);
        final EditText ed_number = sampleView.findViewById(R.id.dialog_number);
        final EditText ed_price = sampleView.findViewById(R.id.dialog_price);
        final EditText ed_name = sampleView.findViewById(R.id.dialog_name);
        final EditText ed_total = sampleView.findViewById(R.id.dialog_total);

        Button calc_btn=sampleView.findViewById(R.id.calc_btn);
        calc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_number=ed_number.getText().toString();
                String s_price=ed_price.getText().toString();
                if(!s_number.equals("") &&
                !s_price.equals("")){
                    //客户要求：超过四百张进一
                    int number=(Integer.parseInt(s_number)+100)/1000;
                    int price=Integer.parseInt(s_price);
                    int total=number*price;
                    ed_total.setText(Integer.toString(total));
                }
            }
        });

        //获取Comment
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.
                createFromResource(EditBillActivity.this,
                        R.array.comment_spinner,
                        android.R.layout.simple_spinner_item);
        Spinner comment_spi = sampleView.findViewById(R.id.comment_spi);
        comment_spi.setAdapter(arrayAdapter);
        comment_spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item.setComment(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //创建并显示AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(EditBillActivity.this)
                .setTitle("添加商品")
                .setView(sampleView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = ed_name.getText().toString();
                        int number = Integer.parseInt(ed_number.getText().toString());
                        int price = Integer.parseInt(ed_price.getText().toString());
                        int total = Integer.parseInt(ed_total.getText().toString());

                        item.setName(name);
                        item.setNumber(number);
                        item.setPrice(price);
                        item.setTotal(total);
                        adapter.AddItem(item);
                        //return;
                    }
                })
                .setNegativeButton("取消", null).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void editAlertDialog(final int id){
        //自定义dialog的View
        final BillItem item=new BillItem();
        LayoutInflater inflater = LayoutInflater.from(EditBillActivity.this);
        final View sampleView = inflater.inflate(R.layout.bill_dialog, null);

        final BillItem improvise_item = lists.get(id);
        final EditText edt_name = sampleView.findViewById(R.id.dialog_name);
        final EditText edt_number = sampleView.findViewById(R.id.dialog_number);
        final EditText edt_price = sampleView.findViewById(R.id.dialog_price);
        final EditText edt_total = sampleView.findViewById(R.id.dialog_total);

        edt_name.setText(improvise_item.getName());
        edt_number.setText(Integer.toString(improvise_item.getNumber()));
        edt_price.setText(Integer.toString(improvise_item.getPrice()));
        edt_total.setText(Integer.toString(improvise_item.getTotal()));

        Button calc_btn=sampleView.findViewById(R.id.calc_btn);
        calc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_number=edt_number.getText().toString();
                String s_price=edt_price.getText().toString();
                if(!s_number.equals("") &&
                        !s_price.equals("")){
                    //客户要求：超过四百张进一
                    int number=(Integer.parseInt(s_number)+100)/1000;
                    int price=Integer.parseInt(s_price);
                    int total=number*price;
                    edt_total.setText(Integer.toString(total));
                }
            }
        });

        //获取Comment
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.
                createFromResource(EditBillActivity.this,
                        R.array.comment_spinner,
                        android.R.layout.simple_spinner_item);
        final Spinner comment_spi = sampleView.findViewById(R.id.comment_spi);
        comment_spi.setAdapter(arrayAdapter);

        for(int i=0;i<comment_spi.getAdapter().getCount();i++) {
            if(improvise_item.getComment().equals(comment_spi.getAdapter().getItem(i).toString())) {
                comment_spi.setSelection(i,true);
            }
        }

        comment_spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item.setComment(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("onNothingSelected");
            }
        });

        //创建并显示AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(EditBillActivity.this)
                .setTitle("修改商品")
                .setView(sampleView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = edt_name.getText().toString();
                        int number = Integer.parseInt((edt_number.getText().toString()));
                        int price = Integer.parseInt((edt_price.getText().toString()));
                        int total = Integer.parseInt((edt_total.getText().toString()));
                        if(item.getComment() == null){
                            //Comment isn`t changed
                            item.setComment(improvise_item.getComment());
                        }

                        item.setName(name);
                        item.setNumber(number);
                        item.setPrice(price);
                        item.setTotal(total);

                        adapter.set(id,item);
                    }
                })
                .setNegativeButton("取消", null).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void createSwipeMenu(){
        creator = new SwipeMenuCreator() {
            private int dp2px(int dp){
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        dp,getResources().getDisplayMetrics());
            }

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.rgb(0xc9,0xc9,0xce)));
                editItem.setWidth(dp2px(90));
                editItem.setTitle("编辑");
                editItem.setTitleSize(18);
                editItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(editItem);

                SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
                delItem.setBackground(new ColorDrawable(Color.rgb(0xc9,0xc9,0xce)));
                delItem.setWidth(dp2px(90));
                delItem.setTitle("删除");
                delItem.setTitleSize(18);
                delItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(delItem);
            }
        };
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4){ //if back key is up
            return returnHome();
        }
        return false;
    }

    private boolean returnHome() {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isOK",true);
        startActivity(intent);
        return true;
    }
}
