package com.tingyuan.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
  * 简易雷达图，有待优化
 */
public class RadarView extends View {

    //数据个数
    private int mCount = 4;
    //网格最大半径
    private float mRadius;
    //中心X
    private float mCenterX;
    //中心Y
    private float mCenterY;
    //雷达区画笔
    private Paint mMainPaint;
    //文本画笔
    private Paint mTextPaint;
    //数字画笔
    private Paint mTextNumberPaint;
    //数据区画笔
    private Paint mValuePaint;
    //标题文字
    private List<String> mTitles;
    //各维度分值
    private List<Double> mData;
    //数据最大值
    private double mMaxValue = 100;
    //弧度
    private float mAngle;


    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //雷达区画笔初始化
        mMainPaint = new Paint();
        mMainPaint.setColor(Color.parseColor("#D9D9D9"));
        mMainPaint.setAntiAlias(true);
        mMainPaint.setStrokeWidth(1);
        mMainPaint.setStyle(Paint.Style.STROKE);
        //文本画笔初始化
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#666666"));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(28);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setAntiAlias(true);
        //数字画笔初始化
        mTextNumberPaint = new Paint();
        mTextNumberPaint.setColor(Color.parseColor("#F86123"));
        mTextNumberPaint.setTextAlign(Paint.Align.CENTER);
        mTextNumberPaint.setTextSize(42);
        mTextNumberPaint.setStrokeWidth(1);
        mTextNumberPaint.setAntiAlias(true);
        //数据区（分数）画笔初始化
        mValuePaint = new Paint();
        mValuePaint.setColor(Color.parseColor("#AAE402"));
        mValuePaint.setAntiAlias(true);
        mValuePaint.setStyle(Paint.Style.FILL);

        mTitles = new ArrayList<>();
        mTitles.add("性格");
        mTitles.add("气质");
        mTitles.add("外貌");
        mTitles.add("学历");
        mCount = mTitles.size();

        //默认分数
        mData = new ArrayList<>(mCount);
        mData.add(100.0);
        mData.add(80.0);
        mData.add(90.0);
        mData.add(70.0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRadius = (float) Math.min(w, h) / 2 * 0.5f;
        mCenterX = (float) w / 2;
        mCenterY = (float) h / 2;
        //一旦size发生改变，重新绘制
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygon(canvas);//绘制蜘蛛网
      //  drawLines(canvas);//绘制直线
        drawTitle(canvas);//绘制标题
        drawRegion(canvas);//绘制覆盖区域
    }

    /**
     * 绘制多边形
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        //1度=1*PI/180   360度=2*PI   那么我们每旋转一次的角度为2*PI/内角个数
        //中心与相邻两个内角相连的夹角角度
        mAngle = (float) (2 * Math.PI / mCount);
        //每个蛛丝之间的间距
        float r = mRadius / (mCount + 1);

        for (int i = 1; i <= mCount + 1; i++) {
            //当前半径
            float curR = r * i;
            path.reset();
            for (int j = 0; j < mCount; j++) {
                if (j == 0) {
                    float x = mCenterX;
                    float y = (float) (mCenterY - curR * Math.sin(mAngle));
                    path.moveTo(x, y);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x1 = (float) (mCenterX + curR * Math.sin(mAngle));
                    float y1 = mCenterY;
                    path.lineTo(x1, y1);
                    float x2 = mCenterX;
                    float y2 = (float) (mCenterY + curR * Math.sin(mAngle));
                    path.lineTo(x2, y2);
                    float x3 = (float) (mCenterX - curR * Math.sin(mAngle));
                    float y3 = mCenterY;
                    path.lineTo(x3, y3);
                    float x = mCenterX;
                    float y = (float) (mCenterY - curR * Math.sin(mAngle));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mMainPaint);
        }
    }


    /**
     * 绘制直线
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        path.reset();
        //直线1
        path.moveTo(mCenterX, mCenterY);
        float x1 = (float) (mCenterX + mRadius * Math.sin(mAngle));
        float y1 = (float) (mCenterY - mRadius * Math.cos(mAngle));
        path.lineTo(x1, y1);
        //直线2
        path.moveTo(mCenterX, mCenterY);
        float x2 = (float) (mCenterX + mRadius * Math.sin(mAngle / 2));
        float y2 = (float) (mCenterY + mRadius * Math.cos(mAngle / 2));
        path.lineTo(x2, y2);
        //直线3
        path.moveTo(mCenterX, mCenterY);
        float x3 = (float) (mCenterX - mRadius * Math.sin(mAngle / 2));
        float y3 = (float) (mCenterY + mRadius * Math.cos(mAngle / 2));
        path.lineTo(x3, y3);
        //直线4
        path.moveTo(mCenterX, mCenterY);
        float x4 = (float) (mCenterX - mRadius * Math.sin(mAngle));
        float y4 = (float) (mCenterY - mRadius * Math.cos(mAngle));
        path.lineTo(x4, y4);
        path.close();
        canvas.drawPath(path, mMainPaint);
    }

    /**
     * 绘制标题文字以及数字
     */
    private void drawTitle(Canvas canvas) {
        if (mCount != mTitles.size()) {
            return;
        }
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;//标题高度
        //绘制文字1
        float x1 = mCenterX;
        float y1 = mCenterY - mRadius;
        canvas.drawText(mTitles.get(0), x1,  y1 - 10, mTextPaint);
        canvas.drawText(mData.get(0).intValue() + "",x1, y1 - fontHeight - 5,mTextNumberPaint);
        //绘制文字2
        float x2 = (float) (mCenterX + mRadius * Math.sin(mAngle));
        float y2 = mCenterY;
        float dis = mTextPaint.measureText(mTitles.get(1));//标题一半的宽度
        canvas.drawText(mTitles.get(1), 50 + x2 , y2 + fontHeight + 10, mTextPaint);
        canvas.drawText(mData.get(1).intValue() + "",50 + x2, y2 + fontHeight / 2 ,mTextNumberPaint);
        //绘制文字3
        float x3 = mCenterX;
        float y3 = (float) (mCenterY + mRadius * Math.sin(mAngle));
        canvas.drawText(mTitles.get(2), x3, y3 + fontHeight + 35, mTextPaint);
        canvas.drawText(mData.get(2).intValue() + "",x3, y3 + 40,mTextNumberPaint);
        //绘制文字4
        float x4 = (float) (mCenterX - mRadius * Math.sin(mAngle));
        float y4 = mCenterY;
        canvas.drawText(mTitles.get(3), x4 - 50, y4 + fontHeight + 10, mTextPaint);
        canvas.drawText(mData.get(3).intValue() + "", x4 - 50,  y4 + fontHeight / 2,mTextNumberPaint);
    }

