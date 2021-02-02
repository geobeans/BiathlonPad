package cn.geobeans.biathlon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.geobeans.biathlon.utils.NumRangeInputFilter;
import cn.geobeans.biathlon.utils.ToastUtils;
import cn.geobeans.biathlon.utils.ModuleUtil;

public class SettingAdminActivity extends BaseActivity implements View.OnClickListener, ModuleUtil.CallBackInterface {
    private ImageView setting;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;

    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private LinearLayout layout5;

    private int zzwgzpl;
    private int lymc;
    private int dqsb;
    private String cgqList = "";
    private String tdgs = "";
    private List<String> tbList = new ArrayList<>();

    private String strChannel = "";
    private List<String> listChannel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityList.add(this);
        //ModuleUtil.getInstance().setOnCallBackInterface(this);
        setContentView(R.layout.activity_admin_setting);
        try {
            init();
            initView();
            initListener();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        zzwgzpl = (int) preferencesUtil.getParam(ZZWGZPL, -1);
        if (zzwgzpl != -1) {
            text5.setText(zzwgzpl + "");
        }

        lymc = (int) preferencesUtil.getParam(LJLAMC, -1);
        if (lymc != -1) {
            text4.setText(lymc + "");
        }
        String cgqList = "";
        int cgqNum = (int) preferencesUtil.getParam(CGQNUM, -1);
        if (cgqNum != -1) {
            for (int i = 1; i <= cgqNum; i++) {
                cgqList += preferencesUtil.getParam(CGQID + i, -1) + "  ";
            }
        }
        text3.setText(cgqList);

        String sjtgnumList = "";
        int sjtgnum = (int) preferencesUtil.getParam(SJTGNUM, -1);
        if (sjtgnum != -1) {
            for (int i = 0; i < sjtgnum; i++) {
                sjtgnumList += preferencesUtil.getParam(SJTG + i, -1) + "  ";
            }
        }

//        sjtgnumList = (String) preferencesUtil.getParam(SJTG,null);
//        if(sjtgnumList !=null)
//            text1.setText(sjtgnumList);

        dqsb = (int) preferencesUtil.getParam(DQSBID, -1);
        if (dqsb != -1) {
            text2.setText(dqsb + "");
        }

    }

    @Override
    public void result(String[] data) {

    }

    @Override
    public void setData(int cmd) {
        switch (cmd) {
            case 1://表示设置获取传感器数据通道返回结果
                //preferencesUtil.saveParam(SJTG, tdgs);
                //text1.setText(tdgs);

//                if (tdgs.equals("2")) {
//                    text1.setText("401 501");
//                } else if (tdgs.equals("3")) {
//                    text1.setText("401 501 100");
//                }

                if (listChannel == null) {
                    return;
                }
                preferencesUtil.saveParam(SJTGNUM, listChannel.size());
                for (int i = 0; i < listChannel.size(); i++) {
                    preferencesUtil.saveParam(SJTG + i, Integer.parseInt(listChannel.get(i)));
                }
                text1.setText(strChannel);
                break;
            case 2://表示获取传感器数据通道结果。Num为总通道数，分别是Channel1, Channel2。
//                String sjtgnumList = (String) preferencesUtil.getParam(SJTG, -1);
//                text1.setText(sjtgnumList);
                //String sjtgnumList = "";
                int sjtgnum = (int) preferencesUtil.getParam(SJTGNUM, -1);
                if (sjtgnum != -1) {
                    for (int i = 0; i < sjtgnum; i++) {
                        strChannel += preferencesUtil.getParam(SJTG + i, -1) + "  ";
                    }
                }
                text1.setText(strChannel);
                break;
            case 3://设置Device ID 返回结果
                preferencesUtil.saveParam(DQSBID, dqsb);
                text2.setText(dqsb + "");
                break;
            case 4://用于获取前device ID 编号
                dqsb = (int) preferencesUtil.getParam(DQSBID, -1);
                if (dqsb != -1) {
                    text2.setText(dqsb + "");
                }
                break;
            case 5://设置传感器ID返回结果。
                if (tbList == null) {
                    return;
                }
                preferencesUtil.saveParam(CGQNUM, tbList.size());
                for (int i = 1; i <= tbList.size(); i++) {
                    preferencesUtil.saveParam(CGQID + i, Integer.parseInt(tbList.get(i - 1)));
                }
                text3.setText(cgqList);
                break;
            case 6://获取前设备对应的传感器ID号，Num:传感器总数。默认为3。
                int cgqNum = (int) preferencesUtil.getParam(CGQNUM, -1);
                if (cgqNum != -1) {
                    for (int i = 1; i <= cgqNum; i++) {
                        cgqList += preferencesUtil.getParam(CGQID + i, -1) + "  ";
                    }
                }
                text3.setText(cgqList);
                break;
            case 7://设置蓝牙名称返回结果
                preferencesUtil.saveParam(LJLAMC, lymc);
                text4.setText(lymc + "");
                break;
            case 8://设置设备自组网工作频率返回结果。
                preferencesUtil.saveParam(ZZWGZPL, zzwgzpl);
                text5.setText(zzwgzpl + "");
                break;
            case 9://获取设备自组网工作频率。由高八位和低八位组成。频率范围：420-510Mhz。频率step 为1Mhz。假如频率为433Mhz 时，Frequece_L=0XB1, Frequece_H=0x01。
                zzwgzpl = (int) preferencesUtil.getParam(ZZWGZPL, -1);
                if (zzwgzpl == -1) {
                    ToastUtils.showToast("获取自组网工作频率失败");
                    text5.setText("");
                }
                text5.setText(zzwgzpl + "");
                break;
        }
    }

