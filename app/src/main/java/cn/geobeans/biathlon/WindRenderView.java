package cn.geobeans.biathlon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import cn.geobeans.biathlon.utils.Idw;

/**
 * TODO: document your custom view class.
 */
public class WindRenderView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private Paint mWindPaint;
    private float mTextWidth;
    private float mTextHeight;

    private Idw mIdw = new Idw();
    private boolean mbInitialized = false;

    private int mCol = 83*3;
    private int mRow = 50*3;
    private int mGap = 20;
    private int mSensorNum = 6;

    private float[][] mWindSpeeds;
//    private int mCcolors[] ={0xff006837, 0xff1a9850, 0xff66bd63, 0xffa6d96a, 0xffd9ef8b, 0xffffffbf,
//            0xfffee08b, 0xfffdae61, 0xfff46d43, 0xffd73027, 0xffa50026};
    private Paint mDashPaint;

    private static final int mColors[][] = {{69 , 117, 18},
            {69 , 117, 18},
            {71 , 118, 18},
            {72 , 119, 18},
            {74 , 121, 18},
            {74 , 121, 18},
            {76 , 122, 18},
            {78 , 123, 18},
            {80 , 124, 18},
            {81 , 125, 18},
            {83 , 126, 18},
            {84 , 127, 18},
            {86 , 128, 18},
            {88 , 129, 18},
            {90 , 131, 18},
            {92 , 132, 18},
            {92 , 132, 18},
            {94 , 133, 18},
            {95 , 134, 18},
            {97 , 135, 18},
            {99 , 136, 18},
            {99 , 136, 18},
            {101, 137, 18},
            {103, 138, 18},
            {105, 139, 18},
            {105, 139, 18},
            {106, 140, 18},
            {108, 141, 18},
            {110, 142, 18},
            {110, 143, 18},
            {112, 144, 18},
            {115, 147, 18},
            {117, 148, 18},
            {117, 148, 18},
            {119, 149, 18},
            {121, 150, 18},
            {123, 152, 18},
            {123, 152, 18},
            {125, 153, 18},
            {127, 154, 18},
            {128, 155, 18},
            {128, 155, 18},
            {130, 157, 18},
            {132, 158, 18},
            {132, 158, 18},
            {134, 160, 18},
            {136, 161, 18},
            {138, 162, 18},
            {138, 163, 18},
            {140, 164, 18},
            {141, 165, 18},
            {143, 166, 18},
            {143, 166, 18},
            {145, 168, 18},
            {147, 169, 18},
            {151, 172, 18},
            {151, 173, 18},
            {153, 174, 18},
            {155, 175, 18},
            {155, 176, 18},
            {157, 177, 18},
            {159, 178, 18},
            {160, 179, 18},
            {160, 180, 18},
            {162, 181, 18},
            {164, 182, 18},
            {164, 183, 18},
            {166, 184, 18},
            {168, 185, 18},
            {170, 186, 18},
            {170, 187, 18},
            {172, 188, 18},
            {174, 189, 18},
            {176, 191, 19},
            {176, 191, 18},
            {178, 194, 19},
            {178, 194, 18},
            {180, 194, 18},
            {183, 196, 19},
            {183, 196, 18},
            {185, 199, 19},
            {185, 199, 18},
            {189, 201, 19},
            {189, 201, 19},
            {191, 204, 19},
            {192, 204, 19},
            {194, 207, 19},
            {196, 207, 19},
            {196, 207, 19},
            {198, 209, 19},
            {199, 209, 19},
            {202, 212, 19},
            {202, 212, 19},
            {205, 214, 19},
            {206, 214, 19},
            {208, 217, 19},
            {209, 217, 19},
            {211, 219, 19},
            {212, 219, 19},
            {212, 219, 18},
            {215, 222, 19},
            {215, 222, 18},
            {218, 224, 19},
            {218, 224, 19},
            {221, 227, 19},
            {221, 227, 19},
            {224, 230, 19},
            {224, 230, 19},
            {226, 232, 19},
            {227, 232, 19},
            {230, 235, 19},
            {230, 235, 19},
            {233, 237, 19},
            {233, 237, 19},
            {237, 240, 19},
            {237, 240, 19},
            {240, 242, 19},
            {240, 242, 19},
            {242, 245, 19},
            {243, 245, 19},
            {246, 247, 19},
            {245, 247, 19},
            {249, 250, 19},
            {249, 250, 19},
            {251, 252, 19},
            {251, 252, 19},
            {255, 255, 19},
            {255, 255, 19},
            {255, 255, 19},
            {255, 253, 18},
            {255, 252, 18},
            {255, 249, 18},
            {255, 248, 18},
            {255, 247, 18},
            {255, 244, 18},
            {255, 243, 18},
            {255, 241, 17},
            {255, 240, 17},
            {255, 238, 17},
            {255, 237, 17},
            {255, 235, 17},
            {255, 233, 17},
            {255, 231, 17},
            {255, 229, 16},
            {255, 228, 16},
            {255, 225, 16},
            {255, 225, 16},
            {255, 223, 16},
            {255, 220, 16},
            {255, 219, 16},
            {255, 218, 15},
            {252, 214, 15},
            {252, 211, 15},
            {252, 211, 15},
            {252, 209, 15},
            {252, 209, 15},
            {252, 206, 14},
            {252, 204, 14},
            {252, 203, 14},
            {252, 201, 14},
            {252, 200, 14},
            {252, 198, 14},
            {252, 197, 14},
            {252, 195, 14},
            {252, 194, 13},
            {252, 194, 13},
            {250, 189, 13},
            {250, 189, 13},
            {250, 185, 13},
            {250, 185, 13},
            {250, 182, 13},
            {250, 182, 13},
            {250, 181, 12},
            {250, 178, 12},
            {250, 177, 12},
            {250, 177, 12},
            {250, 173, 12},
            {247, 172, 12},
            {247, 168, 11},
            {247, 168, 11},
            {247, 168, 11},
            {247, 164, 11},
            {247, 164, 11},
            {247, 163, 11},
            {247, 163, 11},
            {247, 159, 11},
            {245, 157, 11},
            {245, 156, 10},
            {245, 153, 10},
            {245, 152, 10},
            {245, 152, 10},
            {245, 149, 10},
            {245, 148, 10},
            {242, 146, 10},
            {242, 145, 99},
            {242, 142, 99},
            {242, 141, 97},
            {242, 141, 97},
            {242, 141, 97},
            {242, 136, 94},
            {240, 135, 93},
            {240, 133, 91},
            {240, 133, 91},
            {240, 129, 89},
            {240, 129, 89},
            {240, 129, 89},
            {240, 127, 86},
            {237, 123, 85},
            {237, 122, 83},
            {237, 122, 83},
            {237, 120, 81},
            {237, 117, 81},
            {237, 117, 81},
            {235, 114, 77},
            {235, 114, 77},
            {235, 110, 75},
            {235, 110, 75},
            {235, 110, 75},
            {235, 108, 73},
            {232, 104, 72},
            {232, 102, 70},
            {232, 102, 70},
            {232, 102, 70},
            {232, 97 , 67},
            {230, 96 , 67},
            {230, 96 , 67},
            {230, 95 , 64},
            {230, 95 , 64},
            {230, 90 , 62},
            {227, 89 , 61},
            {227, 89 , 61},
            {227, 87 , 59},
            {227, 84 , 59},
            {227, 84 , 59},
            {224, 81 , 56},
            {224, 81 , 56},
            {224, 77 , 54},
            {224, 77 , 54},
            {224, 77 , 54},
            {222, 74 , 51},
            {222, 71 , 51},
            {222, 71 , 51},
            {222, 69 , 49},
            {219, 65 , 48},
            {219, 63 , 46},
            {219, 63 , 46},
            {219, 63 , 46},
            {219, 58 , 44},
            {217, 58 , 43},
            {217, 58 , 43},
            {217, 53 , 41},
            {217, 53 , 41},
            {214, 52 , 41},
            {214, 47 , 39},
            {214, 47 , 39}};

    public WindRenderView(Context context) {
        super(context);
        init(null, 0);
    }

    public WindRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WindRenderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.WindRenderView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.WindRenderView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.WindRenderView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.WindRenderView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.WindRenderView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.WindRenderView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        mWindPaint = new Paint();
        mWindPaint.setARGB(255,255,0,0);
        mWindPaint.setStyle(Paint.Style.FILL);

        mDashPaint = new Paint();
        mDashPaint.setColor(mExampleColor);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(4.0f);
        mIdw.init(mSensorNum);
    }

    public void InputWind() {
        float fWindSpeeds[]=new float[mSensorNum];
        for(int i=0;i<mSensorNum;i++){
            fWindSpeeds[i] = (float) (2.0f+ Math.random()*7.0f);
        }
        mWindSpeeds = mIdw.Inter_Speed(fWindSpeeds);
        mbInitialized = true;
    }

    public void InputWind(float fWindSpeeds[]) {
        mWindSpeeds = mIdw.Inter_Speed(fWindSpeeds);
        mbInitialized = true;
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
//        canvas.drawText(mExampleString,
//                paddingLeft + (contentWidth - mTextWidth) / 2,
//                paddingTop + (contentHeight + mTextHeight) / 2,
//                mTextPaint);
//
//        // Draw the example drawable on top of the text.
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }
        float startLX = paddingLeft + 60;
        float startTY = paddingTop + mGap;
        float startRX = 95.0f;
        float startBY = 55.0f;

        float tLefty = (float) ((contentHeight-startTY-startBY)/5);
        int dist = 10;
        for(int i=1;i<5;i++) {
            String strDist = String.valueOf(dist);
            canvas.drawText(strDist,
                    paddingLeft,
                    startTY + tLefty*i +mTextHeight/2.0f,
                    mTextPaint);
            dist+=10;
            canvas.drawLine(startLX-8,startTY + tLefty*i,startLX,startTY + tLefty*i,mDashPaint);
        }
        dist = 5;
        float tBottomX = (contentWidth-startLX-startRX)/6.0f;
        float tMidX = (contentWidth-startLX-startRX)/30.0f;
        for(int i=1;i<=6;i++) {
            String strDist = String.valueOf(dist);
            float textWidth = mTextPaint.measureText(strDist);
            canvas.drawText(strDist,
                    startLX + i * tBottomX-textWidth-tMidX/2.0f,
                    contentHeight-startBY/3.0f,
                    mTextPaint);
            dist+=5;
            //canvas.drawLine(startLX + i * tBottomX,startTY + tLefty*i,startLX + i * tBottomX,startTY + tLefty*i,mDashPaint);
        }

        float wx = (float) ((contentWidth-startLX-startRX)/mCol);
        float wy = (float) ((contentHeight-startTY-startBY)/mRow);

        if(mbInitialized) {
            for (int i = 0; i < mRow; i++) {
                for (int j = 0; j < mCol; j++) {
                    float z = (mWindSpeeds[i][j] - mIdw.MinVal) / (mIdw.MaxVal - mIdw.MinVal);
                    int col[] = mColors[(int) Math.floor((mColors.length - 1.0) * z)];
                    mWindPaint.setARGB(255,col[0],col[1],col[2]);
                    canvas.drawRect(startLX + j * wx, startTY + i * wy, startLX + j * wx + wx, startTY + i * wy + wy, mWindPaint);
                }
            }
        }

        float wsy = (float) ((contentHeight-startTY-startBY)/mColors.length);
        for(int i=0;i<mColors.length;i++){
            int col[] = mColors[mColors.length-i-1];
            mWindPaint.setARGB(255,col[0],col[1],col[2]);
            canvas.drawRect(contentWidth-startRX+10, startTY + i * wsy, contentWidth-55, startTY + i * wsy + wsy, mWindPaint);
        }

        float fMin = (float) Math.floor(mIdw.MinVal);
        float fMax = (float) Math.ceil(mIdw.MaxVal);
        canvas.drawText(String.valueOf(fMin),
                contentWidth-55 ,
                startTY + mColors.length * wsy,
                mTextPaint);
        canvas.drawText(String.valueOf(fMax),
                contentWidth-55 ,
                startTY+mTextHeight,
                mTextPaint);
        canvas.drawRect(startLX , startTY , startLX + mCol * wx , startTY + mRow * wy, mDashPaint);
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
