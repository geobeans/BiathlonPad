package cn.geobeans.biathlon;

import android.os.Bundle;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.geobeans.biathlon.entity.OptionBean;
import cn.geobeans.biathlon.entity.Shooting;
import cn.geobeans.biathlon.target.TargetView;

public class MainTargetActivity extends AppCompatActivity {
    protected DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private String mAthelete = "ZHU";
    private String mLane = "5-1";
    private String mVenue;
    private boolean mbProne = true;
    private boolean mbZeroing = true;

    private OptionsPickerView pvAtheleteOptions, pvLaneOptions;
    private ArrayList<OptionBean> atheleteItem = new ArrayList<>();
    private ArrayList<OptionBean> laneItem = new ArrayList<>();

    private TextView tv_athelete,tv_lane;

    private ArrayList<PanelView> mPanels = new ArrayList<>();

    protected String getNum(double a) {
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        String p = decimalFormat.format(a);
        return p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_target);
        Toolbar toolbar = findViewById(R.id.toolbar);

        PanelView panel1 = findViewById(R.id.panel_1);
        mPanels.add(panel1);
        PanelView panel2 = findViewById(R.id.panel_2);
        mPanels.add(panel2);
        PanelView panel3 = findViewById(R.id.panel_3);
        mPanels.add(panel3);
        PanelView panel4 = findViewById(R.id.panel_4);
        mPanels.add(panel4);
        PanelView panel5 = findViewById(R.id.panel_5);
        mPanels.add(panel5);
        PanelView panel6 = findViewById(R.id.panel_6);
        mPanels.add(panel6);
        PanelView panel7 = findViewById(R.id.panel_7);
        mPanels.add(panel7);
        PanelView panel8 = findViewById(R.id.panel_8);
        mPanels.add(panel8);

        final TargetView tv = findViewById(R.id.target_1);
        tv.setOnViewClick(new TargetView.onViewClick() {
            @Override
            public void onClick(int index) {
                float x11 = (float) (2.0f + Math.random()*7.0f);//(float) (Math.random()*12.0f);
                float x12 = 60.0f + (float)(Math.random()*90.0f); //(float) (Math.random()*360.0f);
                String fs = getNum(x11);
                mPanels.get(index).setWindSpeedString(fs);
                mPanels.get(index).setAngle(x12);
                mPanels.get(index).invalidate();
            }
        });

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

                shooting.setSpeed_1(Float.parseFloat(mPanels.get(0).getWindSpeedString()));
                shooting.setDirect_1((int)(mPanels.get(0).getAngle()));
                shooting.setSpeed_2(Float.parseFloat(mPanels.get(1).getWindSpeedString()));
                shooting.setDirect_2((int)(mPanels.get(1).getAngle()));
                shooting.setSpeed_3(Float.parseFloat(mPanels.get(2).getWindSpeedString()));
                shooting.setDirect_3((int)(mPanels.get(2).getAngle()));
                shooting.setSpeed_4(Float.parseFloat(mPanels.get(3).getWindSpeedString()));
                shooting.setDirect_4((int)(mPanels.get(3).getAngle()));
                shooting.setSpeed_5(Float.parseFloat(mPanels.get(4).getWindSpeedString()));
                shooting.setDirect_5((int)(mPanels.get(4).getAngle()));

                RadioButton rb = findViewById(R.id.prone);
                shooting.setProne(rb.isChecked());
                RadioButton rbZero = findViewById(R.id.zeoring_yes);
                shooting.setModel(rbZero.isChecked()?"ZEROING":"COMP");

                shooting.save();
                tv.reset();
            }
        });

        tv_athelete = findViewById(R.id.atheletes);
        tv_lane = findViewById(R.id.lanes);

        laneItem.add(new OptionBean("5-1"));
        laneItem.add(new OptionBean("10-6"));
        laneItem.add(new OptionBean("15-11"));
        laneItem.add(new OptionBean("20-16"));
        laneItem.add(new OptionBean("25-21"));
        laneItem.add(new OptionBean("30-26"));

        atheleteItem.add(new OptionBean("BLA"));
        atheleteItem.add(new OptionBean("CAN"));
        atheleteItem.add(new OptionBean("LI"));
        atheleteItem.add(new OptionBean("STA"));
        atheleteItem.add(new OptionBean("FUN"));
        atheleteItem.add(new OptionBean("DIN"));
        atheleteItem.add(new OptionBean("ZHU"));
        atheleteItem.add(new OptionBean("CHE"));

        pvAtheleteOptions = new OptionsPickerBuilder(MainTargetActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = atheleteItem.get(options1).getPickerViewText();
                tv_athelete.setText(tx);
                mAthelete = tx;
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        //final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvAtheleteOptions.returnData();
                                pvAtheleteOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvAtheleteOptions.dismiss();
                            }
                        });
                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();

        pvAtheleteOptions.setPicker(atheleteItem);

        pvLaneOptions = new OptionsPickerBuilder(MainTargetActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = laneItem.get(options1).getPickerViewText();
                tv_lane.setText(tx);
                mLane = tx;
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        //final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvLaneOptions.returnData();
                                pvLaneOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvLaneOptions.dismiss();
                            }
                        });
                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();

        pvLaneOptions.setPicker(laneItem);

        ImageView ivSelectAthelete = findViewById(R.id.iv_select_athelete);
        ivSelectAthelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvAtheleteOptions.show();
            }
        });

        ImageView ivSelectLane = findViewById(R.id.iv_select_lane);
        ivSelectLane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvLaneOptions.show();
            }
        });


//        final String[] array_spinner_size=new String[5];
//        array_spinner_size[0]="Tom";
//        array_spinner_size[1]="Jack";
//        array_spinner_size[2]="John";
//        array_spinner_size[3]="Mary";
//        array_spinner_size[4]="Lisa";
//
//        Spinner sz =  findViewById(R.id.athelete);
//
//        ArrayAdapter adapter = new ArrayAdapter(this,
//                android.R.layout.simple_spinner_item, array_spinner_size);
//        sz.setAdapter(adapter);
//        sz.setSelection(0,true);
//        sz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                TextView tv = (TextView)view;
//                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL); //设置居中
//                mAthelete = array_spinner_size[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//        });
//
//        final String[] array_lane=new String[6];
//        array_lane[0]="1-5";
//        array_lane[1]="6-10";
//        array_lane[2]="11-15";
//        array_lane[3]="16-20";
//        array_lane[4]="21-25";
//        array_lane[5]="26-30";
//
//        Spinner sp_lanes =  findViewById(R.id.lanes);
//
//        ArrayAdapter adapter_lane = new ArrayAdapter(this,
//                android.R.layout.simple_spinner_item, array_lane);
//        sp_lanes.setAdapter(adapter_lane);
//        sp_lanes.setSelection(0,true);
//        sp_lanes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                TextView tv = (TextView)view;
//                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL); //设置居中
//                mLane = array_lane[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//        });
    }
}