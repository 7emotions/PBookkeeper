package team.paradise.pbookkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
*
* 2020/8/15 Paradise Realized RecvUnit and Date Picking
*
* */

public class AddBillActivity extends AppCompatActivity {

    private Button next_btn;
    private EditText recv_unit;
    private Button bill_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        recv_unit = findViewById(R.id.recv_unit);
        bill_date = findViewById(R.id.bill_date);

        bill_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pick the date of bill
                TimePickerView pvTime = new TimePickerBuilder(AddBillActivity.this,
                        new OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date, View v) {
                                // If date after today
                                if(date.after(new Date())){
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "请输入正确的日期",
                                            Toast.LENGTH_LONG
                                    ).show();
                                    return;
                                }

                                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                                bill_date.setText(ft.format(date));
                            }
                        }).setCancelText("取消").setSubmitText("确定").build();
                pvTime.show();
            }
        });

        next_btn = findViewById(R.id.create_bill);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_date_hit = (String)getBaseContext().getResources().getText(R.string.bill_date);

                if (recv_unit.getText().toString().equals("") ||
                        bill_date.getText().toString().equals(btn_date_hit)){   //If user didn`t pick date
                    Toast.makeText(
                            getApplicationContext(),
                            "请输入正确的收货单位与账单日期",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                //Edit bill content
                Intent i = new Intent(AddBillActivity.this, EditBillActivity.class);
                i.putExtra("recvUnit",recv_unit.getText().toString());
                i.putExtra("date",bill_date.getText().toString());
                startActivity(i);
            }

        });
    }
}
