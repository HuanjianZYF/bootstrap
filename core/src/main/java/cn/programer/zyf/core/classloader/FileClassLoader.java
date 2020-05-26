package cn.programer.zyf.core.classloader;

import java.io.File;
import java.io.FileInputStream;

/**
 * 一个读磁盘上面class文件的类加载器
 *
 * @Author wb-zyf471922
 * @Date 2019/10/23 9:25
 **/
public class FileClassLoader extends ClassLoader {

    /**
     * 构造方法，指定类加载器的父类
     * @param parent
     */
    public FileClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * 加载类，这里暂时直接方法带锁（怕出现并发问题，牺牲一些性能），因为上层对Class做了缓存
     *
     * @param fullFileName 类文件的全路径+名字
     * @param className 类名(带包名) 也就是所谓的binary name
     * @return 加载进来的Class类
     * @throws Exception
     */
    public synchronized Class<?> loadClass(String fullFileName, String className) throws Exception {
        File file = new File(fullFileName);
        if (!file.exists()) {
            throw new RuntimeException("要加载的类文件" + fullFileName + "不存在！请检查");
        }

        // 如果已经加载进来了，直接返回
        Class clazz = findLoadedClass(className);
        if (clazz != null) {
            return clazz;
        }

        // 获取文件的字节
        byte[] bytes = getFileBytes(file);

        clazz = defineClass(className, bytes, 0, bytes.length);
        resolveClass(clazz);

        return clazz;
    }

    /**
     * 获取文件的字节流
     * @param file 文件
     * @return 字节流
     */
    private byte[] getFileBytes(File file) throws Exception {
        FileInputStream in = new FileInputStream(file);

        if (file.length() > Integer.MAX_VALUE) {
            throw new RuntimeException("类文件太大，大小超过最大的整形，无法加载！");
        }
        byte[] result = new byte[(int)file.length()];
        in.read(result);

        return result;
    }
}
