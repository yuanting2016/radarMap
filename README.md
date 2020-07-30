# radarMap
 a user-defined radar map
 xml:
    <com.tingyuan.myapplication.RadarView
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:layout_width="300dp"
        android:layout_height="300dp"/>
 java:
     //设置各门得分
    public void setmData(List<Double> mData) {
        this.mData = mData;
        postInvalidate();
    }
        
