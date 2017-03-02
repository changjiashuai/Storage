package io.github.changjiashuai.storage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
                launchAppDetail("com.insta360.explore", null);
            }
        });
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) return;

//            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
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
        Log.i(TAG, "ExternalStorage Emulated: " + storage.getExternalStorage().isEmulated());
        Log.i(TAG, "ExternalStorage Removable: " + storage.getExternalStorage().isRemovable());
        Log.i(TAG, "ExternalStorage path: " + storage.getExternalStorage().toString());
        Log.i(TAG, "External getFilesDir: " + storage.getExternalStorage().getFilesDir(null));
        File[] files = storage.getExternalStorage().getFilesDirs(null);
        for (File file : files) {
            Log.i(TAG, "External getFilesDir: " + file);
        }
        Log.i(TAG, "External getStoragePublicDirectory: " + storage.getExternalStorage().getStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        Log.i(TAG, "External getCacheDir: " + storage.getExternalStorage().getCacheDir());
        Log.i(TAG, "External getDataDir: " + storage.getExternalStorage().getDataDir());


//        MySDCard mySDCard = new MySDCard(this);
        File[] paths = StorageManagerCompat.getExternalStoragePaths(getApplicationContext());
//        File[] paths = getUsbPath();
        int length = paths.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                File file = paths[i];
                Log.i(TAG, "paths: " + file);
                mTvMsg.append("extSdcardPath:" + file);
                mTvMsg.append("\n");
                if (i==1){
                    for (File f: file.listFiles()){
                        Log.i(TAG, "80F2-FB73: " + f.getName());
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