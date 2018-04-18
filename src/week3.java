import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class week3 {
    static Map<String,String> map;
    static Map<String,String> signMap;
    public static void makeMap(){
        map = new HashMap<>();
        map.put("const","constsym");
        map.put("var","varsym");
        map.put("real","realsym");
        map.put("if","ifsym");
        map.put("else","elsesym");
        map.put("call","callsym");
        map.put("do","dosym");
        map.put("odd","oddsym");
        map.put("procedure","procedursym");
        map.put("then","thensym");
        map.put("while","whilesym");
        map.put("begin","beginsym");
        map.put("read","readsym");
        map.put("end","endsym");
    }
    public static void makeSignMap(){
        signMap = new HashMap<>();
        signMap.put("=","eql");
        signMap.put(";","semicolon");
        signMap.put(",","comma");
        signMap.put("(","lparen");
        signMap.put(")","rparen");
        signMap.put("+","plus");
        signMap.put(".","period");
        signMap.put("-","minus");
        signMap.put("*","times");
        signMap.put("/","slash");
        signMap.put("#","neq");
        signMap.put("<=","leq");
        signMap.put("<","lss");
        signMap.put(">=","geq");
        signMap.put(">","ggr");
    }
    static public void main(String argv[])throws IOException{
        FileOutputStream outputStream = null;
        FileWriter fw = null;
        makeMap();
        for(int k=1;k<=1;k++){
            FileInputStream inputStream = new FileInputStream("/Users/12dong/Downloads/SSM_BookSystem-master/compile/src/analy"+k);
            outputStream = new FileOutputStream("/Users/12dong/Downloads/SSM_BookSystem-master/compile/src/out"+k+".txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            int rows = 0;
            while((str=bufferedReader.readLine())!=null){
                rows++;
                String filter = "";
                for(int i=0;i<str.length();i++){
                    if(str.charAt(i)=='/' && i!=str.length()-1 && str.charAt(i+1)=='/') break;
//                System.out.println(str.charAt(i));
                    str  = str.trim();
                    str = str.toLowerCase();
                    char current=str.charAt(i);
                    if(current==' ' && i>=1 && str.charAt(i-1)==' ') continue;
                    if(current=='=' || current==';' || current==',' || current=='+' || current==' '|| current=='(' || current==')' ||current=='.' || current==':'
                            || current=='+' || current=='-' || current=='*' || current=='/' || current=='#' || current=='>' ||  current=='<' || current==' '){
                        if(map.containsKey(filter)){
//                            outputStream.write(("("+map.get(filter)+"  "+filter+")").getBytes());
//                            outputStream.write("\n".getBytes());
                            System.out.println("("+map.get(filter)+"  "+filter+")");
                        }else{
                            String regx = "^[0-9]{1,10}";
                            if(current==' '){
                                filter="";
                                continue;
                            }
                            if(filter.matches(regx)){
//                                outputStream.write(("(number   "+filter+")").getBytes());
                                System.out.println("(number   "+filter+")");
                            }
                            else{
//                                outputStream.write(("(ident   "+filter+")").getBytes());
                                System.out.println("(ident    "+filter+")");
                            }
                            outputStream.write("\n".getBytes());
                        }
                        if(current==':'){
//                            outputStream.write(("(becomes   :=)   ").getBytes());
                            System.out.println("(becomes  :=)");
                            i++;
                        }else if(current=='='){
//                            outputStream.write(("(eql      =)   ").getBytes());
                            System.out.println("(eql      =)");
                        }else if(current==';'){
//                            outputStream.write(("(semicolon   ;)   ").getBytes());
                            System.out.println("(semicolon  ;)");
                        }else if(current==','){
//                            outputStream.write(("(comma   ,)   ").getBytes());
                            System.out.println("(comma    ,)");
                        }else if(current=='('){
//                            outputStream.write(("(lparen   ()   ").getBytes());
                            System.out.println("(lparen   ()");
                        }else if(current==')'){
//                            outputStream.write(("(rparen   ))   ").getBytes());
                            System.out.println("(rparen   ))");
                        }else if(current=='+'){
//                            outputStream.write(("(plus   +)   ").getBytes());
                            System.out.println("(plus     +)");
                        }else if(current=='.'){
//                            outputStream.write(("(period   .)   ").getBytes());
                            System.out.println("(period   .)");
                        }else if(current=='-'){
//                            outputStream.write(("(minus   -)   ").getBytes());
                            System.out.println("(minus    -)");
                        }else if(current=='*'){
//                            outputStream.write(("(times   *)   ").getBytes());
                            System.out.println("(times    *)");
                        }else if(current=='/'){
//                            outputStream.write(("(slash   /)   ").getBytes());
                            System.out.println("(slash    /)");
                        }else if(current=='#'){
//                            outputStream.write(("(neq   #)   ").getBytes());
                            System.out.println("(neq      #)");
                        }else if(current=='<'){
                            if(str.charAt(i+1)=='='){
//                                outputStream.write(("(leq   <=)   ").getBytes());
                                System.out.println("(leq      <=)");
                                i++;
                            }else{
//                                outputStream.write(("(lss   <)   ").getBytes());
                                System.out.println("(lss      <)");
                            }
                        }else if(current=='>'){
                            if(str.charAt(i+1)=='='){
//                                outputStream.write(("(geq   >=)   ").getBytes());
                                System.out.println("(geq      >=");
                                i++;
                            }else{
//                                outputStream.write(("(ggr   >)   ").getBytes());
                                System.out.println("(ggr      >)");
                            }
                        }
//                        outputStream.write("\n".getBytes());
                        filter="";
                    }else{
                        filter+=current;
                    }
                }
                if(map.containsKey(filter)){
//                    outputStream.write(("("+map.get(filter)+"  "+filter+")").getBytes());
                    System.out.println("("+map.get(filter)+"  "+filter+")");
                }
            }
        }
    }
}
