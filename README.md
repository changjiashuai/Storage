# Storage

>提供Android`内部存储`和`外部存储`的API

### Adding to your project

> Add the jcenter repository information in your build.gradle file like this

```
repositories {
  jcenter()
}


dependencies {
compile 'io.github.changjiashuai:Storage:1.0.0'
}
```

### InternalStorage

    /**
    * 获取在其中存储内部文件的文件系统目录的绝对路径。
    *
    * @return /data/data/包名/files
    */
    public File getFilesDir();

    /**
    * @return 返回您的应用当前保存的一系列文件
    */
    public String[] getFileList();

    /**
    * @param name 文件名
    * @return 删除保存在内部存储的文件。
    */
    public boolean deleteFile(String name);

    /**
    * 向指定的文件中写入指定的数据
    *
    * @param name    文件名
    * @param content 文件内容
    *
    */
    public void writeFileData(String name, String content, int mode);

    /**
    * 打开指定文件，读取其数据，返回字符串对象
    */
    public String readFileData(String fileName);

    /**
    * @param name 文件名
    * @return 在您的内部存储空间内创建（或打开现有的）目录。
    * @see #writeFileData(String, String, int)
    */
    public File getDir(String name, int mode);

    /**
    * 将临时缓存文件保存到的内部目录。
    *
    * @return /data/data/包名/cache
    */
    public File getCacheDir();

    /**
    * dir: data|user/0
    *
    * @return /data/{dir}/包名
    */
    public File getDataDir();


### ExternalStorage

    /**
    * 判断外部设置是否为模拟的
    */
    public boolean isEmulated();

    /**
    * 判断外部设置是否可以移除
    */
    public boolean isRemovable();

    public String getStorageState();

    /* 检测虚拟外部存储是否可读可写*/
    public boolean isStorageWritable();

    /* 检测虚拟外部存储是否可读*/
    public boolean isStorageReadable();

    /**
    * @return /storage/emulated/0/Android/data/包名/cache
    */
    public File getCacheDir();

    public File[] getCacheDirs();

    /**
    * 保存应用私有文件
    *
    * 如果您不需要特定的媒体目录，
    * 请传递 null 以接收应用私有目录的根目录。
    *
    * /storage/emulated/0/Android/data/包名/files/{type}
    *
    */
    public File getFilesDir(String type);

    public File[] getFilesDirs(String type);

    /**
    * @return 保存可与其他应用共享的文件
    */
    public File getStoragePublicDirectory(String type);

    /**
    * @param type 目录类型
    * @param name 目录名称
    * @return 在公共目录中创建了一个指定名称的目录：
    */
    public File getStoragePublicDirWithName(String type, String name);


    /**
    * @return  /storage/emulated/0/Android/data/包名
    */
    public File getDataDir();

### Storage:

    /**
     * 清除缓存：
     *
     * 1. 将内部数据下的cache包下的内容（/data/data/包名/cache/XXX）清除 。
     *
     * 2. 将外部私有数据下的cache包（/storage/emulated/0/Android/data/包名/cache）清除，
     */
    public void clearCache();

    /**
     * 清除数据：
     *
     * 1. 将内部数据下的所有内容（/data/data/包名/XXX）清除；
     *
     * 2. 将外部私有数据包（/storage/emulated/0/Android/data/包名）清除，
     */
    public void clearData();

    /**
     * 获取手机内部剩余存储空间
     */
    public long getRemainInternalStorageSize();

    /**
     * 获取手机内部剩余虚拟外部存储空间
     */
    public long getRemainExternalStorageSize();

    /**
     * 获取手机内部总的存储空间
     */
    public long getTotalInternalStorageSize();

    /**
     * 获取手机内部总的虚拟外部存储空间
     */
    public long getTotalExternalStorageSize();