package typing.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.separator;

public class CodeFilesName {
    private static List<String> codeFilesName = new ArrayList<>();

    public static void ergodic(String path) {
        codeFilesName.clear();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        ergodic(file2.getAbsolutePath());
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                        String filename = file2.getAbsolutePath();
                        filename = filename.substring(filename.lastIndexOf("/")+1,filename.lastIndexOf("."));
                        System.out.println(filename);
                        codeFilesName.add(filename);
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    public static List<String> getCodeFilesName() {
        String  path = "编码文件"+separator+"输入法编码"+separator;
        ergodic(path);
        return codeFilesName;
    }
}
