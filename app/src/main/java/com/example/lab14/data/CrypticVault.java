package com.example.lab14.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public final class CrypticVault {

    private static final String STORAGE_LABEL = "encrypted_vault";
    private static final String FIELD_SECRET_KEY = "vault_token";

    private CrypticVault() {}

    private static SharedPreferences getSecureStore(Context ctx) throws Exception {
        MasterKey rootKey = new MasterKey.Builder(ctx)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                ctx,
                STORAGE_LABEL,
                rootKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void archiveSecret(Context ctx, String secret) throws Exception {
        getSecureStore(ctx).edit().putString(FIELD_SECRET_KEY, secret).apply();
    }

    public static String retrieveSecret(Context ctx) throws Exception {
        return getSecureStore(ctx).getString(FIELD_SECRET_KEY, "");
    }

    public static void resetVault(Context ctx) throws Exception {
        getSecureStore(ctx).edit().clear().apply();
    }
}