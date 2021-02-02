package cn.geobeans.biathlon_landscape;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;

import cn.geobeans.biathlon.BaseActivity;
import cn.geobeans.biathlon.MainShow;
import cn.geobeans.biathlon.R;
import cn.geobeans.biathlon.WindData;
import cn.geobeans.biathlon.WindRenderView;
import cn.geobeans.biathlon.utils.ToastUtils;

import static cn.geobeans.biathlon.App.getContext;
import static cn.geobeans.biathlon.utils.FileUtil.addTxtToFileBuffered;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WindrenderActivity extends BaseActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private WindRenderView mContentView;
    private float m_angleOffset = 0.0f;
    //测试用--开始
    //Timer timer = new Timer();
    //测试用--结束

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    onBackPressed();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windrender);
        mActivityList.add(this);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.windRender);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String strAngle =  preferences.getString ("signature","0");

        m_angleOffset = Float.parseFloat(strAngle);
        //测试用--开始
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mContentView.InputWind();
//                        mContentView.invalidate();
//                    }
//                });
//            }
//        }, 1000,1000);
        //测试用--结束
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

    private boolean fillWind(String[] data,StringBuffer stringBuffer,int data_index,int panel_index,WindData windData)
    {
        //风速
        float x11 = Float.intBitsToFloat(new BigInteger(data[data_index+4] + data[data_index+3] + data[data_index+2] + data[data_index+1], 16).intValue());
        //风向
        float x12 = Float.intBitsToFloat(new BigInteger(data[data_index+8] + data[data_index+7] + data[data_index+6] + data[data_index+5], 16).intValue());
        //温度
        float x13 = Float.intBitsToFloat(new BigInteger(data[data_index+12] + data[data_index+11] + data[data_index+10] + data[data_index+9], 16).intValue());
        //气压
        float x14 = Float.intBitsToFloat(new BigInteger(data[data_index+16] + data[data_index+15] + data[data_index+14] + data[data_index+13], 16).intValue());
        //湿度
        float x15 = Float.intBitsToFloat(new BigInteger(data[data_index+20] + data[data_index+19] + data[data_index+18] + data[data_index+17], 16).intValue());

        String fs = getNum(x11);
        x12 = x12-m_angleOffset+180.0f;
        if(x12<0) x12+=360.0f;
        x12 = x12%360;
        String fx = getNum(x12);
        stringBuffer.append( fs + ";" + fx + ";");
        m_stringBuffer.append( fs + ";" + fx + ";");

        windData.m_strFs[panel_index] = fs;
        windData.m_dFx[panel_index] = x12;

        if(Math.abs(x13 - 0.0f) < FLT_EPSILON && Math.abs(x14 - 0.0f) < FLT_EPSILON) {
            return  false;
        }else {
            m_fQiwen += x13;
            m_fQiya += x14;
            return true;
        }
    }

    private void parseFS_FX(String[] data) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(dateformat1.format(System.currentTimeMillis()) + " ");//日期
            stringBuffer.append(dateformat.format(System.currentTimeMillis()));//时间
            String strCurTime = stringBuffer.toString();

            stringBuffer.append(";");

            long targetId = Long.parseLong(data[0], 16);
            int sourceId = Integer.parseInt(data[1], 16);
            long sequence = Long.parseLong(data[2], 16);
            //Log.e("dongaohui: ", data[0]+data[1]+data[2]);

            long length = Long.parseLong(data[3], 16);
            long batteryLevel = Long.parseLong(data[4], 16);

            long index = Long.parseLong(data[9], 16);
            //过滤重复数据
            if (tempSeq == sequence && tempIndex == index) {
                return;
            }
            tempSeq = sequence;
            tempIndex = index;

            long cmd = Long.parseLong(data[5], 16);
            long totalSensorNum = Long.parseLong(data[6], 16);

            if(totalSensorNum==15 && index==1){
                m_stringBuffer.setLength(0);
                m_stringBuffer.append(stringBuffer);
            }

            if (totalSensorNum == 5 ||totalSensorNum == 6||totalSensorNum == 15) {
                m_fQiwen = 0.0f;
                m_fQiya = 0.0f;
                WindData windData = new WindData();
                int iNum = 0;

                long packetSensorNum = Long.parseLong(data[7], 16);

                int idx = 10;
                for(int i=0;i<packetSensorNum;i++){
                    fillWind(data,stringBuffer,idx, (int) ((index-1)*6+i),windData);
                    idx += 21;
                    iNum++;
                }

                if(iNum!=0) {
                    m_fQiwen /= iNum;
                    m_fQiya /= iNum;
                }else{
                    m_fQiwen = 0.0f;
                    m_fQiya = 0.0f;
                }
                String strQiwen = getNum(m_fQiwen);
                String strQiya = getNum(m_fQiya);
                stringBuffer.append( strQiwen + ";" + strQiya + ";");

                if(index==1){
                    float fWindSpeeds[] = new float[6];
                    for(int i=0;i<5;i++){
                        fWindSpeeds[i] = Float.parseFloat(windData.m_strFs[i]);
                        mContentView.InputWind(fWindSpeeds);
                        mContentView.invalidate();
                    }
                }

//                if(totalSensorNum==15){
//                    if(index==3) {
//                        m_stringBuffer.append(strQiwen + ";" + strQiya + ";");
//                        addTxtToFileBuffered(m_stringBuffer.toString());
//                    }
//                }else
//                    addTxtToFileBuffered(stringBuffer.toString());
            }
        }catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("Parsing data failed");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}