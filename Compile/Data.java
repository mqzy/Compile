package Compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {
    public static final String[] RULES = {
            "R->a=S",
            "S->X",
            "S->[T]",
            "T->SU",
            "T->#",
            "U->,SU",
            "U->#",
            "X->AB",
            "B->+AB",
            "B->#",
            "A->CD",
            "D->-CD",
            "D->#",
            "C->MN",
            "N->*MN",
            "N->#",
            "M->PQ",
            "Q->/PQ",
            "Q->#",
            "P->(X)",
            "P->a",
            "P->i",
    };
    public static final String[] VN = {"R", "S", "X", "T", "U", "A", "B", "C", "D", "M", "N", "P", "Q"};//非终结符
    public static final String[] VT = {"a", "i", "+", "-", "*", "/", "=", "[", "]", ",", "(", ")", "#"};//终结符
    public static final String[] VT1 = clone(VT);

    private static String[] clone(String[] vt) {
        String[] S = new String[vt.length];
        for (int i = 0; i < vt.length; i++) {
            if (!vt[i].equals("#"))
                S[i] = vt[i];
            else
                S[i] = "$";
        }
        return S;
    }

    public static final List<String> RESERVEWORDS = Arrays.asList("if", "else", "elsif", "end", "while", "for", "in");//保留字
    public static final List<String> SYMBOL = Arrays.asList("+", "-", "*", "/", "=", "==", "<", ">", ",", "\"", "[", "]", "(", ")", "{", "}", "...");//符号

    List<Element> input = new ArrayList<>();;//输入流

    public List<Element> getInput() {
        return input;
    }

    public void setInput(List<Element> input) {
        this.input = input;
    }
}
