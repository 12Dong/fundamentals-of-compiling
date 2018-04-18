import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class week3 {
    static Map<String,String> map;
    static Map<String,String> signMap;
    static Map<String,Integer> varStack;
    static Map<String,Integer> constStack;
    static Map<String,Integer> functionStack;
    static String filter;



    static public void main(String argv[])throws IOException{
        FileOutputStream outputStream = null;
        FileWriter fw = null;
        makeMap();
        makeSignMap();

        for(int k=1;k<=1;k++) {
            varStack = new HashMap<>();
            constStack = new HashMap<>();
            functionStack = new HashMap<>();
            FileInputStream inputStream = new FileInputStream("/Users/12dong/IdeaProjects/compile/src/analy" + k);
//            outputStream = new FileOutputStream("/Users/12dong/Downloads/SSM_BookSystem-master/compile/src/out"+k+".txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            int rows = 0;
            while ((str = bufferedReader.readLine()) != null) {
                rows++;
                str = str.toLowerCase();
//                检测程序结束标志 end.
                str = str.trim();
                //没有 ; 为 begin 或者 end.
                if(!str.contains(";")){
                    String type="";
                    for(int i=0;i<str.length();i++){
                        String sign = ""+str.charAt(i);
                        if(signMap.containsKey(sign)){
                            if(map.containsKey(type)==false){
                                System.out.println(rows+" 行 非法字符");
                                return ;
                            }
                            System.out.println("("+map.get(type)+"    "+type+")");
                            System.out.println("("+signMap.get(sign)+"   "+str.charAt(i)+")");
                            type=null;
                            continue;
                        }
                        type+=str.charAt(i);
                    }
                    if(type !=null && map.containsKey(type)==false){
                        System.out.println(rows+" 行 非法字符");
                        return ;
                    }

                    if(type!=null){
                        System.out.println("("+map.get(type)+"    "+type+")");
                    }
                    continue;
                }

                if(str.contains("(")&&str.contains(")")){
                    String segments[] = str.split("[()]");
                    if(map.containsKey(segments[0])){
                        System.out.println("("+map.get(segments[0])+"   "+segments[0]+")");
                        System.out.println("("+signMap.get("(")+"    "+"()");
                        if(judgeVarStack(segments[1])){
                            System.out.println("(ident    "+segments[1]+")");
                            System.out.println("("+signMap.get(")")+"    ))");
                            System.out.println("("+signMap.get(";")+"    ;)");
                        }else{
                            //变量未出现 在 变量池中
                            System.out.println(rows+" 行 非法变量 未定义");
                            return ;
                        }

                    }else{
                        //左括号前的 关键词 非法
                        System.out.println(rows+" 行 非法关键字");
                        return ;
                    }
                    continue;
                }else if(str.contains("(")  || str.contains(")")){
                    System.out.println(rows+" 行 缺少 （ 或者 ）");
                    return ;
                }
                filter = "";
                //对应 一行 多段 异端 编码 世界上 怎么会有如此naocan的编码方式
                String segments[] = str.split(";");
                for (String segment : segments) {

                    if(segment.contains(":=")){
                        String words[] = segment.split(":=");

                        // words[0] 目标对象
                        // words[1] 表达式
                        Stack<Integer> tmpVarStack = new Stack<>();
//                        检测 目标对象1 是否存在
                        if(constStack.containsKey(words[0])){
                            System.out.println(rows+" 行 编译错误 常量 无法 改变值");
                            return ;
                        }
                        if(functionStack.containsKey(words[0])){
                            System.out.println(rows+" 行 编译错误 函数 无法 被改变");
                            return ;
                        }
                        if(varStack.containsKey(words[0])==false){
                            System.out.println(rows+" 行 编译错误 变量未被定义");
                            return;
                        }
                        System.out.println("(ident    "+words[0]+")");
                        System.out.println("("+signMap.get(":=")+"    :=)");
                        String var = "";
                        for(int i=0;i<words[1].length();i++){
                            //遇到 符号
                            String current = ""+words[1].charAt(i);
                            if(signMap.containsKey(current)){
                                if(functionStack.containsKey(var)){
                                    System.out.println(rows+" 行 编译错误 函数无法参与运算");
                                    return;
                                }
                                if(constStack.containsKey(var)){
                                    tmpVarStack.push(constStack.get(var));
                                    System.out.println("(ident     "+var+")");
                                }else if(varStack.containsKey(var)){
                                    tmpVarStack.push(varStack.get(var));
                                    System.out.println("(ident     "+var+")");

                                }else{
                                    System.out.println(rows+" 行 编译错误 变量未被定义");
                                    return ;
                                }
                                System.out.println("("+signMap.get(current)+"    "+current+")");
                                var="";
                            }else{
                                var+=words[1].charAt(i);
                            }
                        }
                        if(functionStack.containsKey(var)){
                            System.out.println(rows+" 行 编译错误 函数无法参与运算");
                            return;
                        }
                        if(constStack.containsKey(var)){
                            tmpVarStack.push(constStack.get(var));
                            System.out.println("(ident     "+var+")");
                        }else if(varStack.containsKey(var)){
                            tmpVarStack.push(varStack.get(var));
                            System.out.println("(ident     "+var+")");

                        }else{
                            System.out.println(rows+" 行 编译错误 变量未被定义");
                            return ;
                        }
                    }
                    //分割 空格 转化为 Type  expression格式 如果 有的话
                    String words[] = segment.split(" ");
                    if (map.containsKey(words[0])) {

//                        ===================================================================
                        //常量 处理方法
                        if (words[0].equals("const")){
                            System.out.println("(" + map.get("const") + "   " + "const)");
                            //逗号 分割 表达式
                            String expressions[] = words[1].split(",");
                            boolean onlyOnce = false;
                            for (String expression : expressions) {
//                                逗号 表达
                                if(onlyOnce==true){
                                    System.out.println("("+signMap.get(",")+"   ,)");
                                }
                                //常量 必须在初始化时 被赋值 也就是 每个 expression 必须有 =
                                if (expression.contains("=")) {
//                                    分割为 var =  value
                                    String process[] = expression.split("=");
                                    if(process.length==1){
                                        System.out.println(rows+" 行 =号右边 没有赋值");
                                        return ;
                                    }
                                    String regex = "^[0-9]*";
                                    //判断 value 合法
                                    if (process[1].matches(regex) == false) {
                                        System.out.println(rows + " 行 const变量 应为整数");
                                        return;
                                    } else {
                                        //判断 var 重名
                                        if (judgeExit(process[0]) == false) {
                                            System.out.println(rows + " 行 const常量名 已被使用");
                                            return;
                                        }
                                        if (judgeLegal(process[0]) == false) {
                                            System.out.println(rows + " 行 const常量名 命令非法");
                                        }
                                        System.out.println("(ident    " + process[0] + ")");
                                        System.out.println("(eql      =)");
                                        System.out.println("(number   " + process[1] + ")");
                                        constStack.put(process[0], Integer.parseInt(process[1]));
                                    }
                                } else {
                                    System.out.println(rows + "   行 编译错误 常量必须在初始化时 被赋值");
                                }
                                onlyOnce = true;
                            }
                        }

//                        ===================================================================
                        //  变量处理方法
                        else if(words[0].equals("var")){
                            System.out.println("(" + map.get("var") + "   " + "var)");
                            //逗号 分割 表达式
                            String expressions[] = words[1].split(",");
                            boolean onlyOnce = false;
                            for (String expression : expressions) {
                                if(onlyOnce==true){
                                    System.out.println("("+signMap.get(",")+"    ,)");
                                }
                                // 变量 可以初始化 也可以不初始化
                                if(expression.contains("=")){
                                    String process[] = expression.split("=");
                                    if(process.length==1){
                                        System.out.println(rows+" 行  = 右边 没有 赋值");
                                        return ;
                                    }else{


                                        String regex = "^[0-9]*";
                                        //判断 value 合法
                                        if (process[1].matches(regex) == false) {
                                            //判断等式 右边 是否 为 变量 or 常量
                                            if(varStack.containsKey(process[1]) || constStack.containsKey(process[1])){
                                                //判断 var 重名
                                                if (judgeExit(process[0]) == false) {
                                                    System.out.println(rows + " 行 var变量名 已被使用");
                                                    return;
                                                }
                                                if (judgeLegal(process[0]) == false) {
                                                    System.out.println(rows + " 行 var变量名 命名非法");
                                                    return;
                                                }
                                                System.out.println("(ident    " + process[0] + ")");
                                                System.out.println("(eql      =)");
                                                System.out.println("(ident    " + process[1] + ")");
                                                if(varStack.containsKey(process[1])) varStack.put(process[0],varStack.get(process[1]));
                                                if(constStack.containsKey(process[1])) varStack.put(process[0],constStack.get(process[1]));
                                            }
                                            else{
                                                System.out.println(rows + " 行 var变量 应为整数 或 变量");
                                                return ;
                                            }
                                        } else {
                                            //判断 var 重名
                                            if (judgeExit(process[0]) == false) {
                                                System.out.println(rows + " 行 var变量名 已被使用");
                                                return;
                                            }
                                            if (judgeLegal(process[0]) == false) {
                                                System.out.println(rows + " 行 var变量名 命令非法");
                                            }
                                            System.out.println("(ident    " + process[0] + ")");
                                            System.out.println("(eql      =)");
                                            System.out.println("(number   " + process[1] + ")");
                                            varStack.put(process[0], Integer.parseInt(process[1]));
                                        }
                                    }
                                }else{
                                    //变量 没有 初始化
                                    //判断 var 重名
                                    if (judgeExit(expression) == false) {
                                        System.out.println(rows + " 行 var变量名 已被使用");
                                        return;
                                    }
                                    if (judgeLegal(expression) == false) {
                                        System.out.println(rows + " 行 var变量名 命令非法");
                                    }
                                    System.out.println("(ident    " + expression + ")");
                                    varStack.put(expression, 0);
                                }
                                onlyOnce = true;
                            }
                        }
                        else if(words[0].equals("procedure")){
                            // 处理 函数
                            if(judgeExit(words[1])==false){
                                System.out.println(rows+" 行 编译错误 函数名被占用");
                                return;
                            }else{
                                System.out.println("("+map.get("procedure")+"    procedure)");
                                functionStack.put(words[1],rows);
                                System.out.println("(ident     "+words[1]+")");
                            }
                        }
                        else if(words[0].equals("call")){
                            // 处理 函数
                            if(judgeExit(words[1])==true){
                                System.out.println(rows+" 行 编译错误 函数名未定义");
                                return;
                            }else{
                                System.out.println("("+map.get("call")+"    call)");
                                System.out.println("(ident     "+words[1]+")");
                            }
                        }

//                        非 变量 常量 定义
                        else {
                            System.out.println("("+map.get(words[0])+"     "+words[0]+")");
                        }
                    }


                    //输出 为 split 分割 的 ;
                    System.out.println("(semicolon  ;)");
                }

            }
        }
    }



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
            map.put("write","writesym");
            map.put("end","endsym");
        }
        public static void makeSignMap(){
            signMap = new HashMap<>();
            signMap.put(":=","becomes");
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


        static boolean judgeExit(String var){
            if(constStack.containsKey(var)==true) return false;
            if(varStack.containsKey(var)==true) return false;
            if(functionStack.containsKey(var)==true) return false;
            return true;
        }
        static boolean judgeLegal(String var){
            String regex = "^[a-zA-Z]{1}[a-zA-Z0-9]*";
            return var.matches(regex);

        }
        static boolean judgeVarStack(String varName){
            return varStack.containsKey(varName);
        }
}
