package team.paradise.pbookkeeper;

import android.app.Activity;
import android.content.DialogInterface;
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

public class EditBillActivity extends Activity {
    private ListView bill_list;
    private List<BillItem> lists;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);

        lists = new ArrayList<>();
        adapter = new MyAdapter(lists,EditBillActivity.this);
        bill_list=findViewById(R.id.bill_item_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BillItem item=new BillItem();
                LayoutInflater inflater = LayoutInflater.from(EditBillActivity.this);
                final View sampleView = inflater.inflate(R.layout.bill_dialog, null);

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
        switch (item.getItemId()){
            case R.id.save:{

                break;
            }
            default:{

            }
        }
        return true;
    }
}
