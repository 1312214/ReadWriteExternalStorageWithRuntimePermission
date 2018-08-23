package com.duyhoang.readwriteexternalstoragewithruntimepermission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by rogerh on 5/4/2018.
 */

public class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected static int MY_EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    protected File EXTERNAL_STORAGE_DIRECTORY;

    protected void requestRuntimePermissions(final Activity activity, final String[] permissions,
                                             int customeConstantPermission, String reason){
        if(permissions.length == 1){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])){
                Snackbar.make(findViewById(android.R.id.content), "App needs permission for: " + reason, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Enable", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(activity, permissions, MY_EXTERNAL_STORAGE_PERMISSION_CODE);
                            }
                        })
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(activity, permissions, MY_EXTERNAL_STORAGE_PERMISSION_CODE);
            }
        }
        else if(permissions.length > 1){
            Snackbar.make(findViewById(android.R.id.content), "App needs permissions for: " + reason, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Enable", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(activity, permissions, MY_EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    })
                    .show();
        }
    }

    protected boolean arePermssionsGranted(Context context, String[] permssions){
        for(String permission: permssions){
            if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }


    /**
     * this method read content from internal storage
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected String readFileInteralStorage(String fileName) throws FileNotFoundException, IOException{
        StringBuilder strBuilder = new StringBuilder();
        String str = null;
        FileInputStream fis = openFileInput(fileName);
        BufferedReader buffReader = new BufferedReader(
                new InputStreamReader(fis)
        );
        while((str = buffReader.readLine()) != null){
            strBuilder.append(str + "\n");
        }

        return strBuilder.toString();

    }

    /**
     * This method read content from a filepath of external storage
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected String readFileExternalStorage(String fileName) throws FileNotFoundException, IOException{
        String pathName = EXTERNAL_STORAGE_DIRECTORY + "/" + fileName;
        String readString;
        StringBuilder strBuilder = new StringBuilder();

        try {
            File f = new File(pathName);
            if(isExternalStorageReadable()){
                BufferedReader buffReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(f))
                );
                while ((readString = buffReader.readLine()) != null){
                    strBuilder.append(readString);
                }
            }
            else
                Toast.makeText(this, "<<ERROR>> DID NOT FIND EXTERNAL STORAGE", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "<<IOException>>" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return strBuilder.toString();
    }

    protected boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
            return true;
        return false;

    }

    protected boolean isExternalStorageWritable(){
        String state  = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
            return  true;
        return false;
    }


    /**
     * this method writes content to a file.
     * @param fileName
     * @param content
     * @throws IOException
     */
    protected void writeFileToExternalStorage(String fileName, String content) throws IOException{

        if(isExternalStorageWritable()){
            EXTERNAL_STORAGE_DIRECTORY = new File(Environment.getExternalStorageDirectory().getPath(), getPackageName());
            EXTERNAL_STORAGE_DIRECTORY.mkdir();
            FileWriter fileWriter = new FileWriter(new File(EXTERNAL_STORAGE_DIRECTORY, fileName));
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            Toast.makeText(this, "Write file completely!", Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(this, "<<ERROR>> DID NOT FIND EXTERNAL STORAGE", Toast.LENGTH_SHORT).show();
    }


    /**
     * this method use to write content to a file in internal storage
     * @param fileName
     * @param content
     * @throws IOException
     */
    protected void writeFileToInteralStorage(String fileName, String content) throws IOException{
        try {
            FileOutputStream fos = openFileOutput(fileName, Activity.MODE_PRIVATE);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
            bufferedWriter.write(content);
            bufferedWriter.close();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "<<IOException>>" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected  boolean isStringEmpty(String string){
        if(string.length() == 0 || string.equals(" ") || string == null)
        {
            Toast.makeText(this, "Your string is empty!", Toast.LENGTH_SHORT).show();
            return  false;
        }

        return true;
    }

}
