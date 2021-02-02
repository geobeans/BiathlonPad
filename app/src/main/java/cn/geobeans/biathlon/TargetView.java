package cn.geobeans.biathlon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class TargetView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private Paint mCirclePaint; //画圆画笔
    private Paint mCirclePaint1; //画圆画笔
    private Paint mCirclePaint2; //画圆画笔
    private Paint mDashPaint; //画圆画笔
    private Paint mBackgroudPaint;

    private Paint mHitPaint;

    int mIndex = 0;
    int mSelHit = -1;

    float[] mHitX = new float[5];
    float[] mHitY = new float[5];

    float[] mHitX0 = new float[5];
    float[] mHitY0 = new float[5];

    public TargetView(Context context) {
        super(context);
        init(null, 0);
    }

    public TargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TargetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TargetView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.TargetView_targetViewString);
        mExampleColor = a.getColor(
                R.styleable.TargetView_targetViewColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.TargetView_targetViewDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.TargetView_targetViewDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.TargetView_targetViewDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        //画圆画笔初始化
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setARGB(255,0,0,0);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBackgroudPaint = new Paint();
        mBackgroudPaint.setARGB(247,247,9,104);
        mBackgroudPaint.setStyle(Paint.Style.FILL);

        mCirclePaint1 = new Paint();
        mCirclePaint1.setAntiAlias(true);
        mCirclePaint1.setARGB(255,255,255,255);
        mCirclePaint1.setStyle(Paint.Style.STROKE);
        mCirclePaint1.setStrokeWidth(10.0f);

        mCirclePaint2 = new Paint();
        mCirclePaint2.setAntiAlias(true);
        mCirclePaint2.setARGB(255,255,255,255);
        mCirclePaint2.setStyle(Paint.Style.STROKE);
        mCirclePaint2.setStrokeWidth(5.0f);

        mHitPaint = new Paint();
        mHitPaint.setARGB(255,216,216,216);
        mHitPaint.setStyle(Paint.Style.FILL);

        mDashPaint = new Paint();
        mDashPaint.setAntiAlias(true);
        mDashPaint.setARGB(255,255,255,255);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        mDashPaint.setStrokeWidth(5);

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        //invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //touch_start(x, y);
                if(mIndex<5) {
                    mHitX[mIndex] = x;
                    mHitY[mIndex] = y;
                    invalidate();
                }else{
                    mSelHit = selectHit(x,y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //touch_move(x, y);
                if(mSelHit!=-1 && mIndex>4){
                    mHitX[mSelHit] = x;
                    mHitY[mSelHit] = y;
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                //touch_up();
                //invalidate();
                if(mIndex<5) {
                    mIndex++;
                }else{
                    mSelHit = -1;
                }
//                if(mIndex>4){
//                    mIndex=0;
//                }
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHitX[0] = 40.0f;
        mHitX[1] = 130.0f;
        mHitX[2] = 220.0f;
        mHitX[3] = 310.0f;
        mHitX[4] = 400.0f;

        mHitY[0] = h-50.0f;
        mHitY[1] = h-50.0f;
        mHitY[2] = h-50.0f;
        mHitY[3] = h-50.0f;
        mHitY[4] = h-50.0f;

        System.arraycopy(mHitX,0,mHitX0,0,5);
        System.arraycopy(mHitY,0,mHitY0,0,5);
    }

    private int selectHit(float x, float y)
    {
        float dist = java.lang.Float.MAX_VALUE;
        int sel = -1;

        for(int i=0;i<5;i++){
            float d = (mHitX[i]-x)*(mHitX[i]-x)+(mHitY[i]-y)*(mHitY[i]-y);
            if(d<dist){
                dist = d;
                sel = i;
            }
        }

        if(dist < 900){
            return sel;
        }else
            return -1;
    }

    public void reset()
    {
        mIndex = 0;
        System.arraycopy(mHitX0,0,mHitX,0,5);
        System.arraycopy(mHitY0,0,mHitY,0,5);
        invalidate();
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

        canvas.drawRect(0,0,contentWidth,contentHeight,mBackgroudPaint);
        //中心点
        int centerX = contentWidth/2;
        int centerY = contentHeight/2;
        int radius = contentHeight>contentWidth?contentWidth:contentHeight;
        radius = (radius-150)/2;

        canvas.drawCircle(centerX, centerY, radius, mCirclePaint);
        canvas.drawCircle(centerX, centerY, radius/4, mCirclePaint2);
        canvas.drawCircle(centerX, centerY, radius/2, mCirclePaint1);
        canvas.drawCircle(centerX, centerY, radius*3/4, mCirclePaint2);
        canvas.drawCircle(centerX, centerY, radius, mCirclePaint1);

        canvas.drawLine(centerX-radius,centerY,centerX+radius,centerY,mDashPaint);
        canvas.drawLine(centerX,centerY-radius,centerX,centerY+radius,mDashPaint);

        for(int i=0;i<5;i++) {
            canvas.drawCircle(mHitX[i],mHitY[i],40,mHitPaint);
        }
        for(int i=0;i<5;i++) {
            canvas.drawCircle(mHitX0[i],mHitY0[i],40,mCirclePaint2);
        }
        // Draw the text.
//        canvas.drawText(mExampleString,
//                paddingLeft + (contentWidth - mTextWidth) / 2,
//                paddingTop + (contentHeight + mTextHeight) / 2,
//                mTextPaint);

        // Draw the example drawable on top of the text.
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }



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
     * Sets the view"s example string attribute value. In the example view, this string
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
     * Sets the view"s example color attribute value. In the example view, this color
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
     * Sets the view"s example dimension attribute value. In the example view, this dimension
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
     * Sets the view"s example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}