package experiment_02;

public class Sign {
    public static String[] signs = {
        "(",
        ")",
        ",",
        ".",
        ":",
        ":=",
        ";",
        "[",
        "]",
        "_",
        "{",
        "}",
        "#"
    };
    public static boolean isSign(String target) {
        for (String temp : signs) {
            if (temp.equals(target))
                return true;
        }
        return false;
    }

}