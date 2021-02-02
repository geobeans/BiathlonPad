package cn.geobeans.biathlon;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.TextView;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.geobeans.biathlon.utils.ToastUtils;

import static cn.geobeans.biathlon.App.getContext;
import static cn.geobeans.biathlon.utils.FileUtil.addTxtToFileBuffered;

public class SecondShow extends BaseActivity  {
    private SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");

    private Long tempSeq = -1L;

    TextView mWind1;
    TextView mWind2;
    TextView mWind3;
    TextView mWind4;
    TextView mWind5;
    private float m_angleOffset = 0.0f;
    private List<PanelView> mPanels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_show);
        mActivityList.add(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mWind1 = findViewById(R.id.simple_wind1);
//        mWind2 = findViewById(R.id.simple_wind2);
//        mWind3 = findViewById(R.id.simple_wind3);
//        mWind4 = findViewById(R.id.simple_wind4);
//        mWind5 = findViewById(R.id.simple_wind5);

//        PanelView panel1 = findViewById(R.id.panel_simple2);
//        mPanels.add(panel1);
//        PanelView panel2 = findViewById(R.id.panel_simple3);
//        mPanels.add(panel2);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void result(final String[] data) {
        if (Long.parseLong(data[5], 16) == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parseFS_FX(data);
                }
            });
        }
    }

    private void fillWind(String[] data,int data_index,TextView tv,int index)
    {
        //风速
        float x11 = Float.intBitsToFloat(new BigInteger(data[data_index+3] + data[data_index+2] + data[data_index+1] + data[data_index], 16).intValue());
        //风向
        float x12 = Float.intBitsToFloat(new BigInteger(data[data_index+7] + data[data_index+6] + data[data_index+5] + data[data_index+4], 16).intValue());
        //温度
        float x13 = Float.intBitsToFloat(new BigInteger(data[data_index+11] + data[data_index+10] + data[data_index+9] + data[data_index+8], 16).intValue());
        //气压
        float x14 = Float.intBitsToFloat(new BigInteger(data[data_index+15] + data[data_index+14] + data[data_index+13] + data[data_index+12], 16).intValue());
        //湿度
        float x15 = Float.intBitsToFloat(new BigInteger(data[data_index+19] + data[data_index+18] + data[data_index+17] + data[data_index+16], 16).intValue());

        String fs = getNum(x11);
        x12 = x12+140.0f;
        x12 = x12%360;
        String fx = getNum(x12);

//        if(index==2 || index==3) {
//            PanelView panel = mPanels.get(index-2);
//            panel.setWindSpeedString(fs);
//            panel.setAngle(x12);
//            panel.invalidate();
//        }
        tv.setText("   " +String.valueOf(index) + "    "+fs + "   " +  fx);
     }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void parseFS_FX(String[] data) {
        try {
            long targetId = Long.parseLong(data[0], 16);
            int sourceId = Integer.parseInt(data[1], 16);
            long sequence = Long.parseLong(data[2], 16);
            //Log.e("dongaohui: ", data[0]+data[1]+data[2]);

            long length = Long.parseLong(data[3], 16);
            long batteryLevel = Long.parseLong(data[4], 16);
            //过滤重复数据
            if (tempSeq == sequence) {
                return;
            }
            tempSeq = sequence;

            long cmd = Long.parseLong(data[5], 16);
            long totalSensorNum = Long.parseLong(data[6], 16);
            if (totalSensorNum == 0||totalSensorNum==1||totalSensorNum==2)
                return;
            if (totalSensorNum == 5) {
                fillWind(data,7,mWind1,1 );

                fillWind(data,27,mWind2,2);

                fillWind(data,47,mWind3,3);

                fillWind(data,67,mWind4,4);

                fillWind(data,87,mWind5,5);
             }
         }catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("数据解析出错");
        }

    }
}