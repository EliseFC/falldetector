package com.android.falldetector;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Simple fall detection algorithm that computes a = ax^2 + ay^2 + az^2,
 * and reports a fall when a < threshold for a certain number of times continuously.
 * Refer to the document of FallDetectionAlgorithm for its usage.
 *
 * Created by duanp on 3/19/2017.
 */

class SimpleAlgorithm implements FallDetectionAlgorithm, SensorEventListener{

    private MainActivity mActivity;
    private SensorManager mSensorManager;

    private static final int MAX_RECORDS = 200;
    private static final int NUM_FALL_THRESHOLD = 5;
    private static final double FALL_MAG_THRESHOLD = 35;
    private static final int REST_THRESHOLD = 20;

    private int mCurrRecIndex = 0; // index for mAccelData[]
    private int mAccelCount = 0; // how many times mCurrData < FALL_MAG_THRESHOLD continuously
    private int mIdleCount = 0; // if mCurrData >= FALL_MAG_THRESHOLD, it is in Idle state, count it
    private boolean mIsInCycle = false;
    private float[] mAccelData; // history data for mCurrData
    private float mCurrData = 0.0f; // square sum of accelerations in x, y, and z directions
    private boolean mIsFall = false;

    SimpleAlgorithm(MainActivity activity) {
        mActivity = activity;
        mSensorManager = (SensorManager)activity.getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        initialize();
    }

    @Override
    public void initialize() {
        mCurrRecIndex = 0;
        mAccelCount = 0;
        mIsInCycle = false;
        mIdleCount = 0;
        mCurrData = 0.0f;
        mIsFall = false;
        mAccelData = new float[MAX_RECORDS];
    }

    @Override
    public void update(float ax, float ay, float az) {
        // 1) get new accelerometer reading
        mCurrData = ax * ax + ay * ay + az * az;

        // 2) record accelerometer difference, then increment mCurrRecIndex
        if (mCurrRecIndex != 0) { // if not the very first record
            // 3) update mAccelCount
            boolean newRecordTap = mCurrData < FALL_MAG_THRESHOLD;
            boolean oldRecordTap = mAccelData[mCurrRecIndex] < FALL_MAG_THRESHOLD;
            if (newRecordTap) {
                if (!oldRecordTap || !mIsInCycle) {
                    mAccelCount++;
                }
                mIdleCount = 0;
            } else {
                if (oldRecordTap && mIsInCycle) {
                    mAccelCount--;
                }
                mIdleCount++;
                if (mIdleCount >= REST_THRESHOLD)
                    mAccelCount = Math.max(0, mAccelCount - 2);
            }
        }
        mAccelData[mCurrRecIndex] = mCurrData;
        mCurrRecIndex = (mCurrRecIndex + 1) % MAX_RECORDS;

        // 4) check if mAccelCount threshold is met, if so switch activity
        if (mAccelCount >= NUM_FALL_THRESHOLD) {
            mIsFall = true;
        }
    }

    @Override
    public boolean isFall() {
        return mIsFall;
    }

    @Override
    public void clearFallFlag() {
        mIsFall = false;
    }

    float getLatestData() {
        return mCurrData;
    }

    @Override
    public void unregisterSensorListener() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("SimpleAlgorithm", "==============> onSensorChanged");
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];

        this.update(ax, ay, az);

        float[] args = new float[] {ax, ay, az};
        //---- display sensor data in Wave fragment
        mActivity.updateWaveFragment(args);

        //---- save sensor data in file
        mActivity.saveDataToFile(args);

        if (this.isFall()) {
            // Need to check if the "are you okay is already called"
            mActivity.doSomethingWhenFall();
            this.clearFallFlag();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
