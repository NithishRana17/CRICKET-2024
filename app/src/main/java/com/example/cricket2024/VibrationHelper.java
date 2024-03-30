package com.example.cricket2024;

import android.content.Context;
import android.os.Vibrator;

public class VibrationHelper {

    // Vibrate the device for a specified duration
    public static void vibrate(Context context, long durationMillis) {
        // Get the Vibrator service
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            // Check if the device supports vibration
            if (vibrator.hasVibrator()) {
                // Vibrate the device for the specified duration
                vibrator.vibrate(durationMillis);
            }
        }
    }
}
