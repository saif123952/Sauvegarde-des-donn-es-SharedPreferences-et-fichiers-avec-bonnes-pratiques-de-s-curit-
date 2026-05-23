package com.example.lab14.io;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public final class ExternalDataMover {

    private ExternalDataMover() {}

    public static String exportContent(Context ctx, String fileName, String content) throws Exception {
        File folder = ctx.getExternalFilesDir(null);
        if (folder == null) return null;

        File targetFile = new File(folder, fileName);
        try (FileOutputStream out = new FileOutputStream(targetFile)) {
            out.write(content.getBytes(StandardCharsets.UTF_8));
        }
        return targetFile.getAbsolutePath();
    }

    public static String importContent(Context ctx, String fileName) throws Exception {
        File folder = ctx.getExternalFilesDir(null);
        if (folder == null) return null;

        File targetFile = new File(folder, fileName);
        if (!targetFile.exists()) return null;

        try (FileInputStream in = new FileInputStream(targetFile)) {
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        }
    }

    public static boolean deleteExternal(Context ctx, String fileName) {
        File folder = ctx.getExternalFilesDir(null);
        if (folder == null) return false;

        File targetFile = new File(folder, fileName);
        return targetFile.delete();
    }
}