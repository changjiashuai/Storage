package io.github.changjiashuai.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <a href="https://developer.android.com/guide/topics/data/data-storage.html">data-storage</a>
 *
 * Email: changjiashuai@gmail.com
 *
 * Created by CJS on 2017/2/28 11:22.
 */

public class Storage {

    private InternalStorage mInternalStorage;
    private ExternalStorage mExternalStorage;

    public Storage(Context context) {
        mInternalStorage = new InternalStorage(context);
        mExternalStorage = new ExternalStorage(context);
    }

    public InternalStorage getInternalStorage() {
        return mInternalStorage;
    }

    public ExternalStorage getExternalStorage() {
        return mExternalStorage;
    }

    //内存:全称内部存储,英文名(InternalStorage)。
    //当我们在打开DDMS下的File Explorer面板的时候，/data目录就是所谓的内部存储 (ROM )。
    //但是注意，当手机没有root的时候不能打开此文件夹。
    // --> /data/app
    // 1.app文件夹里存放着我们所有安装的app的apk文件

    // --> /data/data
    // 2.第二个文件夹是data,也就是我们常说的/data/data目录(存储包私有数据)。


    //    内部数据：/data/data/包名/XXX

    // 此目录下将每一个APP的存储内容按照包名分类存放好。
    //    比如:
    //    1.data/data/包名/shared_prefs 存放该APP内的SP信息
    //    2.data/data/包名/databases 存放该APP的数据库信息
    //    3.data/data/包名/files 将APP的文件信息存放在files文件夹
    //    4.data/data/包名/cache 存放的是APP的缓存信息
    public class InternalStorage {

        private Context mContext;

        private InternalStorage(Context context) {
            mContext = context;
        }

        /* 'data/data/包名/shared_prefs' */
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return mContext.getSharedPreferences(name, mode);
        }

        /* 'data/data/包名/databases' */
        public File getDatabasePath(String name) {
            return mContext.getDatabasePath(name);
        }

        /**
         * 获取在其中存储内部文件的文件系统目录的绝对路径。
         *
         * @return /data/data/包名/files
         */
        public File getFilesDir() {
            return mContext.getFilesDir();
        }

        /**
         * @return 返回您的应用当前保存的一系列文件
         */
        public String[] getFileList() {
            return mContext.fileList();
        }

        /**
         * @param name 文件名
         * @return 删除保存在内部存储的文件。
         */
        public boolean deleteFile(String name) {
            return mContext.deleteFile(name);
        }

        /**
         * @param name    文件名
         * @param content 文件内容
         * @param mode    MODE_PRIVATE | MODE_APPEND 自 API 级别 17 以来，常量 MODE_WORLD_READABLE 和
         *                MODE_WORLD_WRITEABLE 已被弃用
         */
        public void openFileOutput(String name, String content, int mode) throws IOException {
            FileOutputStream fos = mContext.openFileOutput(name, mode);
            fos.write(content.getBytes());
            fos.close();
        }

        /**
         * @param name 文件名
         * @param mode MODE_PRIVATE | MODE_APPEND 自 API 级别 17 以来，常量 MODE_WORLD_READABLE 和
         *             MODE_WORLD_WRITEABLE 已被弃用
         * @return 在您的内部存储空间内创建（或打开现有的）目录。
         * @see #openFileOutput(String, String, int)
         */
        public File getDir(String name, int mode) {
            return mContext.getDir(name, mode);
        }

        /**
         * 将临时缓存文件保存到的内部目录。 使其占用的空间保持在合理的限制范围内（例如 1 MB）
         *
         * @return /data/data/包名/cache
         */
        public File getCacheDir() {
            return mContext.getCacheDir();
        }

        /**
         * dir: data|user/0
         *
         * @return /data/{dir}/包名
         */
        public File getDataDir() {
            return ContextCompat.getDataDir(mContext);
        }

