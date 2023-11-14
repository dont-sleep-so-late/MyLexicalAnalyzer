package experiment_02;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

public class AnalyzeTool implements Runnable {
    private static Vector<String> keywordlist;
    private String content;

    public AnalyzeTool(String content) {
        this.content = content;
        keywordlist = new Vector<String>();
        //添加关键字列表
        keywordlist.addAll(Arrays.asList(Keyword.keywords));
        //添加符号列表
        keywordlist.addAll(Arrays.asList(Sign.signs));
    }

    public int countSpecificSign(String str, char sign) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == sign)
                count++;
        }
        return count;
    }

    public boolean isInteger(String str) {      //判断是否为整数
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public boolean isConsChar(String str) {       //判断是否为字符常数
        return str.startsWith("\"")
                && str.endsWith("\"")
                && str.length() > 1
                && countSpecificSign(str, '\"') == 2;
    }

    public boolean isMarker(String str) {
        //<标识符> → <字母>│_ <字母>│_ <数字>│<标识符> <数字>│<标识符> <字母>
        Pattern pattern = Pattern.compile("^[a-zA-Z]*[0-9]*$|^[a-zA-Z]*$|^_[a-zA-Z]*$|^_[a-zA-Z]*[0-9]*$");       //待验证
        return pattern.matcher(str).matches();
    }

    public Vector<String> dispart() {         //拆解单词
        //处理错误
        //1、处理非法字符
        int line = 1;
        for (int i = 0; i < content.length(); i++) {
            char curchar = content.charAt(i);
            //处理换行
            if (i < content.length() - 1 && content.substring(i, i+2).equals("\r\n")) {
                line++;
                i++;
                continue;
            }
            if (curchar == '\r' || curchar == '\n') {
                line++;
                continue;
            }
            //处理空格
            if (curchar == ' ' || curchar == '\t')
                continue;
            //处理关键字
            if (Keyword.isKeyword(String.valueOf(curchar)))
                continue;
            //处理符号
            if (Sign.isSign(String.valueOf(curchar)))
                continue;
            //处理操作符
            if (Operator.isOperator(String.valueOf(curchar)))
                continue;
            //处理标识符
            if (String.valueOf(curchar).matches("^[a-zA-Z]*[0-9]*$|^[a-zA-Z]*$|^_[a-zA-Z]*$|^_[a-zA-Z]*[0-9]*$"))
                continue;
            //处理数字
            if (String.valueOf(curchar).matches("[0-9]"))
                continue;
            //处理字母
            if (String.valueOf(curchar).matches("[a-zA-Z]"))
                continue;
            //处理引号
            if (curchar == '\'')
                continue;
            if (curchar == '\"')
                continue;
            //System.out.println("非法字符！"+String.valueOf(curchar)+"在第"+line+"行");
        }
        //2、处理单引号配对问题
        line = 1;
        int lastline = 1;
        boolean stack = false;
        for (int i = 0; i < content.length(); i++) {
            char curchar = content.charAt(i);
            //处理换行
            if (i < content.length() - 1 && content.startsWith("\r\n", i)) {
                line++;
                i++;
                continue;
            }
            if (curchar == '\r' || curchar == '\n') {
                line++;
                continue;
            }
            //单引号判断
            if (stack && line != lastline) {     //缺少单引号
                System.out.println("缺少单引号！在"+lastline+"行");
                stack = false;
            }
            if (stack && curchar == '\'') {
                stack = false;
                continue;
            }
            if (!stack && curchar == '\'') {
                lastline = line;
                stack = true;
                continue;
            }
        }

        Vector<String> words = new Vector<String>();
        int p = 0, q = 0;
        boolean flag = false;
        for (int i = 0; i < content.length(); i++) {
            //System.out.println(content.charAt(i));
            //处理符号
            
            //1、处理带两个字符的符号
            if (i < content.length() - 1) {
                if (!flag && Sign.isSign(content.substring(i, i+2))) {
                    q = i;
                    words.add(content.substring(p, q));
                    p = i;
                    q = i+2;
                    words.add(content.substring(p, q));
                    p = i+2;
                    i++;        //加上循环里的i++，其实是i+=2
                    flag = true;
                    continue;
                }
            }
            //2、处理带一个字符的符号
            if (i < content.length()) {
                if (!flag && Sign.isSign(content.substring(i, i+1))) {
                    q = i;
                    words.add(content.substring(p, q));
                    p = i;
                    q = i+1;
                    words.add(content.substring(p, q));
                    p = i+1;
                    flag = true;
                    continue;
                }
            }


            //找到单词结束位置
            if (!flag && String.valueOf(content.charAt(i)).matches("\\s")) {
                q = i;
                flag = true;
                words.add(content.substring(p, q));
                continue;
            } else if (!flag && i == content.length() - 1) {
                q = i;
                words.add(content.substring(p));
                continue;
            }
            //找到下一单词开始位置
            if (flag && !String.valueOf(content.charAt(i)).matches("\\s")) {
                p = i;
                flag = false;
                continue;
            }
        }
        return words;
    }

    public Vector<Result> analyze(Vector<String> words) {
        Vector<Result> result = new Vector<Result>();
        for (String temp : words) {
            if (Keyword.isKeyword(temp)) {      //判断关键字
                Result res = new Result("关键字", temp);
                result.add(res);
            } else if (Sign.isSign(temp)) {            //判断符号
                Result res = new Result("界符", temp);
                result.add(res);
            } else if (Operator.isOperator(temp)) {            //判断运算符
                Result res = new Result("运算符", temp);
                result.add(res);
            } else if (isInteger(temp)) {           //判断整数
                Result res = new Result("常数", temp);
                result.add(res);
            } else if (isMarker(temp)) {
                Result res = new Result("标识符", temp);
                result.add(res);
            } else if (isConsChar(temp)) {             //判断字符常数
                Result res = new Result("字符串", temp);
                result.add(res);
            } else {
                Result res = new Result("未识别", temp);
                result.add(res);
            }
        }
        return result;
    }

    public void run() {
        String outputFilePath = "C:\\Users\\DIO\\Desktop\\大学\\大三上\\编译原理\\实验\\实验二测试文件\\JAVA语言代码\\if语句_out.txt"; // 输出的属性字文件路径
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            Vector<String> words = this.dispart();      //拆解
            Vector<Result> result = this.analyze(words);        //分析
            for (Result temp : result) {
                System.out.println(temp);
                writer.write(String.valueOf(temp));
                writer.newLine();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}