    /**
     * 绘制覆盖区域
     */
    private void drawRegion(Canvas canvas) {
        mValuePaint.setAlpha(255);
        Path path = new Path();
        double dataValue;
        double percent;
        //绘制圆点1
        dataValue = mData.get(0);
        if (dataValue != mMaxValue) {
            percent = dataValue / mMaxValue;
        } else {
            percent = 1;
        }
        float x1 = mCenterX;
        float y1 = (float) (mCenterY - mRadius * percent);
        path.moveTo(x1, y1);

        //绘制圆点2
        dataValue = mData.get(1);
        if (dataValue != mMaxValue) {
            percent = dataValue / mMaxValue;
        } else {
            percent = 1;
        }
        float x2 = (float) (mCenterX + mRadius * percent * Math.sin(mAngle));
        float y2 = (float) (mCenterY - mRadius * percent * Math.cos(mAngle));
        path.lineTo(x2, y2);

        //绘制圆点3
        dataValue = mData.get(2);
        if (dataValue != mMaxValue) {
            percent = dataValue / mMaxValue;
        } else {
            percent = 1;
        }
        float x3 = (float) (mCenterX + mRadius * percent * Math.cos(mAngle));
        float y3 = (float) (mCenterY + mRadius * percent * Math.sin(mAngle));
        path.lineTo(x3, y3);

        //绘制圆点4
        dataValue = mData.get(3);
        if (dataValue != mMaxValue) {
            percent = dataValue / mMaxValue;
        } else {
            percent = 1;
        }
        float x4 = (float) (mCenterX - mRadius * percent * Math.sin(mAngle));
        float y4 = (float) (mCenterY + mRadius * percent * Math.cos(mAngle));
        path.lineTo(x4, y4);

        path.close();
        mValuePaint.setStyle(Paint.Style.STROKE);
        //绘制覆盖区域外的连线
        canvas.drawPath(path, mValuePaint);
        //填充覆盖区域
        mValuePaint.setAlpha(128);
        mValuePaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, mValuePaint);
    }


    //设置蜘蛛网颜色
    public void setMainPaint(Paint mainPaint) {
        this.mMainPaint = mainPaint;
        postInvalidate();
    }

    //设置标题颜色
    public void setmTextPaint(Paint mTextPaint) {
        this.mTextPaint = mTextPaint;
    }

    //设置覆盖局域颜色
    public void setmValuePaint(Paint mValuePaint) {
        this.mValuePaint = mValuePaint;
        postInvalidate();
    }

    //设置各门得分
    public void setmData(List<Double> mData) {
        this.mData = mData;
        postInvalidate();
    }

    //设置满分分数，默认是100分满分
    public void setmMaxValue(float mMaxValue) {
        this.mMaxValue = mMaxValue;
    }
}