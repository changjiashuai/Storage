package io.github.changjiashuai.library;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;
import static android.os.Environment.getExternalStorageState;

/**
 * <a href="https://developer.android.com/guide/topics/data/data-storage.html">data-storage</a>
 *
 * Email: changjiashuai@gmail.com
 *
 * Created by CJS on 2017/2/28 11:22.
 */

public class Storage {

    /**
     * 内部存储
     *
     * <p>当我们在打开DDMS下的File Explorer面板的时候，/data目录就是所谓的内部存储 (ROM )。 但是注意，当手机没有root的时候不能打开此文件夹。</p>
     *
     * <li>'/data/app' :--> app文件夹里存放着我们所有安装的app的apk文件</li> <br/>
     *
     * <li>'/data/data':-->第二个文件夹是data,也就是我们常说的/data/data目录(存储包私有数据)</li>
     *
     * <li>内部数据：/data/data/包名/XXX</li>
     *
     * //此目录下将每一个APP的存储内容按照包名分类存放好。 比如:
     *
     * <li>1.data/data/包名/shared_prefs 存放该APP内的SP信息
     *
     * <li>2.data/data/包名/databases 存放该APP的数据库信息
     *
     * <li>3.data/data/包名/files 将APP的文件信息存放在files文件夹
     *
     * <li>/4.data/data/包名/cache 存放的是APP的缓存信息
     */
    public static class InternalStorage {

        /**
         * 获取在其中存储内部文件的文件系统目录的绝对路径。
         *
         * @return /data/data/包名/files
         */
        public static File getFilesDir(Context context) {
            return context.getFilesDir();
        }

        /**
         * @return 返回您的应用当前保存的一系列文件
         */
        public static String[] getFileList(Context context) {
            return context.fileList();
        }

        /**
         * @param name 文件名
         * @return 删除保存在内部存储的文件。
         */
        public static boolean deleteFile(Context context, String name) {
            return context.deleteFile(name);
        }

