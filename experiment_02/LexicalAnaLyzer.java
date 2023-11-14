package experiment_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LexicalAnaLyzer {
    public static void main(String[] args) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get("testcode.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(content);
        

        System.out.println("--------------------------------------------");
        // String words[] = content.split("\\s+");
        // for (String word : words) {
        //     System.out.println(word);
        // }
        AnalyzeTool tool = new AnalyzeTool(content);
        // Thread t1 = new Thread(tool);
        // t1.start();
        tool.run();
    }
}