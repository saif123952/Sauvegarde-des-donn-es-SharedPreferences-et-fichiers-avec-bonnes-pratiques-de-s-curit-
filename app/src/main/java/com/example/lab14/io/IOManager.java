package com.example.lab14.io;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class IOManager {

    private IOManager() {}

    public static void persistText(Context ctx, String fileName, String data) throws Exception {
        try (FileOutputStream out = ctx.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            out.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static String retrieveText(Context ctx, String fileName) throws Exception {
        try (FileInputStream in = ctx.openFileInput(fileName)) {
            return readStream(in);
        }
    }

    public static boolean removeFile(Context ctx, String fileName) {
        return ctx.deleteFile(fileName);
    }

    private static String readStream(InputStream is) throws Exception {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }
}