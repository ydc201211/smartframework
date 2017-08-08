package com.ydc.framework.util;

import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);
    /** 获取类加载器**/
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取该包名下的所有类
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){
        /**
         * 1. 获取指定包名下的的所有类
         * 2. 根据包名将其转换为文件路径
         * 3. 读取class文件或jar包
         * 4. 获取指定的类名去加载类
         */
        Set<Class<?>> classSet = new HashSet<>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                String protocol = url.getProtocol(); //获取此 URL 的协议名称。
                if(protocol.equals("file")){
                    // %20 表示file协议?
                    String packagePath = url.getPath().replaceAll("%20", "");
                    addClass(classSet,packagePath,packageName);
                }else if(protocol.equals("jar")){
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if(jarURLConnection != null){
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if(jarFile != null){
                            Enumeration<JarEntry> jarEntries =jarFile.entries();
                            while(jarEntries.hasMoreElements()){
                                JarEntry jarEntry= jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if(jarEntryName.endsWith(".class")){
                                    String className = jarEntryName.substring(0,
                                            jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                    doAddClass(classSet,className);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    /**
     * 如果是文件,就根据包名 和 文件名 组成类的全限定名称,然后 加载类
     * @param classSet
     * @param packagePath 文件(夹)的绝对路径
     * @param packageName 和当前文件(夹) 对应的包名
     */
    public static  void addClass(Set<Class<?>> classSet,String packagePath,String packageName){

        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // 只需要 文件并且是.class的文件,或则是目录 都返回true
                return file.isFile() && file.getName().endsWith(".class") || file.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if(file.isFile()){ // 是指定的文件 就获取到全限定类名 然后装载它
                String className = fileName.substring(0, fileName.lastIndexOf(".")); // 把.class后最截取掉
                if(StringUtils.isNotBlank(packageName)){
                    className = packageName + "." + className; // 根据包名 + 文件名 得到这个类的全限定名称,
                }
                doAddClass(classSet,className);
            }else { // 是文件夹就递归自己. 获取 文件夹的绝对路径,和 当前文件夹对应的 限定包名.方便 文件里面直接使用

                String subPackagePath= fileName;
                if(StringUtils.isNotBlank(subPackagePath)){
                    subPackagePath = packagePath + "/" + subPackagePath; // 第一次:由基础包名 得到绝对路径,再加上当前文件夹名称 = 当前文件夹的绝对路径
                }
                subPackagePath = file.getAbsolutePath(); // 该方法获得文件的绝对路径.和上面的代码效果是一致的
                String subPackageName = fileName;
                if(StringUtils.isNotBlank(subPackageName)){
                    subPackageName = packageName + "." + subPackageName; // 第一次: 基础包名 加文件夹名称 组合成 当前包名 +
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }

    /**
     * 加载类,并把该类对象 添加到集合中
     * @param classSet
     * @param className
     */
    public static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    /**
     * 加载类
     * @param className 所需类的完全限定名
     * @param isInitialized 是否执行类的静态代码块(api:参数为 true 且以前未被初始化时，才初始化该类,
     *                      作者说:设置为false可以提高性能.具体原因是什么没有说.我也不知道,这个参数确实  不知道是什么意思)
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInitialized){

        Class<?> aClass = null;
        try {
            aClass = Class.forName(className, isInitialized,getClassLoader());  // 该方法类加载器有值的时候调用的是原生方法
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure",e);
            throw new RuntimeException(e);
        }
        return aClass;
    }

    public static void main(String[] args) {
        getClassSet("mr.code");
    }
}
