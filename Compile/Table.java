package Compile;

import java.util.ArrayList;
import java.util.List;

import static Compile.Data.RULES;
import static Compile.Data.VT;
import static Compile.Data.VN;
import static Compile.Data.VT1;

public class Table {//First集和Follow集
    public String[][] Table;
    class Set{
        private String s;
        private List<String> list = new ArrayList<>();
        private boolean isES = false;//是否存在空串

        public Set(String s){
            this.s = s;
        }

        public String getS(){
            return this.s;
        }
        public void setList(String v){
            if (!this.list.contains(v))
                this.list.add(v);
        }

        public void print(){
            System.out.print("("+this.s+",{ ");
            for (int i = 0; i < list.size(); i++) {
                if (i!=list.size()-1)
                    System.out.print(list.get(i)+",");
                else
                    System.out.print(list.get(i));
            }
            System.out.print(" }");
            System.out.print(")");
            System.out.println();
        }

    }
    List<Set> FirstList = new ArrayList<>();
    List<Set> FollowList = new ArrayList<>();

    public boolean isHs(String s, List<Set> list){//判断是否存在s的集合
        for (Set set : list) {
            if (s.equals(set.getS())) {
                return true;
            }
        }
        return false;
    }

    public int getIndex(String s,List<Set> list){//返回s集合在List中的位置
        for (int i = 0; i < list.size(); i++) {
            if (s.equals(list.get(i).getS())){
                return i;
            }
        }
        return 999;
    }

    public int getIndex(String s,String[] ss){
        for (int i = 0; i < ss.length; i++) {
            if (s.equals(ss[i]))
                return i;
        }
        return 999;
    }

    public void getFirstSet(String[] strs){
        for (String str : strs) {
            String[] set = str.split("->");
            if (!isHs(set[0], FirstList))
                FirstList.add(new Set(set[0]));
            if (set[1].equals("#")) {
                FirstList.get(getIndex(set[0].toString(), FirstList)).isES = true;
                FirstList.get(getIndex(set[0].toString(), FirstList)).setList(set[1]);
            }
        }

        for (String str : strs) {
            String[] set = str.split("->");
            int index = getIndex(set[0], FirstList);
            String Right = set[1];
            StringBuilder builder = new StringBuilder("");
            for (int j = 0; j < Right.length(); j++) {
                builder.append(String.valueOf(Right.charAt(j)));
                if (isTerm(builder)) {
                    FirstList.get(index).setList(builder.toString());
                    builder.delete(0, builder.length());
                    break;
                } else if (isNonTerm(builder)) {
                    if (!FirstList.get(getIndex(builder.toString(), FirstList)).isES) {
                        FirstList.get(index).setList(builder.toString());
                        builder.delete(0, builder.length());
                        break;
                    } else {
                        FirstList.get(index).setList(builder.toString());
                        builder.delete(0, builder.length());
                    }
                }
            }
        }

        for (int i = 0; i < FirstList.size(); i++) {
            form(FirstList.get(i).list,FirstList);
        }
//        printList(FirstList);
    }

