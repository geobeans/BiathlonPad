package cn.geobeans.biathlon;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import cn.geobeans.biathlon.entity.Shooting;
import cn.geobeans.biathlon.target.TargetView;

public class MainTargetActivity extends AppCompatActivity {

    private String mAthelete = "ZHU";
    private String mLane = "5-1";
    private String mVenue;
    private boolean mbProne = true;
    private boolean mbZeroing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_target);
        Toolbar toolbar = findViewById(R.id.toolbar);

        final TargetView tv = findViewById(R.id.target_1);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Shooting shooting = new Shooting();
                shooting.setName(mAthelete);
                shooting.setLane(mLane);
                shooting.setX1(tv.getHitX(0));
                shooting.setY1(tv.getHitY(0));
                shooting.setX2(tv.getHitX(1));
                shooting.setY2(tv.getHitY(1));
                shooting.setX3(tv.getHitX(2));
                shooting.setY3(tv.getHitY(2));
                shooting.setX4(tv.getHitX(3));
                shooting.setY4(tv.getHitY(3));
                shooting.setX5(tv.getHitX(4));
                shooting.setY5(tv.getHitY(4));

                shooting.setTime1(tv.getHitDate(0));
                shooting.setTime2(tv.getHitDate(1));
                shooting.setTime3(tv.getHitDate(2));
                shooting.setTime4(tv.getHitDate(3));
                shooting.setTime5(tv.getHitDate(4));

                RadioButton rb = findViewById(R.id.prone);
                shooting.setProne(rb.isChecked());
                RadioButton rbZero = findViewById(R.id.zeoring_yes);
                shooting.setModel(rbZero.isChecked()?"ZEROING":"COMP");

                shooting.save();
                tv.reset();
            }
        });

        final String[] array_spinner_size=new String[5];
        array_spinner_size[0]="Tom";
        array_spinner_size[1]="Jack";
        array_spinner_size[2]="John";
        array_spinner_size[3]="Mary";
        array_spinner_size[4]="Lisa";

        Spinner sz =  findViewById(R.id.athelete);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner_size);
        sz.setAdapter(adapter);
        sz.setSelection(0,true);
        sz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TextView tv = (TextView)view;
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL); //设置居中
                mAthelete = array_spinner_size[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        final String[] array_lane=new String[6];
        array_lane[0]="1-5";
        array_lane[1]="6-10";
        array_lane[2]="11-15";
        array_lane[3]="16-20";
        array_lane[4]="21-25";
        array_lane[5]="26-30";

        Spinner sp_lanes =  findViewById(R.id.lanes);

        ArrayAdapter adapter_lane = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_lane);
        sp_lanes.setAdapter(adapter_lane);
        sp_lanes.setSelection(0,true);
        sp_lanes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TextView tv = (TextView)view;
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL); //设置居中
                mLane = array_lane[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}