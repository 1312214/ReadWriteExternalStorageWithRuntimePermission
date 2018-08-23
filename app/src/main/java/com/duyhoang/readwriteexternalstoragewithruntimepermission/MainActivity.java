package com.duyhoang.readwriteexternalstoragewithruntimepermission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static String MY_FILE = "my_data.txt";

    EditText etTextInput;
    Button btnWriteFile, btnReadFile;

    enum ReadWriteMode{READ, WRITE};
    ReadWriteMode recentOpRun;

    UserPSettingChangeListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTextInput = (EditText)findViewById(R.id.edit_input);
        btnReadFile = (Button)findViewById(R.id.button_read_from_file);
        btnWriteFile = (Button)findViewById(R.id.button_write_to_file);

        btnWriteFile.setOnClickListener(this);
        btnReadFile.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_read_from_file: readContentFromExternalStorage();
                break;
            case R.id.button_write_to_file: writeContentToExternalStorage();
                break;
            default: break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        readContentFromExternalStorage();
    }

    /**
     * kiem tra neu đa co permission chua, neu chua thi request permission
     *  kiem tra da external storage state are ready, ngc lai thong bao chua ready
     *      neu ready thi bat dau ghi file
     */

    private void readContentFromExternalStorage() {
        recentOpRun = ReadWriteMode.READ;
        if(arePermssionsGranted(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})){
            // write readfile from external storage FUNCTION, then set the text  to edit input
            try {
                etTextInput.setText(readFileExternalStorage(MY_FILE));

            } catch (FileNotFoundException e){
                Toast.makeText(this, "<<FileNotFoundException>>" + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "<<**IOException>>" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            String mReason = " Read content from external storage";
            Snackbar.make(findViewById(android.R.id.content), R.string.read_permission, Snackbar.LENGTH_SHORT)
                    .show();
            requestRuntimePermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_EXTERNAL_STORAGE_PERMISSION_CODE, mReason );
        }


    }

    private void writeContentToExternalStorage() {
        recentOpRun = ReadWriteMode.WRITE;
        if(arePermssionsGranted(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})){
            // write writefile from external storage FUNCTION, then set the text  to edit input
            String content = etTextInput.getText().toString();
            if(isStringEmpty(content)){
                try {
                    writeFileToExternalStorage(MY_FILE, content);
                    etTextInput.setText("");
                } catch (IOException e) {
                    Toast.makeText(this, "<<IOException>>" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }
        else{
            String mReason = " Write content from external storage";
            Snackbar.make(findViewById(android.R.id.content), "App needs: Write permission", Snackbar.LENGTH_SHORT)
                    .show();
            requestRuntimePermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_EXTERNAL_STORAGE_PERMISSION_CODE, mReason );
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_EXTERNAL_STORAGE_PERMISSION_CODE){
            if(permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                switch (recentOpRun){
                    case READ: readContentFromExternalStorage(); break;
                    case WRITE: writeContentToExternalStorage(); break;
                    default: break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mListener = new UserPSettingChangeListener(this);
        switch (item.getItemId()){
            case R.id.item_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(mListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
