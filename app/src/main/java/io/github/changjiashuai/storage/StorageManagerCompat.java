package io.github.changjiashuai.storage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.github.changjiashuai.storage.Reflect.on;

/**
 * Email: changjiashuai@gmail.com
 *
 * Created by CJS on 2017/3/2 15:01.
 */

public class StorageManagerCompat {

    private static final String TAG = "StorageManagerCompat";

    //region  getSDCardPathsVersionID()
    public static File[] getExternalStoragePaths(Context context) {
        Log.e(TAG, "当前手机系统版本号=" + Build.VERSION.SDK_INT);
        switch (Build.VERSION.SDK_INT) {
            case 14:
                return getExternalStoragePathsForSDK14(context);
            case 15:
                return getExternalStoragePathsForSDK15(context);
            case 16:
                return getExternalStoragePathsForSDK16(context);
            case 17:
                return getExternalStoragePathsForSDK17(context);
            case 18:
                return getExternalStoragePathsForSDK18(context);
            case 19:
                return getExternalStoragePathsForSDK19();
            case 20:
                return getExternalStoragePathsForSDK20();
            case 21:
                return getExternalStoragePathsForSDK21();
            case 22:
                return getExternalStoragePathsForSDK22();
            case 23:
                return getExternalStoragePathsForSDK23();
            case 24:
                return getExternalStoragePathsForSDK24();
            case 25:
                return getExternalStoragePathsForSDK25();
        }
        Log.e(TAG, "不支持这个版本，请查阅源码");
        return null;
    }

    public static File[] getExternalStoragePathsForSDK25() {
        return getExternalStoragePathsForSDK23();
    }

    @TargetApi(24)
    public static File[] getExternalStoragePathsForSDK24() {
//            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
//            List<StorageVolume> storageVolumes = mStorageManager.getStorageVolumes();
//            storageVolumes.get(0).describeContents()
        return getExternalStoragePathsForSDK23();
    }

    /**
     * 通过使用Environment.getExternalStorageDirectory()所使用的方法得到本机中所有存储卡的位置
     *
     * 这个总体思路是对的，但是被一些具体的实现给卡住了
     */
    @TargetApi(23)//这里反射的方法之后api23才有所以我们这个方法是针对这个版本的
    public static File[] getExternalStoragePathsForSDK23() {
        int mUId = on(UserHandle.class).call("myUserId").get();
        return on("android.os.Environment$UserEnvironment")
                .create(mUId)
                .call("getExternalDirs").get();
    }

    /**
     * 在真机对应版本上进行测试没有任何问题，
     *
     * 但是我还有一个问题，就是为什么在模拟器上就得不到这样一个结果，
     *
     * 是因为模拟器的源码和真机的有不同，还是我创建真机的时候有异常？
     *
     * @return 获取到的存储设备
     */
    @TargetApi(22)
    public static File[] getExternalStoragePathsForSDK22() {
        int mUId = on(UserHandle.class).call("myUserId").get();
        return on("android.os.Environment$UserEnvironment")
                .create(mUId)
                .call("getExternalDirsForApp").get();
    }

    /**
     * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     *
     * @return 获取到的存储设备
     */
    @TargetApi(21)
    public static File[] getExternalStoragePathsForSDK21() {
        return getExternalStoragePathsForSDK22();
    }

    /**
     * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     *
     * @return 获取到的存储设备
     */
    @TargetApi(20)
    public static File[] getExternalStoragePathsForSDK20() {
        return getExternalStoragePathsForSDK22();
    }

    /**
     * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     *
     * @return 获取到的存储设备
     */
    @TargetApi(19)
    public static File[] getExternalStoragePathsForSDK19() {
        return getExternalStoragePathsForSDK22();
    }

    /**
     * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     *
     * @return 获取到的存储设备
     */
    @TargetApi(18)
    public static File[] getExternalStoragePathsForSDK18(Context context) {
        StorageManager storageManager = (StorageManager) context.getApplicationContext().getSystemService(Context.STORAGE_SERVICE);
        StorageVolume[] storageVolumes = Reflect.on(storageManager).call("getVolumeList").get();
        List<File> files = new ArrayList<>();
        for (StorageVolume storageVolume : storageVolumes) {
            String path = Reflect.on(storageVolume).call("getPath").get();
            String volumeState = Reflect.on(storageManager).call("getVolumeState", path).get();
            boolean isRemovable = Reflect.on(storageVolume).call("isRemovable").get();
            File pathFile = Reflect.on(storageVolume).call("getPathFile").get();
            files.add(pathFile);
        }
        return (File[]) files.toArray();
    }

    /**
     * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     *
     * @return 获取到的存储设备
     */
    @TargetApi(17)
    public static File[] getExternalStoragePathsForSDK17(Context context) {
        return getExternalStoragePathsForSDK18(context);
    }

    /**
     * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     * <p/>
     * api16在StorageVolume方法中没有getPathFile
     *
     * @return 获取到的存储设备
     */
    @TargetApi(16)
    public static File[] getExternalStoragePathsForSDK16(Context context) {
        StorageManager storageManager = (StorageManager) context.getApplicationContext().getSystemService(Context.STORAGE_SERVICE);
        StorageVolume[] storageVolumes = Reflect.on(storageManager).call("getVolumeList").get();
        List<File> files = new ArrayList<>();
        for (StorageVolume storageVolume : storageVolumes) {
            String path = Reflect.on(storageVolume).call("getPath").get();
            String volumeState = Reflect.on(storageManager).call("getVolumeState", path).get();
            boolean isRemovable = Reflect.on(storageVolume).call("isRemovable").get();
            files.add(new File(path));
        }
        return (File[]) files.toArray();
    }

    /**
     * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     *
     * @return 获取到的存储设备
     */
    @TargetApi(15)
    public static File[] getExternalStoragePathsForSDK15(Context context) {
        return getExternalStoragePathsForSDK16(context);
    }

    /**
     * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     *
     * @return 获取到的存储设备
     */
    @TargetApi(14)
    public static File[] getExternalStoragePathsForSDK14(Context context) {
        return getExternalStoragePathsForSDK16(context);
    }
    //endregion
}