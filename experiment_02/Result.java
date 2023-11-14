package experiment_02;

public class Result{
    private String typeCode;
    private String word;


    public Result(String code, String temp) {
        this.typeCode=code;
        this.word=temp;
    }

    public String toString(){
        return "<"+typeCode+","+word+">";
    }

}