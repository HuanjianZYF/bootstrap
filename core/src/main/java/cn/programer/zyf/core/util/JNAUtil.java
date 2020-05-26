package cn.programer.zyf.core.util;

import cn.programer.zyf.core.classloader.FileClassLoader;
import cn.programer.zyf.core.common.Assert;
import cn.programer.zyf.core.entity.GeneratorClassInfo;
import cn.programer.zyf.core.entity.MethodDefined;
import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Library;
import com.sun.jna.Native;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wb-zyf471922
 * @Date 2019/10/22 15:07
 **/
public class JNAUtil {

    /**
     * c类型到java类型的一个映射
     */
    private static Map<String, String> JAVA_TYPE_MAP = new ConcurrentHashMap<>();

    /**
     * dll到library的一个缓存
     */
    private static Map<String, Library> DLL_LIBRARY_MAP = new ConcurrentHashMap<>();

    /**
     * 获取library的一个锁
     */
    private static final Object GET_LIBRARY_LOCK = new Object();

    private static Logger logger = LoggerFactory.getLogger(JNAUtil.class);

    static {
        JAVA_TYPE_MAP.put("int", "I");
        JAVA_TYPE_MAP.put("void", "V");
        JAVA_TYPE_MAP.put("char*", "Ljava/lang/String;");
    }

    /**
     * 执行dll的方法
     * @param dllName dll的名字
     * @param clazz 生成的Class文件的jvm内的Class对象
     * @param methodName 方法名字
     * @param paramClazzs 方法参数类型
     * @param params 方法参数
     * @return
     */
    public static Object invokeDllMethod(String searchDllPath, String dllName, Class<?> clazz, String methodName, Class[] paramClazzs, Object[] params) {

        // jna通过这个系统参数去找dll
        System.setProperty("jna.library.path", searchDllPath);

        // 获取dll实例
        Library library = getLibrary(dllName, clazz);

        // 找到dll的方法
        Method method = null;
        try {
            method = clazz.getMethod(methodName, paramClazzs);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("dll" + dllName + "类" + clazz.getName() + "没有方法" + methodName + "，请检查！");
        }

        Object result = null;
        try {
            result = method.invoke(library, params);
        } catch (Exception e) {
            throw new RuntimeException("动态库执行方法失败,dll:" + dllName + "方法:" + methodName + e.getMessage());
        }

        return result;
    }

    /**
     * 获取library，这里必须要缓存library，不然会导致初始化完一个library，下一次又重新生成，报错没有初始化
     * @param dllName dll的名字
     * @param clazz 类型
     * @return
     */
    private static Library getLibrary(String dllName, Class<?> clazz) {
        synchronized (GET_LIBRARY_LOCK) {
            Library library = DLL_LIBRARY_MAP.get(dllName);
            if (library == null) {
                library = (Library) Native.loadLibrary(dllName, clazz);
            }
            Assert.assertTrue(library != null, "获取dll实例失败！dll名称" + dllName);
            DLL_LIBRARY_MAP.put(dllName, library);
            return library;
        }
    }

    /**
     * 根据dll返回dll加载进jvm的Class
     * @param dllPathAndName dll的相对路径名字 （【这里使用相对路径是因为黑马会被安装在任意目录下面】）
     * @param classFileTargetPath 生成的class文件的相对路径
     * @param dllMethodList dll的方法列表
     * @return 加载出来的class
     */
    public static Class<?> loadDllClass(String dllPathAndName, String classFileTargetPath, List<MethodDefined> dllMethodList) {

        logger.info("【创建class文件】dll路径" + dllPathAndName + "生成的class的目录" + classFileTargetPath + "方法列表" + JSONObject.toJSONString(dllMethodList));

        // 获取目录的名字，和文件的名字
        String[] pathAndName = getDirAndFileName(dllPathAndName);
        String dirName = pathAndName[0];
        String fileName = pathAndName[1];

        // 获取生成的类名信息
        GeneratorClassInfo info = JNAUtil.generateDllClass(dirName, fileName, classFileTargetPath, dllMethodList);
        logger.info("生成类名返回的信息：" + JSONObject.toJSONString(info));

        // 类加载器
        FileClassLoader classLoader = new FileClassLoader(JNAUtil.class.getClassLoader());

        // 类文件的全路径
        String classFileFullName = classFileTargetPath + "/" + info.getClassFileName();

        // 加载类
        Class<?> result = null;
        try {
            result = classLoader.loadClass(classFileFullName, info.getFullClassName());
        } catch (Exception e) {
            throw new RuntimeException("无法加载类文件！类文件路径：" + dirName + " " + fileName + "类名：" + info.getFullClassName() + e.getMessage());
        }
        return result;
    }

    /**
     * 根据相对路径获取绝对路径的目录和文件名
     * @param dllPathAndName 相对路径
     * @return
     */
    private static String[] getDirAndFileName(String dllPathAndName) {
        String[] result = new String[2];

        File file = new File(dllPathAndName);
        // 文件名
        String fileName = file.getName();
        // 绝对路径名
        String absolutePath = file.getAbsolutePath();
        // 目录名
        int index = absolutePath.indexOf(fileName);
        String dirName = absolutePath.substring(0, index);

        result[0] = dirName;
        result[1] = fileName;
        return result;
    }

