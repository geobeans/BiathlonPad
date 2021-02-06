package cn.geobeans.biathlon.target;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import cn.geobeans.biathlon.R;

/**
 * TODO: document your custom view class.
 */
public class SimpleTargetView extends View {
    private Paint mCirclePaint; //画圆画笔
    private Paint mCirclePaint1; //画圆画笔

    float[] mHitX = new float[8];
    float[] mHitY = new float[8];

    public SimpleTargetView(Context context) {
        super(context);
        init(null, 0);
    }

    public SimpleTargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SimpleTargetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SimpleTargetView, defStyle, 0);

        a.recycle();

        for(int i=0;i<8;i++){
            mHitX[i] = 0.0f;
            mHitY[i] = 0.0f;
        }

        //画圆画笔初始化
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setARGB(255,0,0,0);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(3.0f);

        mCirclePaint1 = new Paint();
        mCirclePaint1.setAntiAlias(true);
        mCirclePaint1.setARGB(255,255,0,0);
        mCirclePaint1.setStyle(Paint.Style.FILL_AND_STROKE);


        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    public void setHitX(float[] x)
    {
        System.arraycopy(mHitX,0,x,0,8);
    }

    public void setHitY(float[] y)
    {
        System.arraycopy(mHitY,0,y,0,8);
    }

    private void invalidateTextPaintAndMeasurements() {
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

        int centerX = contentWidth/2;
        int centerY = contentHeight/2;
        int radius = contentHeight>contentWidth?contentWidth:contentHeight;
        radius = radius*4/10;
        canvas.drawCircle(centerX, centerY, radius, mCirclePaint);

        int hitRadius = radius/12;
        for(int i=0;i<5;i++) {
            canvas.drawCircle(mHitX[i]*contentWidth,mHitY[i]*contentHeight,hitRadius,mCirclePaint1);
        }

    }

 }