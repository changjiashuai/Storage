# Storage

>提供Android`内部存储`和`外部存储`的API

 [ ![Download](https://api.bintray.com/packages/changjiashuai/maven/storage/images/download.svg?version=1.1.0) ](https://bintray.com/changjiashuai/maven/storage/1.1.0/link)
 
### Adding to your project

> Add the jcenter repository information in your build.gradle file like this

```

dependencies {
	compile 'io.github.changjiashuai:storage:{releaseVersion}'
}

```

### InternalStorage

    /**
    * 获取在其中存储内部文件的文件系统目录的绝对路径。
    *
    * @return /data/data/包名/files
    */
     public static File getFilesDir(Context context);

    /**
    * @return 返回您的应用当前保存的一系列文件
    */
    public static String[] getFileList(Context context);

    /**
    * @param name 文件名
    * @return 删除保存在内部存储的文件。
    */
    public static boolean deleteFile(Context context, String name);

    /**
    * 向指定的文件中写入指定的数据
    *
    * @param name    文件名
    * @param content 文件内容
    *
    */
    public static void writeFileData(Context context, String name, String content, int mode);

    /**
    * 打开指定文件，读取其数据，返回字符串对象
    */
    public static String readFileData(Context context, String fileName);

    /**
    * @param name 文件名
    * @return 在您的内部存储空间内创建（或打开现有的）目录。
    * @see #writeFileData(Context, String, String, int)
    */
    public static File getDir(Context context, String name, int mode);

    /**
    * 将临时缓存文件保存到的内部目录。
    *
    * @return /data/data/包名/cache
    */
    public static File getCacheDir(Context context);

    /**
    * dir: data|user/0
    *
    * @return /data/{dir}/包名
    */
    public static File getDataDir(Context context);


### ExternalStorage

    /**
    * 判断外部设置是否为模拟的
    */
    public static boolean isEmulated();

    /**
    * 判断外部设置是否可以移除
    */
    public static boolean isRemovable();

    public static String getStorageState();

    /* 检测虚拟外部存储是否可读可写*/
    public static boolean isStorageWritable();

    /* 检测虚拟外部存储是否可读*/
    public static boolean isStorageReadable();

    /**
    * @return /storage/emulated/0/Android/data/包名/cache
    */
    public static File getCacheDir(Context context);

    public static String getCacheDirPath(Context context);
    
    public static File[] getCacheDirs(Context context);

    /**
    * 保存应用私有文件
    *
    * 如果您不需要特定的媒体目录，
    * 请传递 null 以接收应用私有目录的根目录。
    *
    * /storage/emulated/0/Android/data/包名/files/{type}
    *
    */
    public static File getFilesDir(Context context, String type);
    public static File createDirInFilesDir(Context context, String type, String name);
    public static String getFilesDirPath(Context context, String type);
    public static File[] getFilesDirs(Context context, String type);

    /**
    * @return 保存可与其他应用共享的文件
    */
    public static File getStoragePublicDir(String type);
    public static File createDirInStoragePublicDir(String type, String name);
	public static String getStoragePublicDirPath(String type);

    /**
    * @return  /storage/emulated/0/Android/data/包名
    */
    public static File getDataPkgDir(Context context);
    public static File createDirInDataPkgDir(Context context, String name);
    public static String getDataPkgDirPath(Context context);

### Storage:

	public static long getDirSize(File dir);
	public static long getCacheSize(Context context);
	public static long getDataSize(Context context);
	
    /**
     * 清除缓存：
     *
     * 1. 将内部数据下的cache包下的内容（/data/data/包名/cache/XXX）清除 。
     *
     * 2. 将外部私有数据下的cache包（/storage/emulated/0/Android/data/包名/cache）清除，
     */
    public static void clearCache(Context context);

    /**
     * 清除数据：
     *
     * 1. 将内部数据下的所有内容（/data/data/包名/XXX）清除；
     *
     * 2. 将外部私有数据包（/storage/emulated/0/Android/data/包名）清除，
     */
    public static void clearData(Context context);
    
    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(File dir, boolean deleteThisPath);

    /**
     * 获取手机内部剩余存储空间
     */
    public static long getRemainInternalStorageSize();

    /**
     * 获取手机内部剩余虚拟外部存储空间
     */
    public static long getRemainExternalStorageSize();

    /**
     * 获取手机内部总的存储空间
     */
    public static long getTotalInternalStorageSize();

    /**
     * 获取手机内部总的虚拟外部存储空间
     */
    public static long getTotalExternalStorageSize();