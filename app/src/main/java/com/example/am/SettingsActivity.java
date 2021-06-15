package com.example.am;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.switchmaterial.SwitchMaterial;

import helpers.LocaleManager;
import helpers.SharedPrefsHandler;

public class SettingsActivity extends AppCompatActivity {
    private SwitchMaterial switch1;
    private RadioGroup radioGroup;
    private boolean ifChangedManually = false;
    private RadioButton button1;
    private RadioButton button2;
    private boolean ifSwitchchangedManually;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefsHandler.loadTheme(SettingsActivity.this);
        setContentView(R.layout.activity_settings);
        button1 = findViewById(R.id.radio_button_1);
        button2 = findViewById(R.id.radio_button_2);
        switch1 = (SwitchMaterial) findViewById(R.id.switch1);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ifChangedManually = false;
        ifSwitchchangedManually = false;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(ifChangedManually){
                    ifChangedManually = false;
                    return;
                }
                if(i == R.id.radio_button_1)
                    SharedPrefsHandler.saveTheme(SettingsActivity.this,"light");
                else if (i == R.id.radio_button_2){
                    SharedPrefsHandler.saveTheme(SettingsActivity.this,"dark");
                }
                recreate();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(ifSwitchchangedManually){
                    ifSwitchchangedManually = false;
                    return;
                }
                if (!b) {
                    Log.println(Log.INFO,"info", String.valueOf(b));
                    LocaleManager.setLocale(SettingsActivity.this, "en");
                    SharedPrefsHandler.saveLocale(SettingsActivity.this,"en");
                } else{
                    Log.println(Log.INFO,"info", String.valueOf(b));
                    LocaleManager.setLocale(SettingsActivity.this, "pl");
                    SharedPrefsHandler.saveLocale(SettingsActivity.this,"pl");
                }
                recreate();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
        if (SharedPrefsHandler.ifEnLocale(SettingsActivity.this)) {
            if(switch1.isChecked()){
                ifSwitchchangedManually = true;
                switch1.setChecked(false);
            }
        }else{
            if(!switch1.isChecked()){
                ifSwitchchangedManually = true;
                switch1.setChecked(true);
            }
        }
        if (SharedPrefsHandler.ifLightTheme(SettingsActivity.this)){
            Log.println(Log.INFO,"info", "light theme " );
            if(!button1.isChecked()){
                ifChangedManually = true;
                button1.setChecked(true);

            }
        }else {
            Log.println(Log.INFO,"info", "not light theme " );
            if(!button2.isChecked()){
                ifChangedManually = true;
                button2.setChecked(true);
            }
        }

    }

}