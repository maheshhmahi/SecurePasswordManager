package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PhishingModel {
    private final Interpreter interpreter;
    private final Map<String, Integer> wordIndex;
    private final int maxLen;

    public PhishingModel(Context context) throws Exception {
        // Load the TFLite model
        interpreter = new Interpreter(loadModelFile(context));

        // Load metadata
        wordIndex = new HashMap<>();
        int tempMaxLen;
        try (InputStream metadataStream = context.getAssets().open("phishing_model_metadata.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(metadataStream))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            // Parse metadata
            JSONObject metadata = new JSONObject(jsonBuilder.toString());
            JSONObject wordIndexJson = metadata.getJSONObject("word_index");
            Iterator<String> keys = wordIndexJson.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                wordIndex.put(key, wordIndexJson.getInt(key));
            }
            tempMaxLen = metadata.getInt("max_len");
        }
        maxLen = tempMaxLen;
    }

    private MappedByteBuffer loadModelFile(Context context) throws Exception {
        try (AssetFileDescriptor fileDescriptor = context.getAssets().openFd("phishing_model.tflite");
             FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }

    @SuppressWarnings("unused")
    public Interpreter getInterpreter() {
        return interpreter;
    }

    @SuppressWarnings("unused")
    public Map<String, Integer> getWordIndex() {
        return wordIndex;
    }

    @SuppressWarnings("unused")
    public int getMaxLen() {
        return maxLen;
    }
}