    /**
     * 根据dll路径，dll名字，生成的类文件的路径，dll的方法，生成dll对应的class文件
     * @param dllPath dll的路径
     * @param dllName dll的名字
     * @param classPath 生成的类文件的路径
     * @param methods dll的方法
     * @return 生成的类的信息
     */
    public static GeneratorClassInfo generateDllClass(String dllPath, String dllName, String classPath, List<MethodDefined> methods) {

        GeneratorClassInfo result = new GeneratorClassInfo();

        if (dllPath == null) {
            dllPath = "";
        }
        // 判断生成的class文件的目录是否存在
        if (!existsDir(classPath)) {
            throw new RuntimeException("要生成class文件的目录" + classPath + "不存在！请确认该目录是否正确！");
        }

        // 类文件的名字
        String className = getClassNameByDllName(dllName);
        // 全类名
        String fullClassName = getFullClassName(dllPath, dllName);

        // 判断类文件是否已经存在，已经存在就直接返回，不重新生成
        String classPathAndFileName = assemblePathAndFileName(classPath, className);
        boolean classExist = existsFile(classPathAndFileName);
        if (classExist) {
            result.setFullClassName(fullClassName);
            result.setClassFileName(className);
            return result;
        }

        // 生成字节码文件
        byte[] bytes = makeClassFileBytes(methods, fullClassName);

        // 写入目录
        saveToDist(bytes, classPath, className);

        result.setFullClassName(fullClassName);
        result.setClassFileName(className);
        return result;
    }

    /**
     * 根据dll的路径和dll的名字获取全类名
     * @param dllPath dll的路径
     * @param dllName dll的名字
     * @return 全类名
     */
    private static String getFullClassName(String dllPath, String dllName) {
        // dll文件的全路径名
        String pathAndFileName = assemblePathAndFileName(dllPath, dllName);
        // 包名
        String packageName = convertDir2PackageName(pathAndFileName);
        // 全类名
        return packageName + "." + dllName;
    }

    /**
     * 将类文件写入磁盘
     * @param bytes 字节
     * @param filePath 文件路径
     * @param fileName 文件名字
     */
    private static void saveToDist(byte[] bytes, String filePath, String fileName) {

        File file = new File(filePath, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (Exception e) {
            throw new RuntimeException("生成的class文件，写入磁盘失败！" + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    throw new RuntimeException("写入class文件时，关闭文件符号失败！" + e.getMessage());
                }
            }
        }
    }

    /**
     * 生成class文件的字节
     * @param methods 方法列表
     * @param packageName 包名
     * @return 类文件的字节
     */
    private static byte[] makeClassFileBytes(List<MethodDefined> methods, String packageName) {
        // 写类文件的writer
        ClassWriter writer = new ClassWriter(0);

        // 传的包名要把.替换成/
        packageName = packageName.replaceAll("\\.", "/");
        // 类文件的头部信息，定义了一个接口（不明觉厉）
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
            packageName, null, "java/lang/Object", new String[]{"com/sun/jna/Library"});

        // 类文件里面的方法
        for (MethodDefined method : methods) {

            // 类文件里的方法描述符
            String methodDesc = assembleMethodDesc(method.getMethodTypes(), method.getReturnType());

            // 写入方法
            writer.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, method.getMethodName(),
                methodDesc, null, null);
        }

        // 生成
        writer.visitEnd();

        return writer.toByteArray();
    }

    /**
     * 将目录文件名字转换成包名
     * @param dirFileName
     * @return
     */
    private static String convertDir2PackageName(String dirFileName) {
        String result = dirFileName;

        int index1 = result.indexOf(":");
        if (index1 >= 0) {
            result = result.substring(index1 + 1);
        }

        int index = result.indexOf(".");
        if (index == -1) {
            result = result.replaceAll("\\\\", ".");
        } else {
            result = result.replaceAll("\\\\", ".").substring(0, index);
        }

        if (result.startsWith(".")) {
            result = result.substring(1);
        }

        return result;
    }

    /**
     * 生成java类文件里面的方法描述符
     * @param paramTypes 入参类型
     * @param returnType 返回类型
     * @return 类文件的方法描述符
     */
    private static String assembleMethodDesc(String[] paramTypes, String returnType) {
        StringBuilder sb = new StringBuilder();

        sb.append("(");
        for (String type : paramTypes) {
            String classType = JAVA_TYPE_MAP.get(type);
            if (StringUtils.isEmpty(classType)) {
                classType = type;
            }
            sb.append(classType);
        }
        sb.append(")");

        String returnStr = JAVA_TYPE_MAP.get(returnType);
        if (StringUtils.isEmpty(returnStr)) {
            returnStr = returnType;
        }
        sb.append(returnStr);

        return sb.toString();
    }

    /**
     * 组装路径和文件名
     * @param path 路径
     * @param fileName 文件名
     * @return 文件全名
     */
    private static String assemblePathAndFileName(String path, String fileName) {
        boolean endWithSeparator = path.endsWith(File.separator);
        if (endWithSeparator) {
            return path + fileName;
        } else {
            return path + File.separator + fileName;
        }
    }

    /**
     * 判断文件是否存在
     * @param fileName 文件名字
     * @return 是否存在
     */
    private static boolean existsFile(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 判断目录是否存在
     * @param dir 目录名字
     * @return 是否存在
     */
    private static boolean existsDir(String dir) {
        File file = new File(dir);
        return file.exists() && file.isDirectory();
    }

    /**
     * 根据dllName获取ClassName
     * @param dllName dll的名字
     * @return className
     */
    private static String getClassNameByDllName(String dllName) {
        int index = dllName.lastIndexOf(".");
        if (index == -1) {
            return dllName + ".class";
        } else {
            return dllName.substring(0, index) + ".class";
        }
    }

}