        /**
         * 向指定的文件中写入指定的数据
         *
         * @param name    文件名
         * @param content 文件内容
         * @param mode    MODE_PRIVATE | MODE_APPEND 自 API 级别 17 以来，常量 MODE_WORLD_READABLE 和
         *                MODE_WORLD_WRITEABLE 已被弃用
         */
        public static void writeFileData(Context context, String name, String content, int mode) {
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(name, mode);
                fos.write(content.getBytes());
            } catch (IOException e) {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 打开指定文件，读取其数据，返回字符串对象
         */
        public static String readFileData(Context context, String fileName) {
            String result = "";
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(fileName);
                //获取文件长度
                int length = fis.available();
                byte[] buffer = new byte[length];
                if (fis.read(buffer) != -1) {
                    //将byte数组转换成指定格式的字符串
                    result = getString(buffer, "UTF-8");
                }
            } catch (Exception e) {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        private static String getString(final byte[] data, final String charset) {
            if (data == null) {
                throw new IllegalArgumentException("Parameter may not be null");
            }
            return getString(data, 0, data.length, charset);
        }

        private static String getString(final byte[] data, int offset, int length, String charset) {
            if (data == null) {
                throw new IllegalArgumentException("Parameter may not be null");
            }
            if (charset == null || charset.length() == 0) {
                throw new IllegalArgumentException("charset may not be null or empty");
            }
            try {
                return new String(data, offset, length, charset);
            } catch (UnsupportedEncodingException e) {
                return new String(data, offset, length);
            }
        }

        /**
         * @param name 文件名
         * @param mode MODE_PRIVATE | MODE_APPEND 自 API 级别 17 以来，常量 MODE_WORLD_READABLE 和
         *             MODE_WORLD_WRITEABLE 已被弃用
         * @return 在您的内部存储空间内创建（或打开现有的）目录。
         * @see #writeFileData(Context, String, String, int)
         */
        public static File getDir(Context context, String name, int mode) {
            return context.getDir(name, mode);
        }

        /**
         * 将临时缓存文件保存到的内部目录。 使其占用的空间保持在合理的限制范围内（例如 1 MB）
         *
         * @return /data/data/包名/cache
         */
        public static File getCacheDir(Context context) {
            return context.getCacheDir();
        }

        /**
         * dir: data|user/0
         *
         * @return /data/{dir}/包名
         */
        public static File getDataDir(Context context) {
            return ContextCompat.getDataDir(context);
        }

    }


    /**
     * 外部存储
     *
     * <li> /storage/sdcard/Android/data目录或者说/storage/emulated/0/Android/data包目录属于外部存储。
     *
     * <li> 比如我们的内部存储卡。</li>
     *
     * <li>注意,Google官方建议开发者将App的数据存储在私有目录即/storage/emulated/0/Android/data包下，
     * 这样卸载App时数据会随之被系统清除，不会造成数据残留。<li/>
     *
     * <li>外部应用私有数据：/storage/emulated/0/Android/data/包名/XXX </li>
     *
     * <li>外部公有数据：/storage/emulated/0 </li>
     *
     * <li>从Android 1.0开始，写操作受权限WRITE_EXTERNAL_STORAGE保护。</li>
     *
     * <li>从Android 4.1开始，读操作受权限READ_EXTERNAL_STORAGE保护。</li>
     *
     * <li>从Android 4.4开始，应用可以管理在它外部存储上的特定包名目录，而不用获取WRITE_EXTERNAL_STORAGE权限。</li>
     */
    public static class ExternalStorage {

        private static final String TAG = "ExternalStorage";

        /**
         * 判断外部设置是否有效
         */
        public static boolean isEmulated() {
            return Environment.isExternalStorageEmulated();
        }

        /**
         * 判断外部设置是否可以移除
         */
        public static boolean isRemovable() {
            return Environment.isExternalStorageEmulated();
        }

        public static String getStorageState() {
            return getExternalStorageState();
        }

        /* Checks if external storage is available for read and write */
        public static boolean isStorageWritable() {
            String state = getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        /* Checks if external storage is available to at least read */
        public static boolean isStorageReadable() {
            String state = getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }

        /**
         * @return /storage/emulated/0/Android/data/包名/cache
         */
        public static File getCacheDir(Context context) {
            return context.getExternalCacheDir();
        }

        public static File createDirInCacheDir(Context context, String name) {
            File file = getCacheDir(context);
            if (file != null) {
                File newFile = new File(file, name);
                if (!newFile.exists()) {
                    if (!newFile.mkdirs()) {
                        Log.e(TAG, "getCacheDirWithName:  Directory not created");
                        return null;
                    }
                }
                return newFile;
            }
            return null;
        }

        public static String getCacheDirPath(Context context) {
            File file = getCacheDir(context);
            if (file != null) {
                return file.getPath();
            } else {
                return null;
            }
        }

        public static File[] getCacheDirs(Context context) {
            return ContextCompat.getExternalCacheDirs(context);
        }

        /**
         * 保存应用私有文件
         *
         * @param type The type of files directory to return. May be {@code null} for the root of
         *             the files directory or one of the following constants for a subdirectory:
         *             {@link android.os.Environment#DIRECTORY_MUSIC}, {@link
         *             android.os.Environment#DIRECTORY_PODCASTS}, {@link android.os.Environment#DIRECTORY_RINGTONES},
         *             {@link android.os.Environment#DIRECTORY_ALARMS}, {@link
         *             android.os.Environment#DIRECTORY_NOTIFICATIONS}, {@link
         *             android.os.Environment#DIRECTORY_PICTURES}, or {@link
         *             android.os.Environment#DIRECTORY_MOVIES}.
         *
         *             如果您不需要特定的媒体目录，请传递 null 以接收应用私有目录的根目录。
         * @return /storage/emulated/0/Android/data/包名/files/{type}
         */
        public static File getFilesDir(Context context, String type) {
            return context.getExternalFilesDir(type);
        }

        public static File createDirInFilesDir(Context context, String type, String name) {
            File file = getFilesDir(context, type);
            if (file != null) {
                File newFile = new File(file, name);
                if (!newFile.exists()) {
                    if (!newFile.mkdirs()) {
                        Log.e(TAG, "getFilesDirWithName:  Directory not created");
                        return null;
                    }
                }
                return newFile;
            }
            return null;
        }

        public static String getFilesDirPath(Context context, String type) {
            File file = getFilesDir(context, type);
            if (file != null) {
                return file.getPath();
            } else {
                return null;
            }
        }

        public static File[] getFilesDirs(Context context, String type) {
            return ContextCompat.getExternalFilesDirs(context, type);
        }

        /**
         * @return 保存可与其他应用共享的文件
         */
        @Deprecated
        public static File getStoragePublicDirectory(String type) {
            return Environment.getExternalStoragePublicDirectory(type);
        }

        public static File getStoragePublicDir(String type) {
            return Environment.getExternalStoragePublicDirectory(type);
        }

        /**
         * Writing to this path requires the
         * {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission
         *
         * @param type 目录类型
         * @param name 目录名称
         * @return 在公共目录中创建了一个指定名称的目录：
         */
        public static File createDirInStoragePublicDir(String type, String name) {
            // Get the directory for the user's public directory.
            File file = new File(getStoragePublicDir(type), name);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.e(TAG, "getStoragePublicDirWithName Directory not created");
                    return null;
                }
            }
            return file;
        }

        public static String getStoragePublicDirPath(String type) {
            File file = getStoragePublicDir(type);
            if (file != null) {
                return file.getPath();
            }
            return null;
        }

        /**
         * @return /storage/emulated/0/Android/data/包名
         */
        public static File getDataPkgDir(Context context) {
            File file = getFilesDir(context, null);
            if (file != null) {
                String path = file.getPath();
                String newPath = path.substring(0, path.lastIndexOf("/"));
                file = new File(newPath);
            }
            return file;
        }

        /**
         * @param name 文件名
         * @return /storage/emulated/0/Android/data/io.github.changjiashuai.storage/{name}
         */
        public static File createDirInDataPkgDir(Context context, String name) {
            File file = getDataPkgDir(context);
            if (file != null) {
                File newFile = new File(file, name);
                if (!newFile.exists()) {
                    if (!newFile.mkdirs()) {
                        Log.e(TAG, "createDirInDataPkgDir Directory not created");
                        return null;
                    }
                }
                return newFile;
            }
            return null;
        }

        public static String getDataPkgDirPath(Context context) {
            File file = getDataPkgDir(context);
            if (file != null) {
                return file.getPath();
            }
            return null;
        }
    }


