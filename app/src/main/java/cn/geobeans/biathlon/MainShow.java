package cn.geobeans.biathlon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;

import android.content.res.AssetManager;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import cn.geobeans.biathlon.bluetooth.MainActivity;
import cn.geobeans.biathlon.utils.CrashHandler;
import cn.geobeans.biathlon.utils.ModuleUtil;
import cn.geobeans.biathlon.utils.ToastUtils;
import cn.geobeans.biathlon_landscape.WindrenderActivity;

import static cn.geobeans.biathlon.App.getContext;
import static cn.geobeans.biathlon.utils.FileUtil.addTxtToFileBuffered;
import cn.geobeans.biathlon.PlaymusicService;

public class MainShow extends BaseActivity {
    private TextToSpeech textToSpeech = null;//创建自带语音对象

    private List<PanelView> mPanels = new ArrayList<>();

    private TextView mAver_Qiwen;
    private TextView mAver_Qiya;
    private TextView mConnect_State;
    private TextView mCurrent_time;

    private Button mBt_Replay;
    private Button mBt_RealTime;
    private Button mBt_RenderWind;
    private Button mBt_Setting;

    private boolean mbRecieving = false;


    private float m_angleTst = 0.0f;
    private float m_angleOffset = 0.0f;

    private boolean m_bPlay = false;
    private int m_iPlayIndex = 0;

    private int mVoiceIndex = -1;

    private TextView tv_dl;
    private ImageView iv_dl;
    private int cd_image = 0;
    private boolean dy_80 = false;
    private boolean dy_60 = false;
    private boolean dy_40 = false;
    private boolean dy_20 = false;
    private boolean dy_00 = false;

    MediaPlayer mPlayer = null;

    private Queue<WindData> m_windData = new LinkedList<>();
    private List<WindData> m_windDataPlay = new ArrayList<>();

    public enum PlayState{
        STOP,PLAY,PAUSE
    }

    private PlayState m_playState = PlayState.STOP;
    private void addWindData(WindData data)
    {
        if(m_windData.size()<15){
            m_windData.add(data);
        }else{
            m_windData.poll();
            m_windData.add(data);
        }
    }

    //测试用--开始
    //Timer timer = new Timer();
    Timer timer1 = new Timer();
    //测试用--结束

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_show);
        mActivityList.add(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //去掉顶部标题   横屏专用
        getSupportActionBar().hide();
        //去掉最上面时间、电量等
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        for(int i=0;i<mPanels.size();i++){
            final int finalI = i;
            mPanels.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI!=mVoiceIndex){
                        mVoiceIndex = finalI;
                    }else{
                        mVoiceIndex = -1;
                    }
//                    String strSpeed = mPanels.get(finalI).getWindSpeedString();
//                    startAuto(strSpeed);
                }
            });
        }

        tv_dl = findViewById(R.id.tv_dl);
        iv_dl = findViewById(R.id.iv_dl);

        mAver_Qiwen = findViewById(R.id.average_temp);
        mAver_Qiya = findViewById(R.id.average_pressure);
        mConnect_State = findViewById(R.id.conect_state);
        mCurrent_time = findViewById(R.id.current_time);

        mBt_RenderWind = findViewById(R.id.RenderWind);
        mBt_RenderWind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainShow.this, WindrenderActivity.class);
               startActivity(intent);
            }
        });

        mBt_Setting = findViewById(R.id.SettingButton);
        mBt_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainShow.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        mBt_RealTime = findViewById(R.id.RealTime);
        mBt_RealTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //启动服务，播放音乐
//                Intent intent=new Intent(MainShow.this,PlaymusicService.class);
//                intent.putExtra("type",1);
//                startService(intent);

                mPlayer.start();