    /**
     * CMD为3时，Data 数据格式如下
     * 此命令用于设置当前device ID 编号。如：设置当前设备编号为2
     */
    private void setSBId(String id) {
        List<String> data = new ArrayList<>();
        data.add(format(id));

        setSensorData("03", data);

//        Message  message = Message.obtain();
//        message.what = 3;
//        handler.sendMessageDelayed(message, 2000);
    }

    /**
     * 3.1.5 CMD为5时，Data 数据格式如下：
     * 此命令用于设置三个传感器的ID号。比如第一台传感器ID 为1，第二台传感器ID 为3，第三台传感器ID 为5。则数据格式如下：
     */
    private void setCGQSJTD(String num) {
        List<String> data = new ArrayList<>();
        String[] split = num.split("-");
        strChannel = "";
        listChannel.clear();
        if (split.length > 1) {
            int iChannelNum = split.length;
            String strNum = Integer.toHexString(iChannelNum);
            if (strNum.length() == 1) {
                strNum =  "0" + strNum;
            }
            data.add(strNum);
            for(int i=0;i<split.length;i++){
                strChannel += split[i] + " ";
                listChannel.add(split[i]);
                String strChannel = Integer.toHexString(Integer.parseInt(split[i]));

                if(strChannel.length()==2){
                    data.add(strChannel);
                    data.add("00");
                }else if(strChannel.length()==3){
                    String strLow = strChannel.substring(1,3);
                    data.add(strLow);
                    String strHigh = "0" + strChannel.substring(0,1);
                    data.add(strHigh);
                }else if(strChannel.length()==4){
                    String strLow = strChannel.substring(2,4);
                    data.add(strLow);
                    String strHigh = strChannel.substring(0,2);
                    data.add(strHigh);
                }else{
                    ToastUtils.showToast("数据格式错误");
                    return;
                }
             }
            setSensorData("01", data);
            //setSensorData("02",null);
        } else {
            ToastUtils.showToast("至少设置两个通道数据以“-”分割");
            return;
        }

//        List<String> data = new ArrayList<>();
//        if (num.equals("2")) {
//            data.add("02");
//            data.add("91");
//            data.add("01");
//            data.add("f5");
//            data.add("01");
//            setSensorData("01", data);
//        } else if (num.equals("3")) {
//            data.add("03");
//            data.add("91");
//            data.add("01");
//            data.add("f5");
//            data.add("01");
//            data.add("64");
//            data.add("00");
//            setSensorData("01", data);
//        }
//        Message message = Message.obtain();
//        message.what = 1;
//        handler.sendMessageDelayed(message, 2000);
    }

    /**
     * 3.1.5 CMD为5时，Data 数据格式如下：
     * 此命令用于设置三个传感器的ID号。比如第一台传感器ID 为1，第二台传感器ID 为3，第三台传感器ID 为5。则数据格式如下：
     */
    private void setCGQId(List<String> data) {
        setSensorData("05", data);

//        Message  message = Message.obtain();
//        message.what = 5;
//        handler.sendMessageDelayed(message, 2000);
    }


