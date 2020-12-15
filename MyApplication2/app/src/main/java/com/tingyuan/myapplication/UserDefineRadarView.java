package com.tingyuan.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @description: 自定义多维度雷达图
 * @author: tingyuan
 * @date: 2020/12/15
 */
class UserDefineRadarView extends View {

    private double[] data = {2, 5, 1, 6, 4, 5};
    private float maxValue = 6;
    //中心X
    private float centerX;
    //中心Y
    private int centerY;
    //网格最大半径
    private float radius;
    //数字画笔
    private Paint valuePaint;
    private Paint radarPaint;
    private int count;

    private void init() {
        valuePaint = new Paint();
        valuePaint.setColor(Color.RED);

        radarPaint = new Paint();
        radarPaint.setStyle(Paint.Style.STROKE);
        radarPaint.setColor(Color.GREEN);
        count = data.length;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(w, h) / 2 * 0.9f;
        centerX =  w / 2;
        centerY =  h / 2;
        //一旦size发生改变，重新绘制
        postInvalidate();
    }

    public void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float angel = radius / count;
        float gap = radius / (count - 1);
        for (int i = 0; i < data.length ; i ++) {
            float curR = gap * i;
            path.reset();
            for (int j = 0 ; j< data.length; j++) {
                if (j == 0) {
                    path.moveTo(centerX + curR, centerY);
                } else {
                    float x = (float) (centerX + curR * Math.cos(angel * j));
                    float y = (float) (centerY + curR * Math.sin(angel * j));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path,radarPaint);
        }
    }

    public void drawLines(Canvas canvas) {
        Path path = new Path();
        float angle = radius / data.length;
        for (int i = 0; i < data.length; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            float x = (float) (centerX + radius * Math.cos(angle * i));
            float y = (float) (centerY + radius * Math.sin(angle * i));
            path.lineTo(x, y);
            canvas.drawPath(path, radarPaint);
        }
    }

    public void drawData(Canvas canvas) {
        Path path = new Path();
        float angle = radius / data.length;
        for (int i = 0; i < data.length; i++) {

            double percent = data[i] / maxValue;

            float x = (float) (centerX + radius * Math.cos(angle * i) * percent);
            float y = (float) (centerY + radius * Math.sin(angle * i) * percent);

            if (i == 0) {
                path.moveTo(x, centerY);
            } else {
                path.lineTo(x, y);
            }

            //绘制小圆点
            valuePaint.setAlpha(255);
            canvas.drawCircle(x, y, 10, valuePaint);
        }

        //绘制填充区域
        valuePaint.setAlpha(127);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }

    public UserDefineRadarView(Context context) {
        super(context);
        init();
    }

    public UserDefineRadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserDefineRadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
        drawLines(canvas);
        drawData(canvas);
    }
}
