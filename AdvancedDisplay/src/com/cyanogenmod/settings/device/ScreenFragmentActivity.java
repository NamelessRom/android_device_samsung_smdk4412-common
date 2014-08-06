/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;

import com.cyanogenmod.settings.device.R;

public class ScreenFragmentActivity
        extends PreferenceFragment
        implements OnPreferenceChangeListener {

    private static final String PREF_ENABLED = "1";
    private static final String TAG = "DisplaySettings_Screen";
    private static final String KEY_TOUCHWAKE_ENABLE = "touchwake_enable";
    private static final String KEY_TOUCHWAKE_TIMEOUT = "touchwake_timeout";
    private static final String FILE_TOUCHWAKE_ENABLE = "/sys/devices/virtual/misc/touchwake/enabled";
    private static final String FILE_TOUCHWAKE_TIMEOUT = "/sys/devices/virtual/misc/touchwake/delay";

    private mDNIeScenario mmDNIeScenario;
    private mDNIeMode mmDNIeMode;
    private mDNIeNegative mmDNIeNegative;
    private SwitchPreference mTouchwakeEnable;
    private SeekBarPreference mTouchwakeTimeout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screen_preferences);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Resources res = getResources();

        /* mDNIe */
        mmDNIeScenario = (mDNIeScenario) findPreference(DisplaySettings.KEY_MDNIE_SCENARIO);
        mmDNIeScenario.setEnabled(mDNIeScenario.isSupported(res.getString(R.string.mdnie_scenario_sysfs_file)));

        mmDNIeMode = (mDNIeMode) findPreference(DisplaySettings.KEY_MDNIE_MODE);
        mmDNIeMode.setEnabled(mDNIeMode.isSupported(res.getString(R.string.mdnie_mode_sysfs_file)));

        mmDNIeNegative = (mDNIeNegative) findPreference(DisplaySettings.KEY_MDNIE_NEGATIVE);
        mmDNIeNegative.setEnabled(mDNIeNegative.isSupported(res.getString(R.string.mdnie_negative_sysfs_file)));

        /* Touchwake */
        mTouchwakeEnable = (SwitchPreference) findPreference(KEY_TOUCHWAKE_ENABLE);
        mTouchwakeTimeout = (SeekBarPreference) findPreference(KEY_TOUCHWAKE_TIMEOUT);

        if (!isSupported(FILE_TOUCHWAKE_ENABLE)) {
            mTouchwakeEnable.setEnabled(false);
            mTouchwakeEnable.setSummary(R.string.kernel_does_not_support);

            mTouchwakeTimeout.setEnabled(false);
            mTouchwakeTimeout.setSummary(R.string.kernel_does_not_support);
         } else {
            int b = Integer.parseInt(Utils.readOneLine(FILE_TOUCHWAKE_ENABLE));
            mTouchwakeEnable.setChecked(b != 0);
            mTouchwakeEnable.setOnPreferenceChangeListener(this);

            int i = Integer.parseInt(Utils.readOneLine(FILE_TOUCHWAKE_TIMEOUT));
            mTouchwakeTimeout.setValue(i / 1000);
            mTouchwakeTimeout.setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String key = preference.getKey();
        Log.w(TAG, "key: " + key);

        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mTouchwakeEnable) {
            boolean b = (Boolean) newValue;
            mTouchwakeEnable.setChecked(b);
            Utils.writeValue(FILE_TOUCHWAKE_ENABLE, b);
        } else if (preference == mTouchwakeTimeout) {
            int i = (Integer) newValue;
            mTouchwakeTimeout.setValue(i);
            String s = Integer.toString (i * 1000);
            Utils.writeValue(FILE_TOUCHWAKE_TIMEOUT, s);
        }
        return true;
    }

    public static boolean isSupported(String FILE) {
        return Utils.fileExists(FILE);
    }

    public static void restore(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (isSupported(FILE_TOUCHWAKE_ENABLE)) {
            boolean b = sharedPrefs.getBoolean(KEY_TOUCHWAKE_ENABLE, false);
            Utils.writeValue(FILE_TOUCHWAKE_ENABLE, b);

            int i = sharedPrefs.getInt(KEY_TOUCHWAKE_TIMEOUT, 10) * 1000;
            Utils.writeValue(FILE_TOUCHWAKE_TIMEOUT, Integer.toString(i));
        }
    }
}
