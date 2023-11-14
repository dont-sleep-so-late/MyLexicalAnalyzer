package experiment_01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class experiment1 {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\DIO\\Desktop\\大学\\大三上\\编译原理\\实验\\测试文件\\C语言代码\\if语句.txt"; // 输入的源代码文件路径
        String outputFilePath = "C:\\Users\\DIO\\Desktop\\大学\\大三上\\编译原理\\实验\\测试文件\\C语言代码\\if语句_out.txt"; // 输出的源代码文件路径

        try {
            // 打开输入文件
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));

            // 创建输出文件
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            String line;
            boolean insideBlockComment = false;
            boolean insideQuotes = false;
            int lineNumber = 1; // 记录行号
            int preprocessDirectiveCount = 0; // 预处理指令计数

            // 逐行读取输入文件并处理注释和源代码
            while ((line = reader.readLine()) != null) {

                if (!insideBlockComment && !insideQuotes) {
                    // 删除行注释
                    int commentIndex = line.indexOf("//");
                    if (commentIndex >= 0) {
                        line = line.substring(0, commentIndex);
                    }

                    // 处理块注释的开始部分
                    int blockCommentStartIndex = line.indexOf("/*");
                    if (blockCommentStartIndex >= 0) {
                        int blockCommentEndIndex = line.indexOf("*/", blockCommentStartIndex + 2); // 查找块注释的结束部分
                        if (blockCommentEndIndex >= 0) {
                            // 删除整个块注释
                            line = line.substring(0, blockCommentStartIndex) + line.substring(blockCommentEndIndex + 2);
                        } else {
                            // 删除块注释的开始部分，并进入块注释状态
                            line = line.substring(0, blockCommentStartIndex);
                            insideBlockComment = true;
                        }
                    }
                } else if (insideQuotes) {
                    // 如果处于引号内部，则不执行注释删除操作
                    int quoteIndex = line.indexOf("\"");
                    if (quoteIndex >= 0) {
                        insideQuotes = false;
                    }
                } else {
                    // 处理块注释的结束部分
                    int blockCommentEndIndex = line.indexOf("*/");
                    if (blockCommentEndIndex >= 0) {
                        // 删除块注释的结束部分，并退出块注释状态
                        line = line.substring(blockCommentEndIndex + 2);
                        insideBlockComment = false;
                    } else {
                        // 删除整行内容
                        line = "";
                    }
                }

                // 记录预处理指令行号和个数
                if (line.startsWith("#")) {
                    preprocessDirectiveCount++;
                    System.out.println("预处理指令在第 " + lineNumber + "行: " + line);
                }

                // 写入处理后的行到输出文件
                writer.write(line);
                writer.newLine();

                // 检查引号内容
                int quoteIndex = line.indexOf("\"");
                while (quoteIndex >= 0) {
                    insideQuotes = !insideQuotes;
                    quoteIndex = line.indexOf("\"", quoteIndex + 1);
                }

                lineNumber++;
            }

            // 输出预处理指令个数到控制台
            System.out.println("预处理指令个数为: " + preprocessDirectiveCount);

            // 关闭文件
            reader.close();
            writer.close();

            System.out.println("注释已成功删除并保存到文件: " + outputFilePath);
        } catch (IOException e) {
            System.out.println("处理文件出错: " + e.getMessage());
        }
    }
}