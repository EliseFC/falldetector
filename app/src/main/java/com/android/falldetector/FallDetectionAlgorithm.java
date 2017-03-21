package com.android.falldetector;

/**
 * Fall Detection Algorithm interface.
 * Created by duanp on 3/19/2017.
 */

public interface FallDetectionAlgorithm {
    void initialize(); // initialize the internal data
    void update(float ax, float ay, float az); // update the internal data (state)
    boolean isFall(); // get the result based on the current state (internal data)
    void clearFallFlag(); // should be called after calling isFall()
    void unregisterSensorListener();
}
