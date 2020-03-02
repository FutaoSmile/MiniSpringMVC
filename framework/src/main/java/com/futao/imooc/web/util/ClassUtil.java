package com.futao.imooc.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author futao
 * @date 2020/2/28.
 */
public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";
    public static final String JAR_PROTOCOL = "jar";

    public static final String CLASS_STR = "class";

    private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取指定包下的类
     *
     * @param pkg 包名
     * @return
     */
    public static Set<Class<?>> extractPackageClass(String pkg) {
        Enumeration<URL> resources = null;
        Set<Class<?>> classSet = new HashSet<>(0);
        try {
            resources = ClassUtil.getCurrentClassLoader().getResources(pkg.replace(".", "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (resources == null) {
            throw new RuntimeException("当前包[" + pkg + "]下无资源");
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            //文件协议
            String protocol = url.getProtocol();
            log.info("protocol:{},port:{},path:{}", protocol, url.getPort(), url.getPath());
            if (FILE_PROTOCOL.equalsIgnoreCase(protocol)) {
                //pkg对应的文件夹绝对路径
                File pkgDir = new File(url.getPath());
                log.info("pkg对应的文件夹的绝对路径为:{}", url.getPath());
                extractClassFile(classSet, pkgDir, pkg);
            } else if (JAR_PROTOCOL.equalsIgnoreCase(protocol)) {
                try {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    extractJarFile(classSet, jarFile, pkg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("{}", classSet);
        return classSet;
    }

    /**
     * 加载.jar中的class文件 打成jar包 会走这
     *
     * @param classSet
     * @param jarFile
     * @param pkg
     */
    private static void extractJarFile(Set<Class<?>> classSet, JarFile jarFile, String pkg) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            //  com/futao/imooc/Test.class
            String jarEntryName = jarEntry.getName();
            if (jarEntryName.startsWith(pkg.replace(".", "/")) && jarEntryName.endsWith(".class")) {
                //将`/`替换成`.`的形式并且返回类的包路径和类名
                String pkgAndClassName = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace(File.separator, ".");
                try {
                    log.info("从jar文件中读取到class[{}]", pkgAndClassName);
                    classSet.add(Class.forName(pkgAndClassName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 加载   .class文件，通过IDEA直接启动，会走这
     *
     * @param classSet
     * @param pkgDir
     * @param pkg
     */
    private static void extractClassFile(Set<Class<?>> classSet, File pkgDir, String pkg) {
        String path = pkgDir.getPath();
        if (pkgDir.isDirectory()) {
            File[] files = pkgDir.listFiles();
            if (files != null && files.length > 0) {
                for (File childFile : files) {
                    extractClassFile(classSet, childFile, pkg);
                }
            }
            //是文件夹
        } else if (pkgDir.isFile()) {
            //是文件
            log.debug("识别文件:{}", path);
            if (CLASS_STR.equalsIgnoreCase(path.substring(path.lastIndexOf(".") + 1))) {
                try {
                    //返回相对路径  pck/class.class
                    String pkgAndClassSuffix = path.substring(path.indexOf(pkg.replace(".", "/")));
                    //将`/`替换成`.`的形式并且返回类的包路径和类名
                    String pkgAndClassName = pkgAndClassSuffix.substring(0, pkgAndClassSuffix.lastIndexOf(".")).replace(File.separator, ".");
                    classSet.add(Class.forName(pkgAndClassName));
                } catch (ClassNotFoundException e) {
                    log.error("通过类路径[{}]反射获取class失败", path, e);
                }
            }
        } else {
            //无法识别
            log.warn("无法识别文件[{}]类型", path);
        }
    }

    /**
     * 从当前线程中获取ClassLoader
     *
     * @return
     */
    private static ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
