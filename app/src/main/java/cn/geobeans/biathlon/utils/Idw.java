package cn.geobeans.biathlon.utils;

///IDW类，两个函数。初始化函数和插值函数；
/// IDW(int numberS)是初始化参数并计算权重系数，输入1个int，需要显示传感器个数，5或者6
/// Inter_Speed(float []ValueSensor)函数是对速度进行插值，输入是每一秒的5或者6个风速仪的速度，输出是 50*83大小的风速数组
///风向的插值后面再实现
public final class Idw {
    // IDW(Inverse Distance Weight)
    // 初始化，计算权重函数
    int Length = 83*3;//靶场长度为83米；
    int Width = 50*3;//靶场宽度为50米;
    int NumSensor = 6;//风速传感器的数量为5；
    //风速传感器的位置 X-长度， Y-宽度，坐标，（0,0）坐标是30号靶位点
    float  PositionSensor [][];
    float locX[] = new float[Length];
    float locY[] = new float[Width];

    float [][][] Weight = new float[Width][Length][NumSensor];//权重系数
    public float MinVal = 100;
    public float MaxVal = 0;

    //初始化函数,需要计算权重系数,输入参数为 需要显示的传感器个数，5或者6个；
    public void init(int numberS){
        NumSensor = numberS;
        //5个传感器
        if (NumSensor == 5){
            PositionSensor = new float[][]{{1 / 2.0f, 1 / 2.0f}, {21 / 83.0f, 15 / 50.0f}, {3 / 83.0f, 15 / 50.0f}, {1 / 83.0f, 45 / 50.0f}, {63 / 83.0f, 45 / 50.0f}};
        } else if  (NumSensor == 6){
            PositionSensor = new float[][]{{1 / 2.0f, 15 / 50.0f}, {21 / 83.0f, 15 / 50.0f}, {3 / 83.0f, 15 / 50.0f}, {1 / 83.0f, 45 / 50.0f}, {63 / 83.0f, 45 / 50.0f},{1 / 2.0f, 45 / 50.0f}};
        }


        //初始化 locX
        for (int i = 0;i< Length;i++){
            locX[i] = (i+1.0f)/Length;
        }
        //初始化 locY
        for (int i = 0;i<Width;i++){
            locY[i] = (i+1.0f)/Width;
        }
        //计算权重
        for(int i =0;i<Width;i++){
            for(int j = 0;j< Length;j++){
                float dsum = 0;
                for(int k = 0;k<NumSensor;k++){
                    Weight[i][j][k] = (float) (1.0f/((locY[i]-PositionSensor[k][1])*(locY[i]-PositionSensor[k][1])+(locX[j]-PositionSensor[k][0])*(locX[j]-PositionSensor[k][0])+0.01));
                    dsum = dsum + Weight[i][j][k];
                }
                for(int k = 0;k<NumSensor;k++){
                    Weight[i][j][k] = Weight[i][j][k]/dsum;
                }
            }
        }
    }

    //计算每一秒（帧）的数据，返回50*83的数组,风速
    //////ValueSensor就是需要输入的每一秒的数组，如果是风速的话，那就是5个风速值；
    public float [][] Inter_Speed(float []ValueSensor){
        MinVal = 100;
        MaxVal = 0;
        float value [][] = new float[Width][Length];
        for(int i =0;i<Width;i++) {
            for (int j = 0; j < Length; j++) {
                value[i][j]= 0 ;
                for (int k = 0;k<NumSensor;k++){
                    value[i][j] = value[i][j] +ValueSensor[k]*Weight[i][j][k];
                    if(value[i][j]<MinVal){
                        MinVal = value[i][j];
                    }
                    if(value[i][j]>MaxVal){
                        MaxVal = value[i][j];
                    }
                }
            }
        }
        return value;
    }
}
