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

public class MainTargetActivity extends AppCompatActivity {

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
                tv.reset();
            }
        });

        String[] array_spinner_size=new String[5];
        array_spinner_size[0]="Tom";
        array_spinner_size[1]="Jack";
        array_spinner_size[2]="John";
        array_spinner_size[3]="Mary";
        array_spinner_size[4]="Lisa";
//        array_spinner_size[5]="6";
//        array_spinner_size[6]="7";
//        array_spinner_size[7]="8";
//        array_spinner_size[8]="9";
//        array_spinner_size[9]="10";
//        array_spinner_size[10]="11";
//        array_spinner_size[11]="12";
//        array_spinner_size[12]="13";
//        array_spinner_size[13]="14";
//        array_spinner_size[14]="15";

        Spinner sz =  findViewById(R.id.selskier);

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
                tv.setTextSize(22.0f); //设置大小
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL); //设置居中
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}