package com.android.falldetector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by duanp on 3/20/2017.
 */

public class GradientAlgorithm implements FallDetectionAlgorithm, SensorEventListener {

    private MainActivity mActivity;
    private SensorManager mSensorManager;
    private boolean mIsFall = false;
    private static final int WINDOW_SIZE = 10;
    private float[] mLinearAccelData;
    private float[] mGravityData;

    public GradientAlgorithm(MainActivity activity) {
        mActivity = activity;
        mSensorManager = (SensorManager)activity.getSystemService(SENSOR_SERVICE);
        Sensor linearAcceleration =
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorManager.registerListener(this, linearAcceleration,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
        initialize();
    }

    @Override
    public void initialize() {
        mIsFall = false;
        mLinearAccelData = new float[WINDOW_SIZE];
        mGravityData = new float[WINDOW_SIZE];
    }

    @Override
    public void update(float ax, float ay, float az) {

    }

    @Override
    public boolean isFall() {
        return false;
    }

    @Override
    public void clearFallFlag() {

    }

    @Override
    public void unregisterSensorListener() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
