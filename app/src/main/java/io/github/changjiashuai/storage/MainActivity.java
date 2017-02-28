package io.github.changjiashuai.storage;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.github.changjiashuai.library.Storage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage = new Storage(this);
        testInternalStorage();
        Log.i(TAG, "----------------------------------");
        testExternalStorage();
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
        Log.i(TAG, "External DIRECTORY_MUSIC: " + storage.getExternalStorage().getFilesDir(Environment.DIRECTORY_MUSIC));
        Log.i(TAG, "External getCacheDir: " + storage.getExternalStorage().getCacheDir());
        Log.i(TAG, "External getDataDir: " + storage.getExternalStorage().getDataDir());
    }
}
