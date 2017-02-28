package io.github.changjiashuai.library;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

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
    public class InternalStorage {

        private Context mContext;

        private InternalStorage(Context context) {
            mContext = context;
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
         * 向指定的文件中写入指定的数据
         *
         * @param name    文件名
         * @param content 文件内容
         * @param mode    MODE_PRIVATE | MODE_APPEND 自 API 级别 17 以来，常量 MODE_WORLD_READABLE 和
         *                MODE_WORLD_WRITEABLE 已被弃用
         */
        public void writeFileData(String name, String content, int mode) {
            FileOutputStream fos = null;
            try {
                fos = mContext.openFileOutput(name, mode);
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
        public String readFileData(String fileName) {
            String result = "";
            FileInputStream fis = null;
            try {
                fis = mContext.openFileInput(fileName);
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

        private String getString(final byte[] data, final String charset) {
            if (data == null) {
                throw new IllegalArgumentException("Parameter may not be null");
            }
            return getString(data, 0, data.length, charset);
        }

        private String getString(final byte[] data, int offset, int length, String charset) {
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
         * @see #writeFileData(String, String, int)
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
    public class ExternalStorage {

        private static final String TAG = "ExternalStorage";
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

        /* Checks if external storage is available for read and write */
        public boolean isStorageWritable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        /* Checks if external storage is available to at least read */
        public boolean isStorageReadable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }

        /**
         * @return /storage/emulated/0/Android/data/包名/cache
         */
        public File getCacheDir() {
            return mContext.getExternalCacheDir();
        }

        public File[] getCacheDirs() {
            return ContextCompat.getExternalCacheDirs(mContext);
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
        public File getFilesDir(String type) {
            return mContext.getExternalFilesDir(type);
        }

        public File[] getFilesDirs(String type) {
            return ContextCompat.getExternalFilesDirs(mContext, type);
        }

        /**
         * @return 保存可与其他应用共享的文件
         */
        public File getStoragePublicDirectory(String type) {
            return Environment.getExternalStoragePublicDirectory(type);
        }

        /**
         * @param type 目录类型
         * @param name 目录名称
         * @return 在公共目录中创建了一个指定名称的目录：
         */
        public File getStoragePublicDirWithName(String type, String name) {
            // Get the directory for the user's public directory.
            File file = new File(getStoragePublicDirectory(type), name);
            if (!file.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }
            return file;
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


    /**
     * 清除缓存：
     *
     * 1. 将外部私有数据下的cache包（/storage/emulated/0/Android/data/包名/cache）清除，
     *
     * 2. 将内部数据下的cache包下的内容（/data/data/包名/cache/XXX）清除 。
     */
    public void clearCache() {
        File mInternalCacheFile = mInternalStorage.getCacheDir();
        if (!mInternalCacheFile.delete()) {
            Log.e(TAG, "Internal Cache File Deleted Failed!!!");
        }
        File mExternalCacheFile = mExternalStorage.getCacheDir();
        if (!mExternalCacheFile.delete()) {
            Log.e(TAG, "External Cache File Deleted Failed!!!");
        }
    }
//    清除数据：将外部私有数据包（/storage/emulated/0/Android/data/包名）清除，
//             将内部数据下的所有内容（/data/data/包名/XXX）清除；

    public void clearData() {
        mInternalStorage.getDataDir();
    }

    //======
//其次介绍几个除了/data目录之外的目录
// 1. /mnt :这个目录专门用来当作挂载点(MountPoint)。通俗点说,/mnt就是来挂载外部存储设备的(如sdcard),我们的sdcard将会被手机系统视作一个文件夹,这个文件夹将会被系统嵌入到收集系统的mnt目录
// 2. /dev包：Linux系统的常规文件夹。
// 3. /system包：系统配置的文件夹，比如Android系统框架（framework）、底层类库（lib）、字体（font）等。

}