        @Override
        public String toString() {
            return getDataDir().getPath();
        }
    }


    //外存:
    // /storage/sdcard/Android/data目录或者说/storage/emulated/0/Android/data包目录属于外部存储。
    // 比如我们的内部存储卡。
    // 注意,Google官方建议开发者将App的数据存储在私有目录即/storage/emulated/0/Android/data包下，
    // 这样卸载App时数据会随之被系统清除，不会造成数据残留。


    //    外部应用私有数据：/storage/emulated/0/Android/data/包名/XXX
    //    外部公有数据：/storage/emulated/0

    //    从Android 1.0开始，写操作受权限WRITE_EXTERNAL_STORAGE保护。
    //    从Android 4.1开始，读操作受权限READ_EXTERNAL_STORAGE保护。
    //    从Android 4.4开始，应用可以管理在它外部存储上的特定包名目录，而不用获取WRITE_EXTERNAL_STORAGE权限。
    public class ExternalStorage {

        private Context mContext;

        private ExternalStorage(Context context) {
            mContext = context;
        }

        /**
         * 判断外部设置是否有效
         */
        public boolean isEmulated() {
            return Environment.isExternalStorageEmulated();
        }

        /**
         * 判断外部设置是否可以移除
         */
        public boolean isRemovable() {
            return Environment.isExternalStorageEmulated();
        }

        public String getStorageState() {
            return Environment.getExternalStorageState();
        }

        /**
         * @return /storage/emulated/0/Android/data/包名/cache
         */
        public File getCacheDir() {
            return mContext.getExternalCacheDir();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public File[] getCacheDirs() {
            return mContext.getExternalCacheDirs();
        }

        /**
         * @param type The type of files directory to return. May be {@code null} for the root of
         *             the files directory or one of the following constants for a subdirectory:
         *             {@link android.os.Environment#DIRECTORY_MUSIC}, {@link
         *             android.os.Environment#DIRECTORY_PODCASTS}, {@link android.os.Environment#DIRECTORY_RINGTONES},
         *             {@link android.os.Environment#DIRECTORY_ALARMS}, {@link
         *             android.os.Environment#DIRECTORY_NOTIFICATIONS}, {@link
         *             android.os.Environment#DIRECTORY_PICTURES}, or {@link
         *             android.os.Environment#DIRECTORY_MOVIES}.
         * @return /storage/emulated/0/Android/data/包名/files/{type}
         */
        public File getFilesDir(String type) {
            return mContext.getExternalFilesDir(type);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public File[] getFilesDirs(String type) {
            return mContext.getExternalFilesDirs(type);
        }

        /**
         * @return /storage/emulated/0
         */
        public File getDataDir() {
            return Environment.getExternalStorageDirectory();
        }

        @Override
        public String toString() {
            return getDataDir().getPath();
        }
    }


    //======
//其次介绍几个除了/data目录之外的目录
// 1. /mnt :这个目录专门用来当作挂载点(MountPoint)。通俗点说,/mnt就是来挂载外部存储设备的(如sdcard),我们的sdcard将会被手机系统视作一个文件夹,这个文件夹将会被系统嵌入到收集系统的mnt目录
// 2. /dev包：Linux系统的常规文件夹。
// 3. /system包：系统配置的文件夹，比如Android系统框架（framework）、底层类库（lib）、字体（font）等。


//当用应用管理来清除数据的时候:
//    清除缓存：将外部私有数据下的cache包（/storage/emulated/0/Android/data/包名/cache）清除，
//             将内部数据下的cache包下的内容（/data/data/包名/cache/XXX）清除 。

    public void clearCache() {
        mInternalStorage.getCacheDir();
        mExternalStorage.getCacheDir();
    }
//    清楚数据：将外部私有数据包（/storage/emulated/0/Android/data/包名）清除，
//             将内部数据下的所有内容（/data/data/包名/XXX）清除；

    public void clearData() {
        mInternalStorage.getDataDir();
    }
}
