import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class experiment1_test {
    public static void main(String[] args) {
        BufferedReader inputReader = null;
        BufferedWriter outputWriter = null;

        boolean firstFlag = false;      // 有没有遇到第一个「/」
        boolean secondFlag = false;     // 有没有遇到第二个「/」
        boolean firstStarFlag = false;  // true 的情况是遇到了「/*」
        boolean secondStarFlag = false; // true 的情况是遇到了「/* xxxxx *」
        boolean quotationFlag = false;  // true 是遇到了「"」此时引号内部的注释不可以去掉

        try {
            inputReader = new BufferedReader(new FileReader("C:\\Users\\DIO\\Desktop\\大学\\大三上\\编译原理\\实验\\测试文件\\C语言代码\\if语句.txt")); // input.cpp为任意带注释的C程序
            outputWriter = new BufferedWriter(new FileWriter("C:\\Users\\DIO\\Desktop\\大学\\大三上\\编译原理\\实验\\测试文件\\C语言代码\\if语句_out.txt")); // output.java为去掉注释后的程序

            int c;
            while ((c = inputReader.read()) != -1) {
                char character = (char) c;

                if (character == '/' && !firstFlag && !secondStarFlag && !quotationFlag) // 第一次遇到的「/」不在「"」中
                {
                    firstFlag = true; // 遇到了「/」的情况
                    continue;
                } else if (character == '/' && firstFlag && !quotationFlag) // 遇到「//」的情况
                {
                    secondFlag = true;
                    firstFlag = false;
                    continue;
                } else if (character == '*' && firstFlag && !quotationFlag) // 遇到「/*」的情况
                {
                    firstStarFlag = true;
                    firstFlag = false;
                    continue;
                } else if (character == '"' && !firstFlag && !quotationFlag) {
                    quotationFlag = true; // 遇到第一个「"」
                } else if (character == '"' && quotationFlag) {
                    quotationFlag = false; // 遇到「" xxxx "」的情况
                }

                if (secondFlag) // 遇到「//」的情况
                {
                    if (character != '\n') // 不是回车就跳过
                    {
                        continue;
                    } else {
                        secondFlag = false; // 是回车就跳出判断
                    }
                }

                if (firstStarFlag) // 遇到「/*」的情况
                {
                    if (character != '*' && !secondStarFlag) // 普通字符跳过
                    {
                        continue;
                    } else if (character == '*') // 遇到「/* xxx *」的情况
                    {
                        secondStarFlag = true;
                        continue;
                    }
                    if (secondStarFlag && character == '/') // 遇到了「/* xxx */」表明结束
                    {
                        firstFlag = false;
                        secondFlag = false;
                        firstStarFlag = false;
                        secondStarFlag = false;
                        quotationFlag = false;
                        continue;
                    }
                }

                System.out.print(character);
                outputWriter.write(character);
            }
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } finally {
            try {
                if (inputReader != null)
                    inputReader.close();
                if (outputWriter != null) {
                    outputWriter.flush();
                    outputWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error in closing file: " + e.getMessage());
            }
        }
    }
}