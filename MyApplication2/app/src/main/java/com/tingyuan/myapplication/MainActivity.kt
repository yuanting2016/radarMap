package com.tingyuan.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBarGraph: PercentCylinderViewBar
    private lateinit var respectTarget: ArrayList<Float>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBarGraph = findViewById<View>(R.id.bargraph) as PercentCylinderViewBar

        mBarGraph.setMax(60F)

        btn.setOnClickListener {
            mBarGraph.setTargetNum(40F)
        }
        btn1.setOnClickListener {
            mBarGraph.setTargetNum(60F)
        }
    }
}
