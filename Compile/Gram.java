package Compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Compile.Data.RULES;
import static Compile.Data.VN;
import static Compile.Data.VT1;

public class Gram {
    String[][] Table = new Table().createTable();

    public void GramAnalysis(List<Element> input_element){
        List<String> S = new ArrayList<>();//分析栈
        S.add("$");
        S.add(VN[0]);
        int i=0;
        while (!S.isEmpty()){
            print(S);
            System.out.println();
            try{
                if (S.get(S.size()-1).equals(input_element.get(i).getType())){
                    S.remove(S.size()-1);
                    i++;
                    continue;
                }
            } catch (Exception e){
                System.out.println("语法编译出错");
                break;
            }
            int index = getIndex(S.get(S.size()-1),input_element.get(i).getType());
            if (index==999) {
                System.out.println("语法编译出错");
                break;
            }
            else {
                String[] set = RULES[index].split("->");
                S.remove(S.size()-1);
                if (!set[1].equals("#")){
                    for (int j = set[1].length()-1; j >=0; j--) {
                        S.add(String.valueOf(set[1].charAt(j)));
                    }
                }
            }
        }
        System.out.println("ok");
    }

    private void print(List<String> s) {
        for (String value : s) {
            System.out.print(value + " ");
        }
    }

    private int getIndex(String s, String s1) {
        int m = 0,n = 0;
        for (int i = 0; i < VN.length; i++) {
            if (s.equals(VN[i])) {
                m = i + 1;
                break;
            }
        }
        for (int i = 0; i < VT1.length; i++) {
            if (s1.equals(VT1[i])){
                n = i + 1;
                break;
            }
        }
        if (!Table[m][n].equals("e")) {
            try {
                return Integer.parseInt(Table[m][n]);
            } catch (Exception e){
                return 999;
            }
        }
        return 999;
    }

    public static void main(String[] args) {
        Gram gram = new Gram();
        List<Element> in = Arrays.asList(
                new Element("a","a"),
                new Element("=","="),
                new Element("a","a"),
                new Element("+","a"),
                new Element("a","a"),
                new Element("*","a"),
                new Element("a","a"),
                new Element("-","a"),
                new Element("a","a"),
                new Element("/","a"),
                new Element("a","a"),
                new Element("$","$")
        );
        gram.GramAnalysis(in);
    }
}
