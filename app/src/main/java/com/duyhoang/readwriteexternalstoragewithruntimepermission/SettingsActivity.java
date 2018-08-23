package com.duyhoang.readwriteexternalstoragewithruntimepermission;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

/**
 * Created by rogerh on 5/5/2018.
 */

public class SettingsActivity extends PreferenceActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_pref_settings);

    }
}