//                m_playState = PlayState.STOP;
//                mBt_Replay.setText("Replay");
//                mConnect_State.setText("Receive data normally。");
//                m_iPlayIndex = 0;
            }
        });
        mBt_Replay = findViewById(R.id.Replay);
        mBt_Replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_playState == PlayState.STOP){
                    m_windDataPlay.clear();
                    for(WindData data:m_windData){
                        m_windDataPlay.add(data);
                    }
                    m_playState = PlayState.PLAY;
                    mBt_Replay.setText("Pause");
                    mConnect_State.setText("Replaying data。");
                }else if(m_playState == PlayState.PLAY){
                    m_playState = PlayState.PAUSE;
                    mBt_Replay.setText("Cont.");
                }else if(m_playState == PlayState.PAUSE){
                    m_playState = PlayState.PLAY;
                    mBt_Replay.setText("Pause");
                }
            }
        });
        //mAnglePreference = (EditTextPreference) findPreference("speed_preference");

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
//                        try {
//                            parseFS_Test();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        }, 1000,1000);

        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPlayer.start();
                    }
                });
            }
        }, 1000,1000*30);
        //测试用--结束
        initTTS();

        mPlayer = new MediaPlayer();
        AssetManager assetManager;
        assetManager = getResources().getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd("silence.wav");
            mPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTTS() {
        //实例化自带语音对象
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    // Toast.makeText(MainActivity.this,"成功输出语音",
                    // Toast.LENGTH_SHORT).show();
                    // Locale loc1=new Locale("us");
                    // Locale loc2=new Locale("china");

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(2.5f);//用来控制语速

                    //判断是否支持下面两种语言
                    textToSpeech.setLanguage(Locale.US);
//                    int result2 = textToSpeech.setLanguage(Locale.
//                            SIMPLIFIED_CHINESE);
//                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
//                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);
//
//                    Log.i("zhh_tts", "US支持否？--》" + a +
//                            "\nzh-CN支持否》--》" + b);
                    //Toast.makeText(MainShow.this, "不支持英文", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainShow.this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String num2En(int n){
        switch (n){
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            case 10:
                return "ten";
            case 11:
                return "eleven";
            case 12:
                return "twelve";
            case 13:
                return "thirteen";
            case 14:
                return "fourteen";
            case 15:
                return "fifteen";
            case 16:
                return "sixteen";
            default:
                return "seventeen";
        }
    }

    private void startAuto(String data) {
        //textToSpeech.setLanguage(Locale.ENGLISH);
        //StringBuffer buf = new StringBuffer();
        String s = "";
        String[] d = data.split("\\.");
        if(d.length==2){
//            buf.append(d[0]);
//            buf.append(" ");
//            buf.append("point");
//            buf.append(" ");
//            buf.append(d[1]);

            int a = Integer.parseInt(d[1]);
            int b = Integer.parseInt(d[0]);
            if(a>=5){
                b++;
            }
            s = num2En(b);
        }
         // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        //textToSpeech.setPitch(1.0f);
        // 设置语速
        //textToSpeech.setSpeechRate(1.5f);
//        textToSpeech.speak(buf.toString(),//输入中文，若不支持的设备则不会读出来
//                TextToSpeech.QUEUE_FLUSH, null);
        textToSpeech.speak(s,//输入中文，若不支持的设备则不会读出来
                TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        textToSpeech.shutdown(); // 关闭，释放资源
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.app_setup:
//                mPanels.get(0).setAngle(m_angleTst);
//                mPanels.get(0).invalidate();
//                m_angleTst += 10.0f;

                Intent intent = new Intent(MainShow.this, SettingsActivity.class);
                //Intent intent = new Intent(MainActivity.this, TestActiviy.class);
                startActivity(intent);
                break;
            case R.id.app_admin:
//                mPanels.get(0).setAngle(m_angleTst);
//                mPanels.get(0).invalidate();
//                m_angleTst += 10.0f;

                Intent intentAdmin = new Intent(MainShow.this, SettingAdminActivity.class);
                //Intent intent = new Intent(MainActivity.this, TestActiviy.class);
                startActivity(intentAdmin);
                break;
            case R.id.app_exit:
                ModuleUtil.getInstance().disconnect();
                finish();
                break;
            default:
        }
        return true;
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

//    private boolean fillWind(String[] data,StringBuffer stringBuffer,int data_index,int panel_index,WindData windData)
//    {
//        //风速
//        float x11 = Float.intBitsToFloat(new BigInteger(data[data_index+3] + data[data_index+2] + data[data_index+1] + data[data_index], 16).intValue());
//        //风向
//        float x12 = Float.intBitsToFloat(new BigInteger(data[data_index+7] + data[data_index+6] + data[data_index+5] + data[data_index+4], 16).intValue());
//        //温度
//        float x13 = Float.intBitsToFloat(new BigInteger(data[data_index+11] + data[data_index+10] + data[data_index+9] + data[data_index+8], 16).intValue());
//        //气压
//        float x14 = Float.intBitsToFloat(new BigInteger(data[data_index+15] + data[data_index+14] + data[data_index+13] + data[data_index+12], 16).intValue());
//        //湿度
//        float x15 = Float.intBitsToFloat(new BigInteger(data[data_index+19] + data[data_index+18] + data[data_index+17] + data[data_index+16], 16).intValue());
//
//        String fs = getNum(x11);
//        x12 = x12-m_angleOffset+180.0f;
//        if(x12<0) x12+=360.0f;
//        x12 = x12%360;
//        String fx = getNum(x12);
//        stringBuffer.append( fs + ";" + fx + ";");
//
//        if(m_playState==PlayState.STOP) {
//            mPanels.get(panel_index).setWindSpeedString(fs);
//            mPanels.get(panel_index).setAngle(x12);
//        }else{
//            String speed = m_windDataPlay.get(m_iPlayIndex).m_strFs[panel_index];
//            mPanels.get(panel_index).setWindSpeedString(speed);
//            float direction = m_windDataPlay.get(m_iPlayIndex).m_dFx[panel_index];
//            mPanels.get(panel_index).setAngle(direction);
//        }
//
//        mPanels.get(panel_index).invalidate();
//
//        windData.m_strFs[panel_index] = fs;
//        windData.m_dFx[panel_index] = x12;
//
//        if(Math.abs(x13 - 0.0f) < FLT_EPSILON && Math.abs(x14 - 0.0f) < FLT_EPSILON) {
//            return  false;
//        }else {
//            m_fQiwen += x13;
//            m_fQiya += x14;
//            return true;
//        }
//    }

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

        if(panel_index<=5) {
            if (m_playState == PlayState.STOP) {
                mPanels.get(panel_index).setWindSpeedString(fs);
                mPanels.get(panel_index).setAngle(x12);
            } else {
                String speed = m_windDataPlay.get(m_iPlayIndex).m_strFs[panel_index];
                mPanels.get(panel_index).setWindSpeedString(speed);
                float direction = m_windDataPlay.get(m_iPlayIndex).m_dFx[panel_index];
                mPanels.get(panel_index).setAngle(direction);
            }

            mPanels.get(panel_index).invalidate();
        }
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

    @Override
    protected void onResume() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String strAngle =  preferences.getString ("signature","0");

        m_angleOffset = Float.parseFloat(strAngle);
        super.onResume();
    }

