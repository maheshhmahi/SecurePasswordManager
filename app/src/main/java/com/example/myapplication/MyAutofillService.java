package com.example.myapplication;

import android.os.Build;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.FillCallback;
import android.service.autofill.FillRequest;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveRequest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyAutofillService extends AutofillService {


    @Override
    public void onSaveRequest(SaveRequest request, SaveCallback callback) {

    }

    @Override
    public void onConnected() {
        super.onConnected();
    }

    @Override
    public void onFillRequest(@NonNull FillRequest request, @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback callback) {

    }

    @Override
    public void onDisconnected() {

        super.onDisconnected();
    }
}
