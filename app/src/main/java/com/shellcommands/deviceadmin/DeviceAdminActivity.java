package com.shellcommands.deviceadmin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DeviceAdminActivity extends Activity {
    private static final String TAG = "DeviceAdminActivity";
    private static final String PACKAGE_NAME = "com.evolvtechnology.expresskiosk";
    private static final String ADMIN_RECEIVER_CLASS_NAME = "AdminReceiver";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setDeviceAdmin();
        setInstallNonMarketApps();
        setWriteSecureSettings();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            Log.e(TAG, "onActivityResult: Device Admin not set!!! ResultCode: " + resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setDeviceAdmin(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            ComponentName deviceAdminComponent = new ComponentName(PACKAGE_NAME, PACKAGE_NAME + "." + ADMIN_RECEIVER_CLASS_NAME);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Device admin rights are required for this application to work correctly");
            startActivityForResult(intent, 1);
/*
        } else {
            try {
                String command = "dpm set-device-owner " + PACKAGE_NAME + "/." + ADMIN_RECEIVER_CLASS_NAME;
                Log.i(TAG, "setDeviceAdmin: command: " + command);
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader stdOutBufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader stdErrorBufferReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String stdOut = stdOutBufferReader.readLine();
                String stdErr = stdErrorBufferReader.readLine();
                while (stdOut != null && !stdOut.isEmpty()) {
                    Log.d(TAG, "setDeviceAdmin: Set Device Owner: " + stdOut);
                    stdOut = stdOutBufferReader.readLine();
                }
                while (stdErr != null && !stdErr.isEmpty()) {
                    Log.e(TAG, "setDeviceAdmin: Set Device Owner ERROR: " + stdErr);
                    stdErr = stdOutBufferReader.readLine();
                }
            } catch (Exception e) {
                Log.e(TAG, "setDeviceAdmin: Could not set Device Administrator to " + PACKAGE_NAME);
                e.printStackTrace();
            }
        }
*/
    }
    private void setInstallNonMarketApps(){
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + PACKAGE_NAME));
        } else {
            intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        }
        startActivity(intent);
/*
        try {
            String command = "settings put secure install_non_market_apps 1";
            Log.i(TAG, "setInstallNonMarketApps: command: " + command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader stdOutBufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdErrorBufferReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String stdOut = stdOutBufferReader.readLine();
            String stdErr = stdErrorBufferReader.readLine();
            while(stdOut != null && !stdOut.isEmpty()){
                Log.d(TAG, "setInstallNonMarketApps: Set Install Non-Market Apps to True: " + stdOut);
                stdOut = stdOutBufferReader.readLine();
            }
            while(stdErr != null && !stdErr.isEmpty()){
                Log.e(TAG, "setInstallNonMarketApps: Set Install Non-Market Apps to True ERROR: " + stdErr);
                stdErr = stdOutBufferReader.readLine();
            }
        } catch (Exception e){
            Log.e(TAG, "setInstallNonMarketApps: Could not enable installation from non-market apps");
            e.printStackTrace();
        }
*/
    }
    private void setWriteSecureSettings(){
        // Will grant the permissions, but won't work unless PACKAGE_NAME is signed with platform keys.
       try {
            String command = "pm grant " + PACKAGE_NAME + " android.permission.WRITE_SECURE_SETTINGS";
            Log.i(TAG, "setWriteSecureSettings: command: " + command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader stdOutBufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdErrorBufferReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String stdOut = stdOutBufferReader.readLine();
            String stdErr = stdErrorBufferReader.readLine();
            while(stdOut != null && !stdOut.isEmpty()){
                Log.d(TAG, "setWriteSecureSettings: Set Write Secure Permissions: " + stdOut);
                stdOut = stdOutBufferReader.readLine();
            }
            while(stdErr != null && !stdErr.isEmpty()){
                Log.e(TAG, "setWriteSecureSettings: Set Write Secure Permissions ERROR: " + stdErr);
                stdErr = stdOutBufferReader.readLine();
            }
        } catch (Exception e){
            Log.e(TAG, "setWriteSecureSettings: Could set Write Secure Permissions for: " + PACKAGE_NAME);
            e.printStackTrace();
        }
    }
}