    public void getFollowSet(String[] strs){
        for (int i = 0; i < FirstList.size(); i++) {
            FollowList.add(new Set(FirstList.get(i).getS()));
            if (FollowList.get(i).getS().equals(VN[0]))
                FollowList.get(i).setList("$");
        }
        for (Set value : FollowList) {
            for (String str : strs) {
                String[] set = str.split("->");
                String Right = set[1];
                StringBuilder builder = new StringBuilder("");
                boolean flag = false;
                for (int k = 0; k < Right.length(); k++) {
                    builder.append(Right.charAt(k));
                    if (builder.toString().equals(value.getS())) {
                        flag = true;
                        builder.delete(0, builder.length());
                        if (k == Right.length() - 1) {
                            if (!set[0].equals(value.getS()))
                                value.setList(set[0]);
                        } else
                            continue;
                    }
                    if (flag) {
                        if (isTerm(builder)) {
                            value.setList(builder.toString());
                            builder.delete(0, builder.length());
                            break;
                        } else if (isNonTerm(builder)) {
                            List<String> list = FirstList.get(getIndex(builder.toString(), FirstList)).list;
                            for (String s : list) {
                                value.setList(s);
                            }
                            builder.delete(0, builder.length());
                            if (list.contains("#")) {
                                value.list.remove("#");
                                if (k == Right.length() - 1) {
                                    value.setList(set[0]);
                                }
                            } else {
                                break;
                            }
                        }
                    } else {
                        if (isTerm(builder) || isNonTerm(builder))
                            builder.delete(0, builder.length());
                    }
                }
            }
        }
        for (int i = 0; i < FollowList.size(); i++) {
            form(FollowList.get(i).list,FollowList);
        }
//        printList(FollowList);
    }
    private void form(List<String> list1,List<Set> list2) {
        boolean flag = true;
        for (int i = 0; i < list1.size(); i++) {
            StringBuilder builder = new StringBuilder(list1.get(i));
            if (isNonTerm(builder)){
                list1.remove(i);
                List<String> list3 = list2.get(getIndex(builder.toString(),list2)).list;
                for (String s : list3) {
                    if (!list1.contains(s))
                        list1.add(s);
                }
                builder.delete(0,builder.length());
                flag = false;
            }
        }
        if (!flag)
            form(list1,list2);
    }

    private void printList(List<Set> list) {//打印List
        for (Set set : list) {
            set.print();
        }
    }

    private boolean isNonTerm(StringBuilder builder) {//判断字符串是否为非终结符号
        for (String s : VN) {
            if (builder.toString().equals(s))
                return true;
        }
        return false;
    }

    private boolean isTerm(StringBuilder builder) {//判断字符串是否为终结符号
        for (String s : VT) {
            if (builder.toString().equals(s))
                return true;
        }
        return false;
    }

    public String[][] createTable(){
        getFirstSet(RULES);
        getFollowSet(RULES);
        Table =new String[VN.length+1][VT.length+1];
        Table[0][0] = "V";
        System.arraycopy(VT1, 0, Table[0], 1, VT1.length);
        for (int i = 0; i < VN.length; i++)
            Table[i+1][0] = VN[i];
        for (int i = 0; i < VN.length; i++) {
            for (int j = 0; j < VT.length; j++)
                Table[i+1][j+1] = "e";
        }
        List<String> list;
        int m = 0;
        int n = 0;
        for (int i = 0; i < VN.length; i++) {
            m = i+1;
            for (int j = 0; j < RULES.length; j++) {
                String[] set = RULES[j].split("->");
                if (set[0].equals(Table[m][0])) {
                    for (int k = 0; k < set[1].length(); k++) {
                        StringBuilder builder = new StringBuilder("");
                        builder.append(set[1].charAt(k));
                        if (isTerm(builder)&&!builder.toString().equals("#")){
                            n = getIndex(builder.toString(),VT1)+1;
                            Table[m][n] = String.valueOf(j);
                            break;
                        }
                        else if (builder.toString().equals("#")){
                            list = FollowList.get(getIndex(set[0],FollowList)).list;
                            for (String s : list) {
                                n = getIndex(s, VT1) + 1;
                                Table[m][n] = String.valueOf(j);
                            }
                            break;
                        }
                        else {
                            list = FirstList.get(getIndex(builder.toString(),FirstList)).list;
                            for (String s : list) {
                                if (!s.equals("#")) {
                                    n = getIndex(s, VT1) + 1;
                                    Table[m][n] = String.valueOf(j);
                                }
                            }
                            if (k == set[1].length()-1&&list.contains("#")){
                                List<String> list2 = FollowList.get(getIndex(set[0],FollowList)).list;
                                for (String s : list2) {
                                    n = getIndex(s, VT1) + 1;
                                    Table[m][n] = String.valueOf(j);
                                }
                            }else if (!list.contains("#")){
                                break;
                            }
                        }
                    }
                }
            }
        }
        return Table;
    }

    public void printTable(){
        createTable();
        for (int i = 0; i < VN.length+1; i++) {
            for (int j = 0; j < VT.length+1; j++) {
                System.out.print(Table[i][j]+" ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Compile.Table table = new Table();
        table.createTable();
        table.printTable();
    }
}
