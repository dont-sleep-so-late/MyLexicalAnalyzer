package experiment_02;

public class Operator {
    public static String[] operators = {
        "+",
        "-",
        "*",
        "/",
        "=",
        "==",
        "<",
        ">",
        "<>",
        "%",
        "<=",
        "=",
        ">=",
    };
    public static boolean isOperator(String target) {
        for (String temp : operators) {
            if (temp.equals(target))
                return true;
        }
        return false;
    }
}
