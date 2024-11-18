package com.example.myapplication.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ui.AppDatabase;
import com.example.myapplication.ui.User;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.ui.UserDao;

import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, @Nullable Bundle savedInstanceState) {

        AppDatabase database = AppDatabase.getDatabase(requireContext());

        UserDao userDao = database.userDao();


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView passwordStrengthIcon = binding.passwordStrengthIcon;
        final ProgressBar passwordStrengthBar = binding.passwordStrengthIndicator;
        final TextView passwordStrengthText = binding.passwordStrengthText;

        binding.passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                PasswordStrength(password, passwordStrengthBar, passwordStrengthText,passwordStrengthIcon);
            }

        });
        binding.addPasswordButton.setOnClickListener(v -> {
            String username = binding.usernameInput.getText().toString();
            String password = binding.passwordInput.getText().toString();
            String website = binding.websiteInput.getText().toString();
            String url = binding.urlInput.getText().toString();
            char firstLetter = Character.toUpperCase(website.charAt(0));

            new Thread(() -> {
                List<User> existingUsers = userDao.getUsersDirect();
                boolean isHeaderVisable = true;


                if (existingUsers != null) {
                    for (User existingUser : existingUsers) {
                        if (!existingUser.getWebsite().isEmpty() &&
                                Character.toUpperCase(existingUser.getWebsite().charAt(0)) == firstLetter) {
                            isHeaderVisable = false;
                            break;
                        }
                    }
                }
                long timestamp = System.currentTimeMillis();

                User user = new User(username, password, website,url, isHeaderVisable,timestamp);
                userDao.insert(user);

                binding.getRoot().post(() -> {
                    binding.usernameInput.getText().clear();
                    binding.passwordInput.getText().clear();
                    binding.websiteInput.getText().clear();
                    binding.urlInput.getText().clear();
                    Toast.makeText(getContext(), "User added successfully!", Toast.LENGTH_SHORT).show();
                });
            }).start();

        });
        return root;
    }
    private void PasswordStrength(String password, ProgressBar passwordStrengthBar, TextView passwordStrengthText, ImageView passwordStrengthIcon) {
        int strength = 0;
        int color;
        String strengthText;
        int icon;

        if (password.length() >= 8) {
            strength += 25;
        }
        if (password.matches(".*[A-Z].*")) {
            strength += 25;
        }
        if (password.matches(".*[0-9].*")){
            strength += 25;
        }
        if (password.matches(".*[!@#$%^&*+=?-].*")){
            strength += 25;
        }

        if (strength <= 50) {
            color = Color.RED;
            strengthText = "Strength: Weak";
            icon = R.drawable.close_24dp_5f6368_fill0_wght400_grad0_opsz24;
        } else if (strength <= 75) {
            color = Color.parseColor("#FFA500");
            strengthText = "Strength: Medium";
            icon = R.drawable.close_24dp_5f6368_fill0_wght400_grad0_opsz24;
        } else {
            color = Color.GREEN;
            strengthText = "Strength: Strong";
            icon = R.drawable.baseline_check_24;
        }


        passwordStrengthBar.setProgress(strength);
        passwordStrengthBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);

        passwordStrengthIcon.setImageResource(icon);
        passwordStrengthIcon.setColorFilter(color);
        passwordStrengthText.setText(strengthText);
        passwordStrengthText.setTextColor(color);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}