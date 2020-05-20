package Compile;

public class Cp {
    public static void main(String[] args) {
        Lex lex = new Lex();
        Gram gram = new Gram();
        Data data = new Data();
        int line = lex.read_code();
        for (int i = 0; i < line; i++) {
            data.setInput(lex.lexcial_analsis(i));
            gram.GramAnalysis(data.getInput());
        }
    }
}