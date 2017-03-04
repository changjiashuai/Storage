package io.github.changjiashuai.storage;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import io.github.changjiashuai.library.Storage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Storage storage;
    private TextView mTvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        storage = new Storage(this);
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
        Log.i(TAG, "InternalStorage path: " + storage.getInternalStorage());
        Log.i(TAG, "Internal getFilesDir: " + storage.getInternalStorage().getFilesDir());
        String[] files = storage.getInternalStorage().getFileList();
        Log.i(TAG, "getFileList: length=" + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.i(TAG, "getFileList: " + files[i]);
        }
        Log.i(TAG, "Internal getCacheDir: " + storage.getInternalStorage().getCacheDir());
        Log.i(TAG, "Internal getDataDir: " + storage.getInternalStorage().getDataDir());
    }

    private void testExternalStorage() {
        Log.i(TAG, "External getDataPkgDir: " + storage.getExternalStorage().getDataPkgDir());
        Log.i(TAG, "External getDataPkgDirPath: " + storage.getExternalStorage().getDataPkgDirPath());
        Log.i(TAG, "External getDataPkgDirWithName: " + storage.getExternalStorage().getDataPkgDirWithName("hahaha"));

        Log.i(TAG, "testExternalStorage: --------getStoragePublicDir------------");

        Log.i(TAG, "External getStoragePublicDir: " + storage.getExternalStorage()
                .getStoragePublicDir(Environment.DIRECTORY_MUSIC));
        Log.i(TAG, "External getStoragePublicDirPath: " + storage.getExternalStorage()
                .getStoragePublicDirPath(Environment.DIRECTORY_MUSIC));
        Log.i(TAG, "External getStoragePublicDirWithName: " + storage.getExternalStorage()
                .getStoragePublicDirWithName(Environment.DIRECTORY_MUSIC, "re"));

        Log.i(TAG, "testExternalStorage: ---------getFilesDir------------------");
        Log.i(TAG, "External getFilesDir: " + storage.getExternalStorage().getFilesDir(Environment.DIRECTORY_DOWNLOADS));
        Log.i(TAG, "External getFilesDirPath: " + storage.getExternalStorage().getFilesDirPath(Environment.DIRECTORY_DOWNLOADS));
        Log.i(TAG, "External getFilesDirWithName: " + storage.getExternalStorage()
                .getFilesDirWithName(Environment.DIRECTORY_DOWNLOADS, "test"));

        Log.i(TAG, "testExternalStorage: ---------getCacheDir------------------");
        Log.i(TAG, "External getCacheDir: " + storage.getExternalStorage().getCacheDir());
        Log.i(TAG, "External getCacheDirPath: " + storage.getExternalStorage().getCacheDirPath());
        Log.i(TAG, "External getCacheDirWithName: " + storage.getExternalStorage()
                .getCacheDirWithName("test"));


    }

    private void testPaths(){
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
}