package com.example.lab14;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab14.cache.TemporaryStorage;
import com.example.lab14.data.CrypticVault;
import com.example.lab14.data.LocalSettingsManager;
import com.example.lab14.io.IOManager;
import com.example.lab14.io.MemberJsonHandler;
import com.example.lab14.model.AppMember;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String APP_TAG = "SecureVaultLab";
    private final List<String> availableLocales = Arrays.asList("fr", "en", "es", "de");

    private EditText inputAlias;
    private EditText inputToken;
    private Spinner localeSpinner;
    private Switch nightModeSwitch;
    private TextView logDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUi();
        setupInteraction();
        refreshInterfaceFromStorage();
    }

    private void initializeUi() {
        inputAlias = findViewById(R.id.input_user_alias);
        inputToken = findViewById(R.id.input_secure_token);
        localeSpinner = findViewById(R.id.selector_locale);
        nightModeSwitch = findViewById(R.id.toggle_night_mode);
        logDisplay = findViewById(R.id.display_logs);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_dropdown_item, availableLocales);
        localeSpinner.setAdapter(adapter);
    }

    private void setupInteraction() {
        findViewById(R.id.action_commit_prefs).setOnClickListener(v -> performSaveConfig());
        findViewById(R.id.action_retrieve_prefs).setOnClickListener(v -> refreshInterfaceFromStorage());
        findViewById(R.id.action_export_json).setOnClickListener(v -> performDataExport());
        findViewById(R.id.action_import_json).setOnClickListener(v -> performDataImport());
        findViewById(R.id.action_factory_reset).setOnClickListener(v -> performGlobalWipe());
    }

    private void performSaveConfig() {
        String alias = inputAlias.getText().toString().trim();
        String locale = availableLocales.get(Math.max(0, localeSpinner.getSelectedItemPosition()));
        String mode = nightModeSwitch.isChecked() ? "DARK_MODE" : "LIGHT_MODE";

        boolean success = LocalSettingsManager.commitData(this, alias, locale, mode, false);

        String secret = inputToken.getText().toString();
        if (!secret.isEmpty()) {
            try {
                CrypticVault.archiveSecret(this, secret);
            } catch (Exception e) {
                logDisplay.setText("Vault Error: " + e.getLocalizedMessage());
                return;
            }
        }

        try {
            TemporaryStorage.cacheContent(this, "session_snapshot.log", "Alias: " + alias + " | Mode: " + mode);
        } catch (Exception ignored) {}

        logDisplay.setText(String.format("Config synchronisée (%b)\nUser: %s\nLocale: %s\nMode: %s", 
                success, alias, locale, mode));
        
        Log.i(APP_TAG, "Settings archived successfully.");
    }

    private void refreshInterfaceFromStorage() {
        LocalSettingsManager.SettingData data = LocalSettingsManager.fetchSettings(this);

        inputAlias.setText(data.alias);
        nightModeSwitch.setChecked("DARK_MODE".equals(data.visualMode));

        int pos = availableLocales.indexOf(data.locale);
        localeSpinner.setSelection(pos >= 0 ? pos : 0);

        int secretSize = 0;
        try {
            String secret = CrypticVault.retrieveSecret(this);
            secretSize = (secret != null) ? secret.length() : 0;
        } catch (Exception ignored) {}

        logDisplay.setText("Données restaurées.\nAlias: " + data.alias + 
                "\nToken Length: " + secretSize);
        
        Log.i(APP_TAG, "Interface refreshed from local storage.");
    }

    private void performDataExport() {
        List<AppMember> members = Arrays.asList(
                new AppMember(101, "Jean Dupont", 5),
                new AppMember(102, "Marie Curie", 12),
                new AppMember(103, "Alan Turing", 8)
        );

        try {
            MemberJsonHandler.saveMembers(this, members);
            IOManager.persistText(this, "audit_log.txt", "Last export executed successfully.");
            logDisplay.setText("Export JSON réussi. " + members.size() + " membres enregistrés.");
        } catch (Exception e) {
            logDisplay.setText("Export Failure: " + e.getMessage());
        }
    }

    private void performDataImport() {
        List<AppMember> list = MemberJsonHandler.loadMembers(this);
        String audit;
        try {
            audit = IOManager.retrieveText(this, "audit_log.txt");
        } catch (Exception e) {
            audit = "No audit file found.";
        }

        StringBuilder output = new StringBuilder("Importation Terminée:\n");
        output.append("Log: ").append(audit).append("\nCount: ").append(list.size()).append("\n");
        for (AppMember m : list) {
            output.append("• ").append(m.fullName).append(" (ID: ").append(m.identifier).append(")\n");
        }

        logDisplay.setText(output.toString());
    }

    private void performGlobalWipe() {
        LocalSettingsManager.wipeSettings(this);
        try {
            CrypticVault.resetVault(this);
        } catch (Exception ignored) {}

        MemberJsonHandler.deleteData(this);
        IOManager.removeFile(this, "audit_log.txt");
        int deleted = TemporaryStorage.clearCacheFiles(this);

        inputAlias.setText("");
        inputToken.setText("");
        nightModeSwitch.setChecked(false);
        localeSpinner.setSelection(0);

        logDisplay.setText("Système réinitialisé.\nCache purgé: " + deleted + " fichiers.");
        Log.w(APP_TAG, "Factory reset performed by user.");
    }
}