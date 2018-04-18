import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class week3 {
    static Map<String,String> map;
    static Map<String,String> signMap;
    static Map<String,String> varStack;
    static String filter;
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
        makeSignMap();

        for(int k=1;k<=1;k++){
            varStack = new HashMap<>();
            FileInputStream inputStream = new FileInputStream("/Users/12dong/IdeaProjects/compile/src/analy"+k);
//            outputStream = new FileOutputStream("/Users/12dong/Downloads/SSM_BookSystem-master/compile/src/out"+k+".txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            int rows = 0;
            while((str=bufferedReader.readLine())!=null){
                boolean type = false;
                rows++;
                filter = "";
                for(int i=0;i<str.length();i++){
                    if(str.charAt(i)=='/' && i!=str.length()-1 && str.charAt(i+1)=='/') break;
//                System.out.println(str.charAt(i));
                    str  = str.trim();
                    str = str.toLowerCase();
                    String current=""+str.charAt(i);
                    if(current==" " && i>=1 && str.charAt(i-1)==' ') continue;
                    if(signMap.containsKey(current) || current==" "){
                        //判断 关键词
                        if(map.containsKey(filter)){
                            if(type==true){
                                System.out.println(rows+"行出错  错误原因:关键词 被当作变量使用!  ");
                            }
                            type = true;
                            System.out.println("("+map.get(filter)+"  "+filter+")");
                        }else{
                            String regx = "^[0-9]*";
                            if(current==" "){
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
//                            outputStream.write("\n".getBytes());
                        }


                        if(current==":"){
//                            outputStream.write(("(becomes   :=)   ").getBytes());
                            System.out.println("(becomes  :=)");
                            i++;
                        }else if(signMap.containsKey(current)){
                            if(current=="<") {
                                if (str.charAt(i + 1) == '=') {
//                                outputStream.write(("(leq   <=)   ").getBytes());
                                    System.out.println("(leq      <=)");
                                    i++;
                                } else {
//                                outputStream.write(("(lss   <)   ").getBytes());
                                    System.out.println("(lss      <)");
                                }
                            }
                            if(current==">"){
                                if(str.charAt(i+1)=='='){
//                                outputStream.write(("(geq   >=)   ").getBytes());
                                    System.out.println("(geq      >=");
                                    i++;
                                }else{
//                                outputStream.write(("(ggr   >)   ").getBytes());
                                    System.out.println("(ggr      >)");
                                }
                            }
                            System.out.println("("+signMap.get(current)+"  "+current+")");
                        }
//                        outputStream.write("\n".getBytes());
                        filter="";
                    }else{
                        filter+=current;
                    }
                }
                //  ;结束判断
                if(map.containsKey(filter)){
//                    outputStream.write(("("+map.get(filter)+"  "+filter+")").getBytes());
                    System.out.println("("+map.get(filter)+"  "+filter+")");
                }
            }
        }
    }
}
