package com.duyhoang.readwriteexternalstoragewithruntimepermission;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by rogerh on 5/5/2018.
 */

public class UserPSettingChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    Context mContext;

    public UserPSettingChangeListener(Context context){mContext = context;}


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Toast.makeText(mContext, s + " Value changed: " + sharedPreferences.getString(s, "Unkown"), Toast.LENGTH_SHORT).show();
    }
}