//    private void parseFS_FX(String[] data) {
//        try {
//            if(!mbRecieving){
//                mbRecieving = true;
//                mConnect_State.setText("Receive data normally。");
//                mConnect_State.setTextColor(0xFF173E8E);
//            }
//
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append(dateformat1.format(System.currentTimeMillis()) + " ");//日期
//            stringBuffer.append(dateformat.format(System.currentTimeMillis()));//时间
//            String strCurTime = stringBuffer.toString();
//            if(m_playState==PlayState.STOP) {
//                mCurrent_time.setText(stringBuffer);
//            }else{
//                String curTime = m_windDataPlay.get(m_iPlayIndex).m_curTime;
//                mCurrent_time.setText(curTime);
//            }
//            stringBuffer.append(";");
//
//            long targetId = Long.parseLong(data[0], 16);
//            int sourceId = Integer.parseInt(data[1], 16);
//            long sequence = Long.parseLong(data[2], 16);
//            //Log.e("dongaohui: ", data[0]+data[1]+data[2]);
//
//            long length = Long.parseLong(data[3], 16);
//            long batteryLevel = Long.parseLong(data[4], 16);
//            //过滤重复数据
//            if (tempSeq == sequence) {
//                return;
//            }
//            tempSeq = sequence;
//
//            long cmd = Long.parseLong(data[5], 16);
//            long totalSensorNum = Long.parseLong(data[6], 16);
//            if (totalSensorNum == 0||totalSensorNum==1||totalSensorNum==2)
//                return;
//            if (totalSensorNum == 5) {
//                m_fQiwen = 0.0f;
//                m_fQiya = 0.0f;
//                WindData windData = new WindData();
//                int iNum = 0;
//                if(fillWind(data,stringBuffer,7,0,windData))
//                    iNum++;
//                if(fillWind(data,stringBuffer,27,1,windData))
//                    iNum++;
//                if(fillWind(data,stringBuffer,47,2,windData))
//                    iNum++;
//                if(fillWind(data,stringBuffer,67,3,windData))
//                    iNum++;
//                if(fillWind(data,stringBuffer,87,4,windData))
//                    iNum++;
//
//                if(iNum!=0) {
//                    m_fQiwen /= iNum;
//                    m_fQiya /= iNum;
//                }else{
//                    m_fQiwen = 0.0f;
//                    m_fQiya = 0.0f;
//                }
//                String strQiwen = getNum(m_fQiwen);
//                String strQiya = getNum(m_fQiya);
//                stringBuffer.append( strQiwen + ";" + strQiya + ";");
//
//                if(m_playState==PlayState.STOP) {
//                    mAver_Qiwen.setText("Temp: " + strQiwen + "°C");
//                    mAver_Qiya.setText("BP: " + strQiya + "Pa");
//                }else{
//                    String qw = m_windDataPlay.get(m_iPlayIndex).m_strQiwen;
//                    String qy = m_windDataPlay.get(m_iPlayIndex).m_strQiya;
//                    mAver_Qiwen.setText("Temp: " + qw + "°C");
//                    mAver_Qiya.setText("BP: " + qy + "Pa");
//                    if(m_iPlayIndex<m_windDataPlay.size()-1) {
//                        if(m_playState==PlayState.PLAY)
//                            m_iPlayIndex++;
//                    }else{
//                        mBt_Replay.setText("Replay");
//                        m_playState = PlayState.PAUSE;
//                        m_iPlayIndex = 0;
//                    }
//                }
//
//                windData.m_strQiwen = strQiwen;
//                windData.m_strQiya = strQiya;
//                windData.m_curTime = strCurTime;
//                addWindData(windData);
//            }
//            addTxtToFileBuffered(stringBuffer.toString());
//        }catch (Exception e) {
//            e.printStackTrace();
//            ToastUtils.showToast("Parsing data failed");
//        }
//
//    }

    private void showDL(long batteryLevel) {
        //batteryLevel +=100;
        if(batteryLevel>=129 && batteryLevel <=228){
            batteryLevel -= 128;
            if (batteryLevel >= 80) {
                if(cd_image!=R.mipmap.cd_100) {
                    cd_image=R.mipmap.cd_100;
                    iv_dl.setImageResource(R.mipmap.cd_100);
                }
            } else if (batteryLevel >= 60 && batteryLevel <= 80) {
                if(cd_image!=R.mipmap.cd_80) {
                    cd_image = R.mipmap.cd_80;
                    iv_dl.setImageResource(R.mipmap.cd_80);
                }
            } else if (batteryLevel >= 40 && batteryLevel <= 60) {
                if(cd_image!=R.mipmap.cd_60) {
                    cd_image = R.mipmap.cd_60;
                    iv_dl.setImageResource(R.mipmap.cd_60);
                }
            } else if (batteryLevel >= 20 && batteryLevel <= 40) {
                if(cd_image!=R.mipmap.cd_40) {
                    cd_image = R.mipmap.cd_40;
                    iv_dl.setImageResource(R.mipmap.cd_40);
                }
            } else if (batteryLevel >= 0 && batteryLevel <= 20) {
                if(cd_image!=R.mipmap.cd_20) {
                    cd_image = R.mipmap.cd_20;
                    iv_dl.setImageResource(R.mipmap.cd_20);
                }
            }
        }else {
            if (batteryLevel >= 80 && !dy_80) {
                dy_80 = true;
                dy_60 = false;
                dy_40 = false;
                dy_20 = false;
                dy_00 = false;
                iv_dl.setImageResource(R.mipmap.dl_100);
            } else if (batteryLevel >= 60 && batteryLevel <= 80 && !dy_60) {
                dy_60 = true;
                dy_80 = false;
                dy_40 = false;
                dy_20 = false;
                dy_00 = false;
                iv_dl.setImageResource(R.mipmap.dl_80);
            } else if (batteryLevel >= 40 && batteryLevel <= 60 && !dy_40) {
                dy_40 = true;
                dy_80 = false;
                dy_60 = false;
                dy_20 = false;
                dy_00 = false;
                iv_dl.setImageResource(R.mipmap.dl_60);
            } else if (batteryLevel >= 20 && batteryLevel <= 40 && !dy_20) {
                dy_20 = true;
                dy_80 = false;
                dy_60 = false;
                dy_40 = false;
                dy_00 = false;
                iv_dl.setImageResource(R.mipmap.dl_40);
            } else if (batteryLevel >= 0 && batteryLevel <= 20 && !dy_00) {
                dy_00 = true;
                dy_80 = false;
                dy_60 = false;
                dy_40 = false;
                dy_20 = false;
                iv_dl.setImageResource(R.mipmap.dl_20);
            }
        }
        if(batteryLevel>100) batteryLevel = 100;
        tv_dl.setText(batteryLevel + "%");
    }

    private void parseFS_FX(String[] data) {
        try {
            if(!mbRecieving){
                mbRecieving = true;
                mConnect_State.setText("Receive data normally。");
                mConnect_State.setTextColor(0xFF173E8E);
            }

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(dateformat1.format(System.currentTimeMillis()) + " ");//日期
            stringBuffer.append(dateformat.format(System.currentTimeMillis()));//时间
            String strCurTime = stringBuffer.toString();
            if(m_playState==PlayState.STOP) {
                mCurrent_time.setText(stringBuffer);
            }else{
                String curTime = m_windDataPlay.get(m_iPlayIndex).m_curTime;
                mCurrent_time.setText(curTime);
            }
            stringBuffer.append(";");

            long targetId = Long.parseLong(data[0], 16);
            int sourceId = Integer.parseInt(data[1], 16);
            long sequence = Long.parseLong(data[2], 16);
            //Log.e("dongaohui: ", data[0]+data[1]+data[2]);

            long length = Long.parseLong(data[3], 16);
            long batteryLevel = Long.parseLong(data[4], 16);
            showDL(batteryLevel);

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
//                if(fillWind(data,stringBuffer,7,0,windData))
//                    iNum++;
//                if(fillWind(data,stringBuffer,27,1,windData))
//                    iNum++;
//                if(fillWind(data,stringBuffer,47,2,windData))
//                    iNum++;
//                if(fillWind(data,stringBuffer,67,3,windData))
//                    iNum++;
//                if(fillWind(data,stringBuffer,87,4,windData))
//                    iNum++;
//                if(totalSensorNum == 6){
//                    if(fillWind(data,stringBuffer,107,5,windData))
//                        iNum++;
//                }

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

                if(m_playState==PlayState.STOP) {
                    mAver_Qiwen.setText("Temp: " + strQiwen + "°C");
                    mAver_Qiya.setText("BP: " + strQiya + "Pa");
                }else{
                    String qw = m_windDataPlay.get(m_iPlayIndex).m_strQiwen;
                    String qy = m_windDataPlay.get(m_iPlayIndex).m_strQiya;
                    mAver_Qiwen.setText("Temp: " + qw + "°C");
                    mAver_Qiya.setText("BP: " + qy + "Pa");
                    if(m_iPlayIndex<m_windDataPlay.size()-1) {
                        if(m_playState==PlayState.PLAY)
                            m_iPlayIndex++;
                    }else{
                        mBt_Replay.setText("Replay");
                        m_playState = PlayState.PAUSE;
                        m_iPlayIndex = 0;
                    }
                }

                windData.m_strQiwen = strQiwen;
                windData.m_strQiya = strQiya;
                windData.m_curTime = strCurTime;
                addWindData(windData);

//                if(index==3)
//                    ToastUtils.showToast("total="+totalSensorNum+";index="+index);

                if(totalSensorNum==15){
                    if(index==3)
                    {
                        m_stringBuffer.append(strQiwen + ";" + strQiya + ";");
                        //ToastUtils.showToast(m_stringBuffer.toString());
                        addTxtToFileBuffered(m_stringBuffer.toString());
                    }
                }else
                    addTxtToFileBuffered(stringBuffer.toString());
                if(index==1) {
                    if (mVoiceIndex != -1) {
                        String strSpeed = mPanels.get(mVoiceIndex).getWindSpeedString();
                        startAuto(strSpeed);
                    }
                }
            }
//            else if(totalSensorNum == 15){
//                long packetSensorNum = Long.parseLong(data[7], 16);
//                int idx = 10;
//                for(int i=0;i<packetSensorNum;i++){
//                    fillWind(data,stringBuffer,idx, (int) ((index-1)*6+i));
//                    idx += 21;
//                }
//            }

        }catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("Parsing data failed");
        }

    }

    private boolean fillWindTest(StringBuffer stringBuffer,int data_index,int panel_index,WindData windData)
    {
        float x11 = (float) (2.0f + Math.random()*7.0f);//(float) (Math.random()*12.0f);
        float x12 = 60.0f + (float)(Math.random()*90.0f); //(float) (Math.random()*360.0f);
        float x13 = -13.0f;//(float)(Math.random()*40.0f);
        float x14 = 1400.0f;//(float)(Math.random()*1400.0f);

        String fs = getNum(x11);
        String fx = getNum(x12);
        stringBuffer.append( fs + ";" + fx + ";");

        if(m_playState==PlayState.STOP) {
            mPanels.get(panel_index).setWindSpeedString(fs);
            mPanels.get(panel_index).setAngle(x12);
        }else{
            String speed = m_windDataPlay.get(m_iPlayIndex).m_strFs[panel_index];
            mPanels.get(panel_index).setWindSpeedString(speed);
            float direction = m_windDataPlay.get(m_iPlayIndex).m_dFx[panel_index];
            mPanels.get(panel_index).setAngle(direction);
        }

        mPanels.get(panel_index).invalidate();

        windData.m_strFs[panel_index] = fs;
        windData.m_dFx[panel_index] = x12;
        m_fQiwen += x13;
        m_fQiya += x14;
        return true;

    }

    private void parseFS_Test() throws Exception {
        try {
            if(!mbRecieving){
                mbRecieving = true;
                mConnect_State.setText("Recieve data normally。");
                mConnect_State.setTextColor(0xFF173E8E);
            }

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(dateformat1.format(System.currentTimeMillis()) + " ");//日期
            stringBuffer.append(dateformat.format(System.currentTimeMillis()));//时间
            String strCurTime = stringBuffer.toString();
            if(m_playState==PlayState.STOP) {
                mCurrent_time.setText(stringBuffer);
            }else{
                String curTime = m_windDataPlay.get(m_iPlayIndex).m_curTime;
                mCurrent_time.setText(curTime);
            }
            stringBuffer.append(";");

            int dl  =  (int)(Math.random()*228);
            showDL(dl);

            if (true) {
                m_fQiwen = 0.0f;
                m_fQiya = 0.0f;
                WindData windData = new WindData();
                int iNum = 0;
                if(fillWindTest(stringBuffer,7,0,windData))
                    iNum++;
                if(fillWindTest(stringBuffer,27,1,windData))
                    iNum++;
                if(fillWindTest(stringBuffer,47,2,windData))
                    iNum++;
                if(fillWindTest(stringBuffer,67,3,windData))
                    iNum++;
                if(fillWindTest(stringBuffer,87,4,windData))
                    iNum++;
                if(fillWindTest(stringBuffer,87,5,windData))
                    iNum++;

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

                if(m_playState==PlayState.STOP) {
                    mAver_Qiwen.setText("Temp: " + strQiwen + "°C");
                    mAver_Qiya.setText("BP: " + strQiya + "Pa");
                }else{
                    String qw = m_windDataPlay.get(m_iPlayIndex).m_strQiwen;
                    String qy = m_windDataPlay.get(m_iPlayIndex).m_strQiya;
                    mAver_Qiwen.setText("Temp: " + qw + "°C");
                    mAver_Qiya.setText("BP: " + qy + "Pa");
                    if(m_iPlayIndex<m_windDataPlay.size()-1) {
                        if(m_playState==PlayState.PLAY)
                            m_iPlayIndex++;
                    }else{
                        mBt_Replay.setText("Replay");
                        m_playState = PlayState.PAUSE;
                        m_iPlayIndex = 0;
                    }
                }

                windData.m_strQiwen = strQiwen;
                windData.m_strQiya = strQiya;
                windData.m_curTime = strCurTime;
                addWindData(windData);
            }
            //ToastUtils.showToast(stringBuffer.toString());
            //addTxtToFileBuffered(stringBuffer.toString());
            if(mVoiceIndex!=-1) {
                String strSpeed = mPanels.get(mVoiceIndex).getWindSpeedString();
                startAuto(strSpeed);
            }
        }catch (Exception e) {
            //e.printStackTrace();

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            Throwable cause = e.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            ToastUtils.showToast(result);
            //ToastUtils.showToast("Parsing data failed");
        }

    }
}