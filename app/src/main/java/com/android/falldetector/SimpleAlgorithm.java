package com.android.falldetector;

/**
 * Simple fall detection algorithm that computes a = ax^2 + ay^2 + az^2,
 * and reports a fall when a < threshold for a certain number of times continuously.
 * Refer to the document of FallDetectionAlgorithm for its usage.
 *
 * Created by duanp on 3/19/2017.
 */

class SimpleAlgorithm implements FallDetectionAlgorithm {
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

    public SimpleAlgorithm() {
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
}
