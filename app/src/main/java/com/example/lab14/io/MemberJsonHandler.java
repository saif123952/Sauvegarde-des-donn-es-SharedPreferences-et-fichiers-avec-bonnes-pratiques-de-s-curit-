package com.example.lab14.io;

import android.content.Context;
import com.example.lab14.model.AppMember;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public final class MemberJsonHandler {

    public static final String DATA_FILE = "members_list.json";

    private MemberJsonHandler() {}

    public static void saveMembers(Context ctx, List<AppMember> members) throws Exception {
        String jsonStr = convertToJson(members);
        try (FileOutputStream out = ctx.openFileOutput(DATA_FILE, Context.MODE_PRIVATE)) {
            out.write(jsonStr.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static List<AppMember> loadMembers(Context ctx) {
        try (FileInputStream in = ctx.openFileInput(DATA_FILE)) {
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, StandardCharsets.UTF_8);
            return parseFromJson(jsonStr);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static boolean deleteData(Context ctx) {
        return ctx.deleteFile(DATA_FILE);
    }

    private static String convertToJson(List<AppMember> list) throws Exception {
        JSONArray root = new JSONArray();
        for (AppMember m : list) {
            JSONObject obj = new JSONObject();
            obj.put("uid", m.identifier);
            obj.put("name_val", m.fullName);
            obj.put("exp_years", m.seniority);
            root.put(obj);
        }
        return root.toString();
    }

    private static List<AppMember> parseFromJson(String json) throws Exception {
        JSONArray root = new JSONArray(json);
        List<AppMember> results = new ArrayList<>();
        for (int i = 0; i < root.length(); i++) {
            JSONObject item = root.getJSONObject(i);
            results.add(new AppMember(
                item.getInt("uid"), 
                item.getString("name_val"), 
                item.getInt("exp_years")
            ));
        }
        return results;
    }
}