    /**
     * 获取目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    public static long getCacheSize(Context context) {
        long internalCacheSize = getDirSize(InternalStorage.getCacheDir(context));
        long externalCacheSize = getDirSize(ExternalStorage.getCacheDir(context));
        return internalCacheSize + externalCacheSize;
    }


    /**
     * 清除缓存：
     *
     * 1. 将内部数据下的cache包下的内容（/data/data/包名/cache/XXX）清除 。
     *
     * 2. 将外部私有数据下的cache包（/storage/emulated/0/Android/data/包名/cache）清除，
     */
    public static void clearCache(Context context) {
        File mInternalCacheFile = InternalStorage.getCacheDir(context);
        deleteFolderFile(mInternalCacheFile, true);
        File mExternalCacheFile = ExternalStorage.getCacheDir(context);
        deleteFolderFile(mExternalCacheFile, true);
    }

    public static long getDataSize(Context context) {
        long internalDataSize = getDirSize(InternalStorage.getDataDir(context));
        long externalDataSize = getDirSize(ExternalStorage.getDataPkgDir(context));
        return internalDataSize + externalDataSize;
    }

    /**
     * 清除数据：
     *
     * 1. 将内部数据下的所有内容（/data/data/包名/XXX）清除；
     *
     * 2. 将外部私有数据包（/storage/emulated/0/Android/data/包名）清除，
     */
    public static void clearData(Context context) {
        File mInternalDataFile = InternalStorage.getDataDir(context);
        deleteFolderFile(mInternalDataFile, true);
        File mExternalDataFile = ExternalStorage.getDataPkgDir(context);
        deleteFolderFile(mExternalDataFile, true);
    }

    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(File dir, boolean deleteThisPath) {
        try {
            if (dir.isDirectory()) {// 如果下面还有文件
                File files[] = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i], true);
                }
            }
            if (deleteThisPath) {
                if (!dir.isDirectory()) {// 如果是文件，删除
                    dir.delete();
                } else {// 目录
                    if (dir.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        dir.delete();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "deleteFolderFile: ", e);
            e.printStackTrace();
        }
    }

    /**
     * 获取手机内部剩余存储空间
     */
    public static long getRemainInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = stat.getBlockSize();
        }
        long availableBlocks = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部剩余虚拟外部存储空间
     */
    public static long getRemainExternalStorageSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
            } else {
                blockSize = stat.getBlockSize();
            }
            long availableBlocks = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                availableBlocks = stat.getAvailableBlocks();
            }
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取手机内部总的存储空间
     */
    public static long getTotalInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = stat.getBlockSize();
        }
        long totalBlocks = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBlocks = stat.getBlockCountLong();
        } else {
            totalBlocks = stat.getBlockCount();
        }
        return totalBlocks * blockSize;
    }

    /**
     * 获取手机内部总的虚拟外部存储空间
     */
    public static long getTotalExternalStorageSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
            } else {
                blockSize = stat.getBlockSize();
            }
            long totalBlocks = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                totalBlocks = stat.getBlockCountLong();
            } else {
                totalBlocks = stat.getBlockCount();
            }
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    //=============其次介绍几个除了/data目录之外的目录===============
    // 1. /mnt :这个目录专门用来当作挂载点(MountPoint)。通俗点说,/mnt就是来挂载外部存储设备的(如sdcard),
    // 我们的sdcard将会被手机系统视作一个文件夹,这个文件夹将会被系统嵌入到收集系统的mnt目录
    // 2. /dev包：Linux系统的常规文件夹。
    // 3. /system包：系统配置的文件夹，比如Android系统框架（framework）、底层类库（lib）、字体（font）等。

}