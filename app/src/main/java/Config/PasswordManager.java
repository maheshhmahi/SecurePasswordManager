package Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class PasswordManager {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "password_manager_prefs";

    public PasswordManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void storePassword(String appName, String password) throws Exception {
        KeyStoreManager.generateKey();
        byte[] encryptedPassword = KeyStoreManager.encryptData(password);
        String encryptedPasswordBase64 = Base64.encodeToString(encryptedPassword, Base64.DEFAULT);

        sharedPreferences.edit().putString(appName, encryptedPasswordBase64).apply();
    }

    public String getPassword(String appName) throws Exception {
        String encryptedPasswordBase64 = sharedPreferences.getString(appName, null);
        if (encryptedPasswordBase64 != null) {
            byte[] encryptedPassword = Base64.decode(encryptedPasswordBase64, Base64.DEFAULT);
            return KeyStoreManager.decryptData(encryptedPassword);
        }
        return null;
    }
}