    /**
     * 3.1.7 CMD为7时，Data 数据格式如下：
     * 假设要设置蓝牙的名称为：BleDAH_02
     * 此值小于等于99
     */
    private void setBluetoothName(String bluetooth) {
        List<String> data = new ArrayList<>();
        data.add(format(bluetooth));
        setSensorData("07", data);
//        Message message = Message.obtain();
//        message.what = 7;
//        handler.sendMessageDelayed(message, 2000);
    }

    /**
     * 3.1.8 CMD为8时，Data 数据格式如下：
     * 设置自组网工作频率，频率范围：420-510Mhz。频率step 为1Mhz。假如频率为433Mhz 时，Frequece_L=0XB1, Frequece_H=0x01。
     */
    private void setPL(String pl) {
        int parseInt = Integer.parseInt(pl);
        if (parseInt >= 420 && parseInt <= 510) {
            List<String> data = new ArrayList<>();
            String crc = getCrc(Integer.toHexString(Integer.parseInt(pl, 10)).toUpperCase());
            if (crc.length() == 4) {
                data.add(crc.substring(0, 2));
                data.add(crc.substring(2));
                setSensorData("08", data);
            }
//            Message    message = Message.obtain();
//            message.what = 8;
//            handler.sendMessageDelayed(message, 2000);
        } else ToastUtils.showToast("数值未在区间内");
    }

    private void initView() {
        setting = findViewById(R.id.setting);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
    }

    private void initListener() {
        setting.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == setting) {
            SettingAdminActivity.this.finish();
        } else if (v == layout1) {
            showEditAlertDialog(text1, 1);
        } else if (v == layout2) {
            showEditAlertDialog(text2, 2);
        } else if (v == layout3) {
            showEditAlertDialog(text3, 3);
        } else if (v == layout4) {
            showEditAlertDialog(text4, 4);
        } else if (v == layout5) {
            showEditAlertDialog(text5, 5);
        }
    }

    public void showEditAlertDialog(final TextView view, final int tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText edit = (EditText) View.inflate(this, R.layout.edter_layout, null);

        if (tag == 3) {
            builder.setTitle("同时设置三个传感器ID以“-”分割");
        } else if (tag == 1) {
            builder.setTitle("输入通道数据以“-”分割");
            //edit.setText(view.getText());
        } else
            builder.setTitle("请输入");

        //final EditText edit = (EditText) View.inflate(this, R.layout.edter_layout, null);
        if (tag == 5) {
            edit.setFilters(new InputFilter[]{new NumRangeInputFilter(510, 420)});
        } else if (tag != 3 && tag != 1) {
            edit.setFilters(new InputFilter[]{new NumRangeInputFilter(99, 0)});
        }

        builder.setView(edit);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String trim = edit.getText().toString().trim();
                    if (edit.getText().toString().trim() != null) {
                        switch (tag) {
                            case 1:
//                                if (trim.equals("2") || trim.equals("3")) {
//                                    tdgs = trim;
//                                    setCGQSJTD(trim);
//                                }
                                tdgs = trim;
                                setCGQSJTD(trim);
                                break;
                            case 2:
                                dqsb = Integer.parseInt(trim);
                                setSBId(trim);

                                break;
                            case 3:
                                String[] split = trim.split("-");
                                tbList.clear();
                                if (split.length > 2) {
                                    cgqList = "";
                                    tbList.add(format(split.length + ""));
                                    for (int i = 0; i < split.length; i++) {
                                        cgqList += split[i] + "  ";
                                        tbList.add(format(split[i]));
                                    }
                                    setCGQId(tbList);

                                } else {
                                    ToastUtils.showToast("同时设置三个传感器以“-”分割");
                                }
                                break;
                            case 4:
                                lymc = Integer.parseInt(trim);
                                setBluetoothName(trim);


                                break;
                            case 5:
                                zzwgzpl = Integer.parseInt(trim);
                                if (zzwgzpl < 420) {
                                    ToastUtils.showToast("设置失败最小值420");
                                    break;
                                }
                                setPL(trim);


                                break;
                        }
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        mActivityList.remove(this);
        super.onDestroy();
    }
}
