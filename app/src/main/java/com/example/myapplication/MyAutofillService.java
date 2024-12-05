package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.assist.AssistStructure;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.CancellationSignal;
import android.service.autofill.*;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.ui.AppDatabase;
import com.example.myapplication.ui.User;
import com.example.myapplication.ui.UserDao;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class MyAutofillService extends AutofillService {

    @Override
    public void onFillRequest(FillRequest request, CancellationSignal cancellationSignal, FillCallback callback) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        UserDao userDao = db.userDao();
        List<AssistStructure.ViewNode> usernameFields = new ArrayList<>();
        List<AssistStructure.ViewNode> passwordFields = new ArrayList<>();



        AssistStructure assistStructure =  request.getFillContexts()
                .get(request.getFillContexts().size() - 1).getStructure();

        identifyEmailFields(assistStructure
                .getWindowNodeAt(0)
                .getRootViewNode(), usernameFields,passwordFields);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<User> users = userDao.getUsersDirect();


                FillResponse.Builder responseBuilder = new FillResponse.Builder();

                for (User user : users) {
                    String decryptedPassword = KeyStoreManager.decryptData(user.getPassword());

                    RemoteViews rvUsername = new RemoteViews(getPackageName(), R.layout.username);
                    rvUsername.setTextViewText(R.id.usernameList, user.getUsername());

                    RemoteViews rvPassword = new RemoteViews(getPackageName(), R.layout.password);
                    rvPassword.setTextViewText(R.id.passwordList, "************");
                    rvPassword.setTextViewText(R.id.websiteList, user.getWebsite());
                    rvPassword.setImageViewResource(R.id.websiteIcon,R.drawable.baseline_lock_outline_24);




                    if (usernameFields.isEmpty() || passwordFields.isEmpty()) {
                        callback.onSuccess(null);
                        return;
                    }
                    if (!usernameFields.isEmpty() && !passwordFields.isEmpty()) {

                        AutofillId autofillId = usernameFields.get(0).getAutofillId();
                        AutofillId autofillId2 = passwordFields.get(0).getAutofillId();

                        Dataset dataset = new Dataset.Builder(rvUsername)
                                .setValue(autofillId, AutofillValue.forText(user.getUsername()))
                                .build();
                        Dataset dataset1 = new Dataset.Builder(rvPassword)
                                .setValue(autofillId2, AutofillValue.forText(decryptedPassword))

                                .build();
                        responseBuilder.addDataset(dataset);
                        responseBuilder.addDataset(dataset1);
                    }
                }

                callback.onSuccess(responseBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFailure("Error querying database or decrypting data.");
            }
        });



    }


    @Override
    public void onSaveRequest(SaveRequest request, SaveCallback callback) {

    }




    void identifyEmailFields(AssistStructure.ViewNode node,
                             List<AssistStructure.ViewNode> emailFields,List<AssistStructure.ViewNode> passwordFields) {

        if (node == null) return;

        String[] autofillHints = node.getAutofillHints();
        if (autofillHints != null) {
            for (String hint : autofillHints) {
                Log.d("AutofillService", "Autofill hint: " + hint);
                if ("username".equalsIgnoreCase(hint) || "emailAddress".equalsIgnoreCase(hint) || "null".equalsIgnoreCase(hint)) {
                    emailFields.add(node);
                    return;
                }else if ("password".equalsIgnoreCase(hint) || ("passwordAuto".equalsIgnoreCase(hint))) {
                    passwordFields.add(node);
                    return;
                }
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            identifyEmailFields(node.getChildAt(i), emailFields,passwordFields);
        }
    }







}











