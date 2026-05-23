package com.example.lab14.cache;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public final class TemporaryStorage {

    private TemporaryStorage() {}

    public static void cacheContent(Context ctx, String label, String data) throws Exception {
        File cacheFile = new File(ctx.getCacheDir(), label);
        try (FileOutputStream out = new FileOutputStream(cacheFile)) {
            out.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static String getCachedContent(Context ctx, String label) throws Exception {
        File cacheFile = new File(ctx.getCacheDir(), label);
        if (!cacheFile.exists()) return null;
        
        try (FileInputStream in = new FileInputStream(cacheFile)) {
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        }
    }

    public static int clearCacheFiles(Context ctx) {
        File[] cachedItems = ctx.getCacheDir().listFiles();
        if (cachedItems == null) return 0;
        int count = 0;
        for (File f : cachedItems) {
            if (f.delete()) count++;
        }
        return count;
    }
}