package team.paradise.pbookkeeper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/*
*
* 2020/8/15 Paradise 实现 Item新建与显示
*
* */
public class EditBillActivity extends Activity {
    private ListView bill_list;
    private List<BillItem> lists;
    private MyAdapter adapter;

    /*
    *
    * TODO:
    * i.我的自定义适配器(res/edit_bill_item)右侧有两个Button，我觉得做左滑显示更好
    * ii.两个Button分别是编辑Item与删除Item，我还没实现
    * iii.数据保存我还没做
    *
    * 以上的features你有时间就实现一下，实现了就在序号的前面做个标记
    *
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);

        lists = new ArrayList<>();
        adapter = new MyAdapter(lists,EditBillActivity.this);
        bill_list=findViewById(R.id.bill_item_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            //悬浮按钮按下
            @Override
            public void onClick(View view) {
                final BillItem item=new BillItem();

                //自定义dialog的View
                LayoutInflater inflater = LayoutInflater.from(EditBillActivity.this);
                final View sampleView = inflater.inflate(R.layout.bill_dialog, null);

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
                                String name = ((TextView)sampleView.findViewById(R.id.dialog_name))
                                        .getText().toString();
                                int number = Integer.parseInt(((TextView)sampleView.
                                        findViewById(R.id.dialog_number)).getText().toString());
                                int price = Integer.parseInt(((TextView)sampleView.
                                        findViewById(R.id.dialog_price)).getText().toString());
                                int total = Integer.parseInt(((TextView)sampleView.
                                        findViewById(R.id.dialog_total)).getText().toString());

                                item.setName(name);
                                item.setNumber(number);
                                item.setPrice(price);
                                item.setTotal(total);
                                lists.add(item);
                                adapter.AddItem(item);
                            }
                        })
                        .setNegativeButton("取消", null).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        bill_list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_bill,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                // TODO:数据保存并返回Home
                Intent i = getIntent();
                //收货单位
                String recvUnit = i.getStringExtra("recvUnit");
                String date = i.getStringExtra("date");
                break;
            }
            default:{

            }
        }
        return true;
    }
}
