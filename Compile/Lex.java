package Compile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Compile.Data.RESERVEWORDS;
import static Compile.Data.SYMBOL;

public class Lex {
    List<String> all_codes = new ArrayList<>();
    List<String> identifier = new ArrayList<>();//标识符
    List<Element> result = new ArrayList<>();

    boolean begin_interpret = false;

    public int read_code(){
        Scanner scanner = new Scanner(System.in);
        boolean begin_enter = false;
        while (true) {
            String s = scanner.nextLine().trim();
            if (s.equals("{Test Token}")) {
                begin_enter = true;
                //all_codes.add(s);
            } else if (s.equals("{Test End}")){
                //all_codes.add(s);
                break;
            }else if (begin_enter && !s.equals(""))
                all_codes.add(s);
        }
        scanner.close();
        return all_codes.size();
    }

    public List<Element> lexcial_analsis(int i) {
        result.clear();
        System.out.println(i+1+": "+all_codes.get(i));
        GetToken(i+1, begin_interpret);
        result.add(new Element("$","$"));
        return result;
    }

    private void GetToken(int i, boolean begin_interpret) {
        String s = all_codes.get(i-1);
        StringBuilder temp = new StringBuilder();
        String type = "";
        boolean begin_string = false;
        boolean point_flag = false;
        for (int count = 0; count<= s.length(); count++) {
            String next;
            if(count == s.length())
                next = "";
            else
                next = s.substring(count,count+1);

            if (begin_interpret){
                if (next.equals("}"))
                    begin_interpret = false;
            }
            else if (next.equals("{")){
                begin_interpret = true;
                Get_element(temp.toString(),type,i);
                temp = new StringBuilder();
            }
            else if (begin_string){
                temp.append(next);
                if (next.equals("\"")){
                    begin_string = false;
                    Get_element(temp.toString(),"string",i);
                    temp = new StringBuilder();
                }
            }
            else if (next.equals("\"")){
                begin_string = true;
                temp.append(next);
            }
            else if (next.equals(" ")||count==s.length()){
                if (begin_string)
                    temp.append(next);
                else {
                    Get_element(temp.toString(),type,i);
                    temp = new StringBuilder();
                    type="";
                }
            }
            else if (!next.equals("")){
                int length = check_symbol(s,count,i);
                if (length > 0){
                    print_by_type(temp.toString(),type,i);
                    temp = new StringBuilder();
                    type="";
                    String this_symbol = s.substring(count,count+length);
                    Get_element(this_symbol, "symbol", i);
                    count += length-1;
                }
                else {
                    if (type.equals("")){
                        point_flag = false;
                        if (next.charAt(0)>='0'&&next.charAt(0)<='9')
                            type = "number";
                        else if (next.charAt(0)>='a'&&next.charAt(0)<='z')
                            type = "identifier";
                    }
                    else {
                        if (type.equals("number")){
                            if (next.charAt(0)=='.'){
                                if(point_flag)
                                    report_error("非法小数点", i);
                                else
                                    point_flag = true;
                            }
                            else if (!(next.charAt(0)>='0'&&next.charAt(0)<='9'))
                                report_error("非法数字", i);
                        }
                        else if (type.equals("identifier")){
                            if (!(next.charAt(0)>='a'&&next.charAt(0)<='z'||next.charAt(0)=='-'||next.charAt(0)=='_'))
                                report_error("非法标识符", i);
                        }
                    }
                    temp.append(next);
                }
            }
        }
        if (begin_string)
            report_error("\" 无法匹配", i);
    }

    private void report_error(String message, int i) {
        System.out.println("第"+i+"行词法编译错误："+message);
    }


    private int check_symbol(String s, int index, int i) {
        int length = 0, s_len = s.length();
        for (String value : SYMBOL) {
            int temp_len = value.length();
            if (temp_len > length && index < s_len - temp_len + 1 && value.equals(s.substring(index, index + temp_len)))
                length = temp_len;
        }
        return length;
    }

    public void Get_element(String s,String type,int i){
        if (RESERVEWORDS.contains(s))
            print_by_type(s,"reserved words",i);
        else if (identifier.contains(s))
            print_by_type(s, "identifier", i);
        else if(SYMBOL.contains(s))
            print_by_type(s, "symbol", i);
        else
        {
            if (type.equals("identifier"))
            {
                identifier.add(s);
                print_by_type(s, "identifier", i);
            }
            else if (type.equals("number")||type.equals("string"))
                print_by_type(s, "number", i);
        }
    }

    private void print_by_type(String s, String type, int i) {
        if (s.equals(""))
            return;
        Element temp = new Element(type, s);
        result.add(temp);
        switch (type) {
            case "reserved_words":
                System.out.println("  " + i + ":reserved words" + s);
                break;
            case "symbol":
                System.out.println("  " + i + ": " + s);
                break;
            case "identifier":
                if (!identifier.contains(s))
                    identifier.add(s);
                System.out.println("  " + i + ":ID,name = " + s);
                break;
            case "number":
                System.out.println("  " + i + ":NUM,value = " + s);
                break;
        }
    }

    public static void main(String[] args) {
        Lex test = new Lex();
        test.read_code();
        //test.print();
        System.out.println("开始分析词法……");
        for (int i = 0; i < test.all_codes.size(); i++) {
            test.lexcial_analsis(i);
        }
    }
}

