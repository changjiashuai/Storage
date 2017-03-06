package io.github.changjiashuai.storage;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import io.github.changjiashuai.library.Storage;
import io.github.changjiashuai.library.Storage.ExternalStorage;
import io.github.changjiashuai.library.Storage.InternalStorage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    public static final int RC_EXTERNAL = 0;
    private TextView mTvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
//        testInternalStorage();
//        Log.i(TAG, "---------------------------------");
        testExternalStorage();
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMarket("com.insta360.explore", null);
            }
        });
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 if null 则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public void navigateToMarket(@NonNull String appPkg, String marketPkg) {
        try {
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "navigateToMarket: no market app installed", e);
        }
    }


    private void testInternalStorage() {
        Log.i(TAG, "Internal getFilesDir: " + InternalStorage.getFilesDir(this));
        String[] files = InternalStorage.getFileList(this);
        Log.i(TAG, "getFileList: length=" + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.i(TAG, "getFileList: " + files[i]);
        }
        Log.i(TAG, "Internal getCacheDir: " + InternalStorage.getCacheDir(this));
        Log.i(TAG, "Internal getDataDir: " + InternalStorage.getDataDir(this));
    }

    @AfterPermissionGranted(RC_EXTERNAL)
    private void testExternalStorage() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing

            Log.i(TAG, "getTotalExternalStorageSize: " + Formatter.formatFileSize(this,
                    Storage.getTotalExternalStorageSize()));
            Log.i(TAG, "getRemainExternalStorageSize: " + Formatter.formatFileSize(this,
                    Storage.getRemainExternalStorageSize()));
            Log.i(TAG, "getTotalInternalStorageSize: " + Formatter.formatFileSize(this,
                    Storage.getTotalInternalStorageSize()));
            Log.i(TAG, "getRemainInternalStorageSize: " + Formatter.formatFileSize(this,
                    Storage.getRemainInternalStorageSize()));

            Log.i(TAG, "testExternalStorage: -------------");

            Log.i(TAG, "External getDataPkgDir: " + ExternalStorage.getDataPkgDir(this));
            Log.i(TAG, "External getDataPkgDirPath: " + ExternalStorage.getDataPkgDirPath(this));
            Log.i(TAG, "External createDirInDataPkgDir: " + ExternalStorage.createDirInDataPkgDir(this, "hahaha"));

            Log.i(TAG, "testExternalStorage: --------getStoragePublicDir------------");

            Log.i(TAG, "External getStoragePublicDir: " + ExternalStorage
                    .getStoragePublicDir(Environment.DIRECTORY_MUSIC));
            Log.i(TAG, "External getStoragePublicDirPath: " + ExternalStorage
                    .getStoragePublicDirPath(Environment.DIRECTORY_MUSIC));
            Log.i(TAG, "External createDirInStoragePublicDir: " + ExternalStorage
                    .createDirInStoragePublicDir(Environment.DIRECTORY_MUSIC, "TestMusic"));

            Log.i(TAG, "testExternalStorage: ---------getFilesDir------------------");
            Log.i(TAG, "External getFilesDir: " + ExternalStorage.getFilesDir(this, Environment.DIRECTORY_DOWNLOADS));
            Log.i(TAG, "External getFilesDirPath: " + ExternalStorage.getFilesDirPath(this, Environment.DIRECTORY_DOWNLOADS));
            Log.i(TAG, "External createDirInFilesDir: " + ExternalStorage
                    .createDirInFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "test"));

            Log.i(TAG, "testExternalStorage: ---------getCacheDir------------------");
            Log.i(TAG, "External getCacheDir: " + ExternalStorage.getCacheDir(this));
            Log.i(TAG, "External getCacheDirPath: " + ExternalStorage.getCacheDirPath(this));
            Log.i(TAG, "External createFileInCacheDir: " + ExternalStorage
                    .createDirInCacheDir(this, "test"));

            Log.i(TAG, "testExternalStorage: -------------clear-----------------------");

            Log.i(TAG, "getDataSize before: " + Formatter.formatFileSize(this, Storage.getDataSize(this)));
            Storage.clearData(this);
            Log.i(TAG, "getDataSize after: " + Formatter.formatFileSize(this, Storage.getDataSize(this)));

            Log.i(TAG, "getCacheSize: before: " + Formatter.formatFileSize(this, Storage.getCacheSize(this)));
            Storage.clearCache(this);
            Log.i(TAG, "getCacheSize: after: " + Formatter.formatFileSize(this, Storage.getCacheSize(this)));
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "request external perms",
                    RC_EXTERNAL, perms);
        }
    }

    private void testPaths() {
        File[] paths = StorageManagerCompat.getExternalStoragePaths(getApplicationContext());
        int length = paths.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                File file = paths[i];
                Log.i(TAG, "paths: " + file);
                mTvMsg.append("extSdcardPath:" + file);
                mTvMsg.append("\n");
                if (i == 1) {
                    if (file.listFiles() != null) {
                        for (File f : file.listFiles()) {
                            Log.i(TAG, "80F2-FB73: " + f.getName());
                        }
                    }
                }
            }
        }
    }

    private File[] getUsbPath() {
        File storage = new File("/storage");
        return storage.listFiles();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Some permissions have been granted
        // ...
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Some permissions have been denied
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "returned_from_app_settings_to_activity", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}