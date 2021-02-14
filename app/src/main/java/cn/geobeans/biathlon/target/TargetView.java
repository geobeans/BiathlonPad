package cn.geobeans.biathlon.target;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.DashPathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.geobeans.biathlon.R;

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

    private onViewClick mViewClick;

    private int mContentWidth = 0;
    private int mContentHeight = 0;

    int mIndex = 0;
    int mSelHit = -1;

    float[] mHitX = new float[8];
    float[] mHitY = new float[8];

    float[] mHitX0 = new float[8];
    float[] mHitY0 = new float[8];

    boolean mbUseNormValue = false;
    float[] mNormHitX = new float[8];
    float[] mNormHitY = new float[8];

    Date[] mHitDate = new Date[8];

    List<Bitmap> mbmpHits = new ArrayList<>();

    public TargetView(Context context) {
        super(context);
        init(context,null, 0);
    }

    public TargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public TargetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs, defStyle);
    }

    private void init(Context context,AttributeSet attrs, int defStyle) {
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
        //mBackgroudPaint.setARGB(247,247,9,104);
        mBackgroudPaint.setARGB(255,255,0,0);
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

        try {
            for(int i=1;i<=8;i++) {
                Bitmap bmpHit = BitmapFactory.decodeStream(context.getAssets().open(i+".png"));
                mbmpHits.add(bmpHit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void setOnViewClick(onViewClick click) {
        this.mViewClick = click;
    }


    public interface onViewClick {

        void onClick(int index);
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
                    mHitDate[mIndex] = new Date(System.currentTimeMillis());
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
                    mViewClick.onClick(mIndex);
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

    public float getHitX(int index)
    {
        return mHitX[index]/mContentWidth;
    }

    public float getHitY(int index)
    {
        return mHitY[index]/mContentHeight;
    }

    public Date getHitDate(int index)
    {
        return mHitDate[index];
    }

    public void setHitX(float[] x)
    {
        mbUseNormValue = true;
        System.arraycopy(x,0,mNormHitX,0,8);
        //invalidate();
    }

    public void setHitY(float[] y)
    {
        mbUseNormValue = true;
        System.arraycopy(y,0,mNormHitY,0,8);
        //invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int radius = h>w?w:h;
        radius = (radius-150)/2;
        int hitRadius = radius/12;

        for(int i=0;i<5;i++){
            mHitX[i] = hitRadius + 2*i*(hitRadius+10) +10;
            mHitY[i] = h-hitRadius-10;
        }
//        mHitX[0] = hitRadius;
//        mHitX[1] = 130.0f;
//        mHitX[2] = 220.0f;
//        mHitX[3] = 310.0f;
//        mHitX[4] = 400.0f;
//
//        mHitY[0] = h-50.0f;
//        mHitY[1] = h-50.0f;
//        mHitY[2] = h-50.0f;
//        mHitY[3] = h-50.0f;
//        mHitY[4] = h-50.0f;

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

        mContentWidth = getWidth() - paddingLeft - paddingRight;
        mContentHeight = getHeight() - paddingTop - paddingBottom;

        canvas.drawRect(0,0,mContentWidth,mContentHeight,mBackgroudPaint);
        //中心点
        int centerX = mContentWidth/2;
        int centerY = mContentHeight/2;
        int radius = mContentHeight>mContentWidth?mContentWidth:mContentHeight;
        radius = radius*4/10;

        canvas.drawCircle(centerX, centerY, radius, mCirclePaint);
        canvas.drawCircle(centerX, centerY, radius/4, mCirclePaint2);
        canvas.drawCircle(centerX, centerY, radius/2, mCirclePaint1);
        canvas.drawCircle(centerX, centerY, radius*3/4, mCirclePaint2);
        canvas.drawCircle(centerX, centerY, radius, mCirclePaint1);

        canvas.drawLine(centerX-radius,centerY,centerX+radius,centerY,mDashPaint);
        canvas.drawLine(centerX,centerY-radius,centerX,centerY+radius,mDashPaint);

        int hitRadius = radius/12;
        for(int i=0;i<5;i++) {
            //canvas.drawCircle(mHitX[i],mHitY[i],40,mHitPaint);
            float hitX = mHitX[i];
            float hitY = mHitY[i];
            if(mbUseNormValue){
                hitX = mNormHitX[i]*mContentWidth;
                hitY = mNormHitY[i]*mContentHeight;
            }
            Rect src = new Rect(0,0,mbmpHits.get(i).getWidth(),mbmpHits.get(i).getHeight());
            Rect dst = new Rect((int)(hitX-hitRadius),(int)(hitY-hitRadius),(int)(hitX+hitRadius),(int)(hitY+hitRadius));
            canvas.drawBitmap(mbmpHits.get(i), src,dst,null);
        }
        for(int i=0;i<5;i++) {
            canvas.drawCircle(mHitX0[i],mHitY0[i],hitRadius,mCirclePaint2);
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