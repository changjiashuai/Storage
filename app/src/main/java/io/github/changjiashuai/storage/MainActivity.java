package io.github.changjiashuai.storage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
//        File[] paths = mySDCard.getSDCardPaths();
////        File[] paths = getUsbPath();
//        int length = paths.length;
//        if (length > 0) {
//            for (int i = 0; i < length; i++) {
//                File file = paths[i];
//                Log.i(TAG, "paths: " + file);
//                mTvMsg.append("extSdcardPath:" + file);
//                mTvMsg.append("\n");
//                if (i==1){
//                    for (File f: file.listFiles()){
//                        Log.i(TAG, "80F2-FB73: " + f.getName());
//                    }
//                }
//            }
//        }
    }

    private File[] getUsbPath() {
        File storage = new File("/storage");
        return storage.listFiles();
    }

    public class MySDCard {
        private static final String TAG = "MySDCard";
        private Context context;

        /**
         * 构造方法
         */
        public MySDCard(Context context) {
            this.context = context;
        }

        //region  getSDCardPathsVersionID()
        public File[] getSDCardPaths() {
//            Log.e(TAG, "当前手机系统版本号=" + Build.VERSION.SDK_INT);
            switch (Build.VERSION.SDK_INT) {
                case 24:
                    return getSDCardPaths_23();
                case 23://23
                    return this.getSDCardPaths_23();
                case 22://22
                    return this.getSDCardPaths_22();
                case 21:
                    return this.getSDCardPaths_21();
                case 20:
                    return this.getSDCardPaths_20();
                case 19:
                    return this.getSDCardPaths_19();
                case 18:
                    return this.getSDCardPaths_18();
                case 17:
                    return this.getSDCardPaths_17();
                case 16:
                    return this.getSDCardPaths_16();
                case 15:
                    return this.getSDCardPaths_15();
                case 14:
                    return this.getSDCardPaths_14();
                //            case 13:
                //                return this.getSDCardPaths_13();
                //            case 12:
                //                return this.getSDCardPaths_12();
                //            case 11:
                //                return this.getSDCardPaths_11();
                //            case 10:
                //                return this.getSDCardPaths_10();
                //            case 9:
                //                return this.getSDCardPaths_9();
                //            case 8:
                //                return this.getSDCardPaths_8();
            }
//            Log.e(TAG, "不支持这个版本，请查阅源码");
            return null;
        }

//        @TargetApi(24)
//        public File[] getSDCardPaths_24(){
//            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
//            List<StorageVolume> storageVolumes = mStorageManager.getStorageVolumes();
//
//            storageVolumes.get(0).describeContents()
//        }

        /**
         * 通过使用Environment.getExternalStorageDirectory()所使用的方法得到本机中所有存储卡的位置
         * <p/>
         * 这个总体思路是对的，但是被一些具体的实现给卡住了
         */
        @TargetApi(23)//这里反射的方法之后api23才有所以我们这个方法是针对这个版本的
        public File[] getSDCardPaths_23() {
//            Log.e(TAG, "我们得到的mUID=" + Environment.getExternalStorageDirectory().getPath());
            try {//我们的思路：首先得到方法-然后执行方法
                Class<?> class_UserEnvironment = Class
                        .forName("android.os.Environment$UserEnvironment");
                Method method_getExternalDirs = class_UserEnvironment.getMethod("getExternalDirs");
                //方法的执行需要这个方法所在类的一个对象
                Constructor<?> ueConstructor = class_UserEnvironment
                        .getConstructor(new Class[]{int.class});
                //这里可以自己初始化，可以以直接通过Environment来调用
                Method method_myUserId = UserHandle.class.getMethod("myUserId");
                int mUId = (int) method_myUserId
                        .invoke(null);//如果底层方法所需的形参数为 0，则所提供的 args 数组长度可以为 0 或 null。
//                Log.e(TAG, "我们得到的mUID=" + mUId);
                //ueConstructor.newInstance(mUId);这个方法就是获取到类对象的方法
                File[] files = (File[]) method_getExternalDirs
                        .invoke(ueConstructor.newInstance(mUId));//以上的一切都是为了这一句服务的
//                Log.e(TAG, "File[].length=" + files.length);
                for (File value : files) {
//                    Log.e(TAG, "-->" + value.getPath());
                }
                return files;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 在真机对应版本上进行测试没有任何问题， 但是我还有一个问题，就是为什么在模拟器上就得不到这样一个结果，是因为模拟器的源码和真机的有不同，还是我创建真机的时候有异常？
         *
         * @return 获取到的存储设备
         */
        @TargetApi(22)
        public File[] getSDCardPaths_22() {
//            Log.e(TAG, "getExternalStorageDirectory=" + Environment
//                    .getExternalStorageDirectory().getPath());
            try {//我们的思路：初始化内部类，调用方法
                Class<?> class_UserEnvironment = Class
                        .forName("android.os.Environment$UserEnvironment");
                Method method_getExternalDirsForApp = class_UserEnvironment
                        .getMethod("getExternalDirsForApp");
                //方法的执行需要这个方法所在类的一个对象
                Constructor<?> ueConstructor = class_UserEnvironment
                        .getConstructor(new Class[]{int.class});
                //这里可以自己初始化，可以以直接通过Environment来调用
                Method method_myUserId = UserHandle.class.getMethod("myUserId");
                int mUId = (int) method_myUserId.invoke(null);
//                Log.e(TAG, "我们得到的mUID=" + mUId);
                //以上的一切都是为了下一句一句服务的
                File[] files = (File[]) method_getExternalDirsForApp
                        .invoke(ueConstructor.newInstance(mUId));
//                Log.e(TAG, "File[].length=" + files.length);
                for (File value : files) {
                    //boolean isRemovable = (boolean) method_isRemovable.invoke(value);//是否可以移除
                    //String getVolumeState = (String) method_getVolumeState.invoke(sm, path);//获取挂载状态。
//                    Log.e(TAG, "-->" + value.getPath());
                }
                return files;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
         *
         * @return 获取到的存储设备
         */
        @TargetApi(21)
        public File[] getSDCardPaths_21() {
            return getSDCardPaths_22();
        }

        /**
         * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
         *
         * @return 获取到的存储设备
         */
        @TargetApi(20)
        public File[] getSDCardPaths_20() {
            return getSDCardPaths_22();
        }

        /**
         * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
         *
         * @return 获取到的存储设备
         */
        @TargetApi(19)
        public File[] getSDCardPaths_19() {
            return getSDCardPaths_22();
        }

        /**
         * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
         * <p/>
         * 借鉴：http://vjson .com/wordpress/%E8%8E%B7%E5%8F%96android%E8%AE%BE%E5%A4%87%E6%8C%82%E8%BD%BD
         * %E7%9A%84%E6%89%80%E6%9C%89%E5%AD%98%E5%82%A8%E5%99%A8.html
         *
         * @return 获取到的存储设备
         */
        @TargetApi(18)
        public File[] getSDCardPaths_18() {
            try {
//                Log.e(TAG, "getExternalStorageDirectory=" +
//                        getExternalStorageDirectory().getPath());
                Class class_StorageManager = StorageManager.class;
                Method method_getVolumeList = class_StorageManager.getMethod("getVolumeList");
                Method method_getVolumeState = class_StorageManager
                        .getMethod("getVolumeState", String.class);
                StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                Class class_StorageVolume = Class.forName("android.os.storage.StorageVolume");
                Method method_isRemovable = class_StorageVolume.getMethod("isRemovable");
                Method method_getPath = class_StorageVolume.getMethod("getPath");
                Method method_getPathFile = class_StorageVolume.getMethod("getPathFile");
                Object[] objArray = (Object[]) method_getVolumeList.invoke(sm);
//                Log.e(TAG, "objArray[].length=" + objArray.length + "---根据是否可以移除来判断是否为外置存储卡。");
                List<File> fileList = new ArrayList<>();
                for (Object value : objArray) {
                    String path = (String) method_getPath.invoke(value);
                    boolean isRemovable = (boolean) method_isRemovable.invoke(value);
                    String getVolumeState = (String) method_getVolumeState.invoke(sm, path);//获取挂载状态。
//                    Log.e(TAG, "存储路径：" + path + "---isRemovable:" + isRemovable +
//                            "----getVolumeState:" + getVolumeState);
                    fileList.add((File) method_getPathFile.invoke(value));
                }
                File[] files = (File[]) fileList.toArray();
                return files;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
         *
         * @return 获取到的存储设备
         */
        @TargetApi(17)
        public File[] getSDCardPaths_17() {
            return getSDCardPaths_18();
        }

        /**
         * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
         * <p/>
         * api16在StorageVolume方法中没有getPathFile
         *
         * @return 获取到的存储设备
         */
        @TargetApi(16)
        public File[] getSDCardPaths_16() {
            try {
//                Log.e(TAG, "getExternalStorageDirectory=" +
//                        getExternalStorageDirectory().getPath());
                Class class_StorageManager = StorageManager.class;
                Method method_getVolumeList = class_StorageManager.getMethod("getVolumeList");
                Method method_getVolumeState = class_StorageManager
                        .getMethod("getVolumeState", String.class);
                StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                Class class_StorageVolume = Class.forName("android.os.storage.StorageVolume");
                Method method_isRemovable = class_StorageVolume.getMethod("isRemovable");
                Method method_getPath = class_StorageVolume.getMethod("getPath");
                Object[] objArray = (Object[]) method_getVolumeList.invoke(sm);
//                Log.e(TAG, "objArray[].length=" + objArray.length + "---根据是否可以移除来判断是否为外置存储卡。");
                List<File> fileList = new ArrayList<>();
                for (Object value : objArray) {
                    String path = (String) method_getPath.invoke(value);
                    boolean isRemovable = (boolean) method_isRemovable.invoke(value);
                    String getVolumeState = (String) method_getVolumeState.invoke(sm, path);//获取挂载状态。
//                    Log.e(TAG, "存储路径：" + path + "---isRemovable:" + isRemovable +
//                            "----getVolumeState:" + getVolumeState);
                    fileList.add(new File((String) method_getPath.invoke(value)));
                }
                File[] files = (File[]) fileList.toArray();
                return files;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
         *
         * @return 获取到的存储设备
         */
        @TargetApi(15)
        public File[] getSDCardPaths_15() {
            return getSDCardPaths_16();
        }

        /**
         * 如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
         *
         * @return 获取到的存储设备
         */
        @TargetApi(14)
        public File[] getSDCardPaths_14() {
            return getSDCardPaths_16();
        }
        //endregion
    }
}
