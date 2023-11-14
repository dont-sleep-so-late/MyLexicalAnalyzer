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
        //��ӹؼ����б�
        keywordlist.addAll(Arrays.asList(Keyword.keywords));
        //��ӷ����б�
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

    public boolean isInteger(String str) {      //�ж��Ƿ�Ϊ����
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public boolean isConsChar(String str) {       //�ж��Ƿ�Ϊ�ַ�����
        return str.startsWith("\"")
                && str.endsWith("\"")
                && str.length() > 1
                && countSpecificSign(str, '\"') == 2;
    }

    public boolean isMarker(String str) {
        //<��ʶ��> �� <��ĸ>��_ <��ĸ>��_ <����>��<��ʶ��> <����>��<��ʶ��> <��ĸ>
        Pattern pattern = Pattern.compile("^[a-zA-Z]*[0-9]*$|^[a-zA-Z]*$|^_[a-zA-Z]*$|^_[a-zA-Z]*[0-9]*$");       //����֤
        return pattern.matcher(str).matches();
    }

    public Vector<String> dispart() {         //��ⵥ��
        //�������
        //1������Ƿ��ַ�
        int line = 1;
        for (int i = 0; i < content.length(); i++) {
            char curchar = content.charAt(i);
            //������
            if (i < content.length() - 1 && content.substring(i, i+2).equals("\r\n")) {
                line++;
                i++;
                continue;
            }
            if (curchar == '\r' || curchar == '\n') {
                line++;
                continue;
            }
            //����ո�
            if (curchar == ' ' || curchar == '\t')
                continue;
            //����ؼ���
            if (Keyword.isKeyword(String.valueOf(curchar)))
                continue;
            //�������
            if (Sign.isSign(String.valueOf(curchar)))
                continue;
            //���������
            if (Operator.isOperator(String.valueOf(curchar)))
                continue;
            //�����ʶ��
            if (String.valueOf(curchar).matches("^[a-zA-Z]*[0-9]*$|^[a-zA-Z]*$|^_[a-zA-Z]*$|^_[a-zA-Z]*[0-9]*$"))
                continue;
            //��������
            if (String.valueOf(curchar).matches("[0-9]"))
                continue;
            //������ĸ
            if (String.valueOf(curchar).matches("[a-zA-Z]"))
                continue;
            //��������
            if (curchar == '\'')
                continue;
            if (curchar == '\"')
                continue;
            //System.out.println("�Ƿ��ַ���"+String.valueOf(curchar)+"�ڵ�"+line+"��");
        }
        //2�����������������
        line = 1;
        int lastline = 1;
        boolean stack = false;
        for (int i = 0; i < content.length(); i++) {
            char curchar = content.charAt(i);
            //������
            if (i < content.length() - 1 && content.startsWith("\r\n", i)) {
                line++;
                i++;
                continue;
            }
            if (curchar == '\r' || curchar == '\n') {
                line++;
                continue;
            }
            //�������ж�
            if (stack && line != lastline) {     //ȱ�ٵ�����
                System.out.println("ȱ�ٵ����ţ���"+lastline+"��");
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
            //�������
            
            //1������������ַ��ķ���
            if (i < content.length() - 1) {
                if (!flag && Sign.isSign(content.substring(i, i+2))) {
                    q = i;
                    words.add(content.substring(p, q));
                    p = i;
                    q = i+2;
                    words.add(content.substring(p, q));
                    p = i+2;
                    i++;        //����ѭ�����i++����ʵ��i+=2
                    flag = true;
                    continue;
                }
            }
            //2�������һ���ַ��ķ���
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


            //�ҵ����ʽ���λ��
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
            //�ҵ���һ���ʿ�ʼλ��
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
            if (Keyword.isKeyword(temp)) {      //�жϹؼ���
                Result res = new Result("�ؼ���", temp);
                result.add(res);
            } else if (Sign.isSign(temp)) {            //�жϷ���
                Result res = new Result("���", temp);
                result.add(res);
            } else if (Operator.isOperator(temp)) {            //�ж������
                Result res = new Result("�����", temp);
                result.add(res);
            } else if (isInteger(temp)) {           //�ж�����
                Result res = new Result("����", temp);
                result.add(res);
            } else if (isMarker(temp)) {
                Result res = new Result("��ʶ��", temp);
                result.add(res);
            } else if (isConsChar(temp)) {             //�ж��ַ�����
                Result res = new Result("�ַ���", temp);
                result.add(res);
            } else {
                Result res = new Result("δʶ��", temp);
                result.add(res);
            }
        }
        return result;
    }

    public void run() {
        String outputFilePath = "C:\\Users\\DIO\\Desktop\\��ѧ\\������\\����ԭ��\\ʵ��\\ʵ��������ļ�\\JAVA���Դ���\\if���_out.txt"; // ������������ļ�·��
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            Vector<String> words = this.dispart();      //���
            Vector<Result> result = this.analyze(words);        //����
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