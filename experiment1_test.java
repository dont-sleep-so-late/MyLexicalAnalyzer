import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class experiment1_test {
    public static void main(String[] args) {
        BufferedReader inputReader = null;
        BufferedWriter outputWriter = null;

        boolean firstFlag = false;      // ��û��������һ����/��
        boolean secondFlag = false;     // ��û�������ڶ�����/��
        boolean firstStarFlag = false;  // true ������������ˡ�/*��
        boolean secondStarFlag = false; // true ������������ˡ�/* xxxxx *��
        boolean quotationFlag = false;  // true �������ˡ�"����ʱ�����ڲ���ע�Ͳ�����ȥ��

        try {
            inputReader = new BufferedReader(new FileReader("C:\\Users\\DIO\\Desktop\\��ѧ\\������\\����ԭ��\\ʵ��\\�����ļ�\\C���Դ���\\if���.txt")); // input.cppΪ�����ע�͵�C����
            outputWriter = new BufferedWriter(new FileWriter("C:\\Users\\DIO\\Desktop\\��ѧ\\������\\����ԭ��\\ʵ��\\�����ļ�\\C���Դ���\\if���_out.txt")); // output.javaΪȥ��ע�ͺ�ĳ���

            int c;
            while ((c = inputReader.read()) != -1) {
                char character = (char) c;

                if (character == '/' && !firstFlag && !secondStarFlag && !quotationFlag) // ��һ�������ġ�/�����ڡ�"����
                {
                    firstFlag = true; // �����ˡ�/�������
                    continue;
                } else if (character == '/' && firstFlag && !quotationFlag) // ������//�������
                {
                    secondFlag = true;
                    firstFlag = false;
                    continue;
                } else if (character == '*' && firstFlag && !quotationFlag) // ������/*�������
                {
                    firstStarFlag = true;
                    firstFlag = false;
                    continue;
                } else if (character == '"' && !firstFlag && !quotationFlag) {
                    quotationFlag = true; // ������һ����"��
                } else if (character == '"' && quotationFlag) {
                    quotationFlag = false; // ������" xxxx "�������
                }

                if (secondFlag) // ������//�������
                {
                    if (character != '\n') // ���ǻس�������
                    {
                        continue;
                    } else {
                        secondFlag = false; // �ǻس��������ж�
                    }
                }

                if (firstStarFlag) // ������/*�������
                {
                    if (character != '*' && !secondStarFlag) // ��ͨ�ַ�����
                    {
                        continue;
                    } else if (character == '*') // ������/* xxx *�������
                    {
                        secondStarFlag = true;
                        continue;
                    }
                    if (secondStarFlag && character == '/') // �����ˡ�/* xxx */����������
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