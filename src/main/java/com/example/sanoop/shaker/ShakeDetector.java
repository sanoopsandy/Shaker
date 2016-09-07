package com.example.sanoop.shaker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.Toast;

/**
 * Created by sanoop on 9/7/2016.
 */
public class ShakeDetector implements SensorEventListener {
    /** Accuracy configuration */
    private static float threshold  = 15.0f;
    private static int interval     = 1000;


    private long now = 0;
    private long timeDiff = 0;
    private long lastUpdate = 0;
    private long lastShake = 0;

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;
    private float force = 0;

    private AccelerometerListener listener;

    public void setOnShakeListener(AccelerometerListener listener) {
        this.listener= listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        now = event.timestamp;

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        if (lastUpdate == 0) {
            lastUpdate = now;
            lastShake = now;
            lastX = x;
            lastY = y;
            lastZ = z;

        } else {
            timeDiff = now - lastUpdate;

            if (timeDiff > 0) {
                float xMovement = Math.abs(x - lastX);
                float yMovement = Math.abs(y - lastY);
                float zMovement = Math.abs(z - lastZ);

                if ((xMovement > threshold) || (yMovement > threshold) || (zMovement > threshold)) {
                    if (now - lastShake >= interval) {
                        listener.onShake(force);
                    }
                    lastShake = now;
                }
                lastX = x;
                lastY = y;
                lastZ = z;
                lastUpdate = now;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
