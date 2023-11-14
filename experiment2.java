import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class experiment2 {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\DIO\\Desktop\\大学\\大三上\\编译原理\\实验\\实验二测试文件\\JAVA语言代码\\if语句.txt"; // 输入的源程序文件路径
        String outputFilePath = "C:\\Users\\DIO\\Desktop\\大学\\大三上\\编译原理\\实验\\实验二测试文件\\JAVA语言代码\\if语句_out.txt"; // 输出的属性字文件路径

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String s;
            while ((s = reader.readLine()) != null) {
                String line = prepareDeal(s);

                StringBuilder buffer = new StringBuilder();
                boolean inString = false; // 标记是否在字符串中

                for (char currentChar : line.toCharArray()) {
                    if (Character.isWhitespace(currentChar) && !inString) {
                        if (!buffer.isEmpty()) {
                            String word = buffer.toString();
                            analyzeToken(word, writer);
                            buffer.setLength(0); // 清空缓冲区
                        }
                    } else {
                        buffer.append(currentChar);
                        if (currentChar == '\"') {
                            inString = !inString; // 切换字符串标记
                        }
                    }
                }

                if (!buffer.isEmpty()) {
                    String word = buffer.toString();
                    analyzeToken(word, writer);
                }
            }

            System.out.println("词法分析完成，结果保存在 " + outputFilePath);
        } catch (IOException e) {
            System.out.println("处理源程序时出错: " + e.getMessage());
        }
    }
    public static String prepareDeal(String str){
        if(str.length()==0) return "";
        int i  = 0;
        str = str.trim();

        // 利用正则表达式用一个空格替换多个空格
        String s1 = str.substring(i, str.length()).replaceAll("\\s+", " ").trim();
        return s1;
    }
    private static void analyzeToken(String word, BufferedWriter writer) throws IOException {
        // 关键字集合
        Set<String> keywords = new HashSet<>();
        keywords.add("if");
        keywords.add("else");
        keywords.add("while");
        keywords.add("for");
        keywords.add("int");
        keywords.add("float");
        keywords.add("printf");

        // 运算符集合
        Set<String> operators = new HashSet<>();
        operators.add("+");
        operators.add("-");
        operators.add("*");
        operators.add("/");
        operators.add("=");
        operators.add("==");
        operators.add("<");
        operators.add(">");
        operators.add("<>"); // 添加运算符<>

        // 界符集合
        Set<String> delimiters = new HashSet<>();
        delimiters.add("(");
        delimiters.add(")");
        delimiters.add("{");
        delimiters.add("}");
        delimiters.add(";");
        delimiters.add(",");
        delimiters.add(".");
        delimiters.add("\"");

        // 判断单词的类型并写入属性字
        if (keywords.contains(word)) {
            writer.write("<关键字, " + word + ">");
            writer.newLine();
        } else if (operators.contains(word)) {
            writer.write("<运算符, " + word + ">");
            writer.newLine();
        } else if (delimiters.contains(word)) {
            writer.write("<界符, " + word + ">");
            writer.newLine();
        } else if (isIdentifier(word)) {
            writer.write("<标识符, " + word + ">");
            writer.newLine();
        } else if (isNumber(word)) {
            writer.write("<常数, " + word + ">");
            writer.newLine();
        } else {
            writer.write("<未识别, " + word + ">");
            writer.newLine();
        }
    }

    private static boolean isIdentifier(String word) {
        // 判断标识符的规则（此处仅作示例，规则可以根据实际情况进行扩展）
        return word.matches("[a-zA-Z][a-zA-Z0-9]*");
    }


    private static boolean isNumber(String word) {
        // 判断常数的规则
        return word.matches("\\d+") || word.matches("\\d+\\.\\d+");
    }
}