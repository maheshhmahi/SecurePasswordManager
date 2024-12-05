package com.example.myapplication;



import android.accessibilityservice.AccessibilityService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAccessibilityService extends AccessibilityService {
    private PhishingModel phishingModel;
    private final Set<String> sentNotifications = new HashSet<>();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        try {
            phishingModel = new PhishingModel(this);
            Log.d("AccessibilityService", "PhishingModel initialized successfully.");
        } catch (Exception e) {
            Log.e("AccessibilityService", "Error initializing PhishingModel", e);
            phishingModel = null;
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {

            String packageName = event.getPackageName().toString();

            if ((packageName.equals("com.android.chrome") || packageName.equals("org.mozilla.firefox"))) {
                Log.d("Accessibility", "Browser detected: " + packageName);

                AccessibilityNodeInfo nodeInfo = event.getSource();

                if (nodeInfo == null) {
                    return;
                }

                CharSequence urlName = nodeInfo.getText();

                if (urlName != null) {
                    String urlText = urlName.toString();
                    if (urlText.contains(".")) {
                        Log.d("Accessibility", "Text contains a dot: " + urlText);
                        if(urlText.length()>20){
                              urlText = urlText.substring(0,20);
                        }
                        String prediction = predictURL(urlText);

                        if(sentNotifications.contains(urlText)) {
                                return;
                        }
                        sendNotification(urlText);
                        sendNotification(prediction);
                        sentNotifications.add(urlText);

                    }


                }
            }
        }
    }

    private String predictURL(String url) {
        try {
            // Preprocess the URL
            String cleanedUrl = url.replaceAll("[^A-Za-z\\s]", " ").toLowerCase().trim();
            String[] words = cleanedUrl.split("\\s+");
            int[] sequence = new int[phishingModel.getMaxLen()];

            // Convert words to indices
            Map<String, Integer> wordIndex = phishingModel.getWordIndex();
            for (int i = 0; i < words.length && i < sequence.length; i++) {
                sequence[i] = wordIndex.getOrDefault(words[i], 0);
            }

            // Make prediction
            float[][] input = new float[1][sequence.length];
            for (int i = 0; i < sequence.length; i++) {
                input[0][i] = sequence[i];
            }

            float[][] output = new float[1][1];
            phishingModel.getInterpreter().run(input, output);

            // Return result based on threshold
            return output[0][0] > 0.8 ? "bad" : "good";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during prediction";
        }
    }

    private void sendNotification(String url) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "global_notifications")
                .setSmallIcon(R.drawable.baseline_announcement_24)
                .setContentTitle("Detected URL")
                .setContentText("URL: " + url)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationChannel channel = new NotificationChannel(
                "global_notifications",
                "Global Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        notificationManager.createNotificationChannel(channel);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    public void onInterrupt() {

    }
}
