package com.tingyuan.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 自定义圆柱体
 */
class PercentCylinderViewBar : View {

    //画柱状图的画笔
    private lateinit var mBarPaint: Paint

    //设置最大值，用于计算比例
    private var mMax = 0f

    //设置柱状图的目标值，除以max即为比例
    private var mTargetValue: Float = 0F

    private var mOriginalLeftValue = 0F

    //设置柱状图的当前比例
    private var mCurrentBarProgress = 0f

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBarPaint.color = Color.parseColor("#42F4E0")
        mBarPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mTargetValue == 0F) {
            return
        }
        canvas.drawOval(mOriginalLeftValue,
            0F,mOriginalLeftValue + 30F,measuredHeight.toFloat(),mBarPaint)

        if (mCurrentBarProgress < (mTargetValue - mOriginalLeftValue) / mMax * measuredWidth) {
            mCurrentBarProgress += 10f
            postInvalidateDelayed(10)
        } else {
            canvas.drawOval(mCurrentBarProgress - 30,
                0F,mCurrentBarProgress,measuredHeight.toFloat(),mBarPaint)
        }
        canvas.drawRect(
            mOriginalLeftValue + 15F,
            0F,
            mCurrentBarProgress - 15,
            measuredHeight.toFloat(),
            mBarPaint
        )
    }


    /**
     * 设置最大值
     * @param max
     */
    fun setMax(max: Float) {
        this.mMax = max
    }

    /**
     * 设置目标值
     * @param respTarget
     */
    fun setTargetNum(respTarget: Float) {
        this.mTargetValue = respTarget
        invalidate()
    }
}