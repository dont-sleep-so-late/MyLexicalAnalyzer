package experiment_02;

public class Keyword {
    public static String[] keywords = {
        "and",
        "array",
        "begin",
        "bool",
        "call",
        "case",
        "char",
        "const",
        "do",
        "double",
        "else",
        "end",
        "false",
        "for",
        "if",
        "int",
        "not",
        "of",
        "or",
        "procedure",
        "program",
        "read",
        "real",
        "repeat",
        "set",
        "stop",
        "then",
        "to",
        "true",
        "until",
        "var",
        "while",
        "write",
        "import",
        "printf",
        "java",
        "void",
        "String",
        "main",
        "public",
        "private",
        "Scanner",
        "util",
        "class",
        "args",
        "System"
    };
    public static boolean isKeyword(String target) {
        for (String temp : keywords) {
            if (temp.equals(target))
                return true;
        }
        return false;
    }

}