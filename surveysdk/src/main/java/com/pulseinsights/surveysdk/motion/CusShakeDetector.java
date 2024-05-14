package com.pulseinsights.surveysdk.motion;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CusShakeDetector implements SensorEventListener {


    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private static int triggerCount = -1;

    private OnShakeListener onShakeListener;
    private long shakeTimestamp;
    private static int mShakeCount;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    public boolean start(SensorManager sensorManager) {
        // Already started?
        if (accelerometer != null) {
            return true;
        }

        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);

        // If this phone has an accelerometer, listen to it.
        if (accelerometer != null) {
            this.sensorManager = sensorManager;
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
        return accelerometer != null;
    }

    public void setTriggerThreshold(int count) {
        triggerCount = count;
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.onShakeListener = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (onShakeListener != null) {
            float sensorX = event.values[0];
            float sensorY = event.values[1];
            float sensorZ = event.values[2];

            float graSensorX = sensorX / SensorManager.GRAVITY_EARTH;
            float graSensorY = sensorY / SensorManager.GRAVITY_EARTH;
            float graSensorZ = sensorZ / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float graForce = (float) Math.sqrt(graSensorX * graSensorX
                    + graSensorY * graSensorY + graSensorZ * graSensorZ);

            if (graForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                shakeTimestamp = now;
                mShakeCount++;

                if (triggerCount > -1) {
                    if (mShakeCount > triggerCount) {
                        onShakeListener.onShake(mShakeCount);
                        mShakeCount = 0;
                    }
                } else {
                    onShakeListener.onShake(mShakeCount);
                }
            }
        }
    }
}