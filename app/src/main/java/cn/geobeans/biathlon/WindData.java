package cn.geobeans.biathlon;

import java.util.ArrayList;
import java.util.List;

public class WindData {
    public String m_curTime;
    public  String[] m_strFs = new String[15];
    public  float[] m_dFx = new float[15];
    public String m_strQiwen;
    public String m_strQiya;

    public WindData(String curTime,String[] strFs, double[] dFx, String strQiwen, String strQiya) {
        this.m_curTime = curTime;
        System.arraycopy(strFs,0,m_strFs,0,15);
        System.arraycopy(dFx,0,m_dFx,0,15);
        this.m_strQiwen = strQiwen;
        this.m_strQiya = strQiya;
    }

    public WindData() {
    }
}
