package Compile;

/*
* 	reserved_words(r)
*   identifier    (a)：字母
*	number        (i)：数字
*	x -> i | a
* */

public class Element {
    private String type = "";
    private String value;
    private String sub_type;

    public Element(String t, String v){
        type = t;
        switch (t){
            case "reserved_words":
                type = "r";
                break;
            case "identifier":
                type = "a";
                break;
            case "number":
                type = "i";
                break;
            case "symbol":
                type = v;
                break;
        }
        value = v;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }
}
