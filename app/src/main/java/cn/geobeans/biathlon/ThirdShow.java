package cn.geobeans.biathlon;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.geobeans.biathlon.utils.ToastUtils;
import cn.geobeans.biathlon.utils.Util;

import static cn.geobeans.biathlon.utils.FileUtil.addTxtToFileBuffered;

public class ThirdShow extends BaseActivity  {
    private SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");

    private Long tempSeq = -1L;
    private Long tempIndex = -1L;

    TextView mWind1;
    TextView mWind2;
    TextView mWind3;
    TextView mWind4;
    TextView mWind5;
    TextView mWind6;
    TextView mWind7;
    TextView mWind8;
    TextView mWind9;
    TextView mWind10;
    TextView mWind11;
    TextView mWind12;
    TextView mWind13;
    TextView mWind14;
    TextView mWind15;
    private float m_angleOffset = 0.0f;
    private List<TextView> mPanels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_show);
        mActivityList.add(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWind1 = findViewById(R.id.simple_wind1);
        mWind2 = findViewById(R.id.simple_wind2);
        mWind3 = findViewById(R.id.simple_wind3);
        mWind4 = findViewById(R.id.simple_wind4);
        mWind5 = findViewById(R.id.simple_wind5);
        mWind6 = findViewById(R.id.simple_wind6);
        mWind7 = findViewById(R.id.simple_wind7);
        mWind8 = findViewById(R.id.simple_wind8);
        mWind9 = findViewById(R.id.simple_wind9);
        mWind10 = findViewById(R.id.simple_wind10);
        mWind11 = findViewById(R.id.simple_wind11);
        mWind12 = findViewById(R.id.simple_wind12);
        mWind13 = findViewById(R.id.simple_wind13);
        mWind14 = findViewById(R.id.simple_wind14);
        mWind15 = findViewById(R.id.simple_wind15);

        mPanels.add(mWind1);
        mPanels.add(mWind2);
        mPanels.add(mWind3);
        mPanels.add(mWind4);
        mPanels.add(mWind5);
        mPanels.add(mWind6);
        mPanels.add(mWind7);
        mPanels.add(mWind8);
        mPanels.add(mWind9);
        mPanels.add(mWind10);
        mPanels.add(mWind11);
        mPanels.add(mWind12);
        mPanels.add(mWind13);
        mPanels.add(mWind14);
        mPanels.add(mWind15);

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
    public void execute(byte[] bytes) {
        String[] data = null;
        if (bytes != null) {
            if (bytes.length == 40) {
                if(arr==null)
                    arr = bytes;
                else{
                    arr = byteMergerAll(arr, bytes);
                }
                return;
            }
            if (arr != null && ((arr.length == 120 && bytes.length == 18) || (arr.length == 40 && bytes.length == 35))) {
                data = Util.byte2HexStr(byteMergerAll(arr, bytes)).split(" ");
                arr = null;
            } else {
                data = Util.byte2HexStr(bytes).split(" ");
            }
        }

        synchronized (BaseActivity.class) {
            //基本数据6位，状态码1位，验证值2位

            //测试用 start-----------------
//            boolean bCrc = verifyData(data);
//            if(data.length>=6) {
//                m_iCurSequence = Integer.parseInt(data[2], 16);
//                m_bCrc = bCrc;
//                if (!m_bCrc)
//                    m_iCrcFailed++;
//                if (m_bFirst) {
//                    m_iPreSequence = m_iCurSequence;
//                    m_bFirst = false;
//                }
//
//                int iDiff = 0;
//                if(m_iCurSequence!=m_iPreSequence){
//                    if(m_iCurSequence>m_iPreSequence){
//                        iDiff = m_iCurSequence-m_iPreSequence;
//                    }else{
//                        iDiff = m_iCurSequence-m_iPreSequence+256;
//                    }
//                    if(iDiff>1 ){
//                        m_iPakageLost += (iDiff-1);
//                    }
//                    m_iPreSequence = m_iCurSequence;
//                }
//
//            }
            //测试用 end-----------------

            if (data != null && data.length > 6 && verifyData(data)) {
                int cmd = Integer.parseInt(data[5],16);
                int result = Integer.parseInt(data[6],16);
                if (cmd != 0) {
                    switch (cmd) {
                        case 1://表示设置获取传感器数据通道返回结果
                            if (result != 1) {
//                                setCgqOk = false;
                                ToastUtils.showToast("设置传感器数据通道失败");
                            } else {
//                                setCgqOk = true;
                                setData(cmd);
                                ToastUtils.showToast("设置传感器数据通道成功");
                            }
                            break;
                        case 2://表示获取传感器数据通道结果。Num为总通道数，分别是Channel1, Channel2。
                            if (result != 1 || data.length < 10) {
                                ToastUtils.showToast("获取传感器数据通道失败");
                                break;
                            }
                            int tdNum = Integer.parseInt(data[7], 16);
                            preferencesUtil.saveParam(SJTGNUM, tdNum);
                            if (data.length == tdNum * 2 + 10) {
                                for (int i = 0; i < tdNum; i++) {
                                    preferencesUtil.saveParam(SJTG + i, Integer.parseInt(data[9 + 2 * i] + data[8 + 2 * i], 16));
                                    //setData(cmd);
                                }
                                setData(cmd);
                            }
                            break;
                        case 3://设置Device ID 返回结果
                            if (result == 0) {
//                                setSbOk = false;
                                ToastUtils.showToast("设置当前设备ID失败");
                            } else {
//                                setSbOk = true;
                                setData(cmd);
                                ToastUtils.showToast("设置当前设备ID成功");
                            }
                            break;
                        case 4://用于获取前device ID 编号
                            if (result == 0 || data.length != 10) {
                                ToastUtils.showToast("获取当前设备ID失败");
                                break;
                            }
                            preferencesUtil.saveParam(DQSBID, Integer.parseInt(data[7]));
                            setData(cmd);
                            break;
                        case 5://设置传感器ID返回结果。
                            if (result != 1) {
//                                setIdOk = false;
                                ToastUtils.showToast("设置传感器ID失败");
                            } else {
//                                setIdOk = true;
                                setData(cmd);
                                ToastUtils.showToast("设置传感器ID成功");
                            }
                            break;
                        case 6://获取前设备对应的传感器ID号，Num:传感器总数。默认为3。
                            if (result != 1 || data.length < 10) {
                                ToastUtils.showToast("获取前设备对应的传感器ID失败");
                                break;
                            }
                            int num = Integer.parseInt(data[7], 16);
                            preferencesUtil.saveParam(CGQNUM, num);
                            if (data.length == num + 10) {
                                for (int i = 1; i <= num; i++) {
                                    preferencesUtil.saveParam(CGQID + i, Integer.parseInt(data[8 + i - 1], 16));
                                }
                                setData(cmd);
                            }
                            break;
                        case 7://设置蓝牙名称返回结果
                            if (result != 1) {
//                                setLyOk = false;
                                ToastUtils.showToast("设置蓝牙名称失败");
                            } else {
//                                setLyOk = true;
                                setData(cmd);
                                ToastUtils.showToast("设置蓝牙名称成功");
                            }
                            break;
                        case 8://设置设备自组网工作频率返回结果。
                            if (result != 1) {
//                                setZzwOk = false;
                                ToastUtils.showToast("设置设备自组网工作频率失败");
                            } else {
//                                setZzwOk = true;
                                setData(cmd);
                                ToastUtils.showToast("设置设备自组网工作频率成功");
                            }
                            break;
                        case 9://获取设备自组网工作频率。由高八位和低八位组成。频率范围：420-510Mhz。频率step 为1Mhz。假如频率为433Mhz 时，Frequece_L=0XB1, Frequece_H=0x01。
                            if (result != 1 || data.length != 11) {
                                ToastUtils.showToast("获取设备自组网工作频率失败");
                                break;
                            }
                            preferencesUtil.saveParam(ZZWGZPL, Integer.parseInt(data[8] + data[7], 16));
                            setData(cmd);
                            break;
                        default:
                            ToastUtils.showToast(cmd + "未定义");
                    }
                }
                for (int i = 0; i < mActivityList.size(); i++) {
                    mActivityList.get(i).result(data);
                }

            } else {
//                ToastUtils.showToast("数据错误");
                if (data!=null){
                    //lijp
                    //ToastUtils.showToast(Arrays.toString(data));
                    ToastUtils.showToast("数据错误");
                }else {
                    ToastUtils.showToast("data为null");
                }
            }
        }

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

    private void fillWind(String[] data,StringBuffer stringBuffer,int data_index,int sensorid)
    {
        long sensorId = Long.parseLong(data[data_index], 16);
        String id = String.valueOf(sensorId);
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
        String fx = getNum(x12);
        stringBuffer.append( id + ";" + fs + ";" + fx + ";");
        mPanels.get(sensorid).setText((sensorid + 1) + "   " + sensorId + "    "+fs + "   " +  fx);
     }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void parseFS_FX(String[] data) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(dateformat1.format(System.currentTimeMillis()) + " ");//日期
            stringBuffer.append(dateformat.format(System.currentTimeMillis()));//时间
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
            if (totalSensorNum == 15) {
                long packetSensorNum = Long.parseLong(data[7], 16);

                int idx = 10;
                for(int i=0;i<packetSensorNum;i++){
                    fillWind(data,stringBuffer,idx, (int) ((index-1)*6+i));
                    idx += 21;
                }
                addTxtToFileBuffered(stringBuffer.toString());
            }
         }catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("数据解析出错");
        }
    }
}