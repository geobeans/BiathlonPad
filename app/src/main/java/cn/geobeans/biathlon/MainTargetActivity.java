package cn.geobeans.biathlon;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import cn.geobeans.biathlon.entity.Shooting;

public class MainTargetActivity extends AppCompatActivity {
    private Shooting mShooting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShooting = new Shooting();

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
                tv.reset();
                mShooting.setX1(tv.getHitX(0));
                mShooting.setY1(tv.getHitY(0));
                mShooting.setX2(tv.getHitX(1));
                mShooting.setY2(tv.getHitY(1));
                mShooting.setX3(tv.getHitX(2));
                mShooting.setY3(tv.getHitY(2));
                mShooting.setX4(tv.getHitX(3));
                mShooting.setY4(tv.getHitY(3));
                mShooting.setX5(tv.getHitX(4));
                mShooting.setY5(tv.getHitY(4));
                mShooting.save();
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
        //s1.setPromptId(index);
        sz.setSelection(0,true);
        sz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TextView tv = (TextView)view;
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL); //设置居中
                mShooting.setName(array_spinner_size[position]);
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
        //s1.setPromptId(index);
        sp_lanes.setSelection(0,true);
        sp_lanes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TextView tv = (TextView)view;
                //tv.setTextSize(22.0f); //设置大小
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL); //设置居中
                mShooting.setLane(array_lane[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}