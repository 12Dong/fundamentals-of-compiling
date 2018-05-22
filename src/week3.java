import java.io.*;
import java.util.*;

import utils.Utils;

public class week3 {
    static Map<String,String> map;
    static Map<String,String> signMap;
    static Map<String,Integer> varStack;
    static Map<String,Integer> constStack;
    static Map<String,Integer> functionStack;
    static String filter;



    static public void main(String argv[])throws IOException{
        Scanner scanner = new Scanner(System.in);
        FileOutputStream outputStream = null;
        FileWriter fw = null;
        makeMap();
        makeSignMap();

        for(int k=9;k<=9;k++) {
            varStack = new HashMap<>();
            constStack = new HashMap<>();
            functionStack = new HashMap<>();
            FileInputStream inputStream = new FileInputStream("/Users/12dong/IdeaProjects/compile/src/analy" + 1    );
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

                if(str.contains("(") && str.contains(")") == false){
                    System.out.println(rows+" 行 缺少 （ 或者 ）");
                    return ;
                }
                filter = "";
                //对应 一行 多段 异端 编码 世界上 怎么会有如此naocan的编码方式
                String segments[] = str.split(";");
                for (String segment : segments) {

                    //赋值语句
                    if(segment.contains(":=")){
                        String words[] = segment.split(":=");

                        // words[0] 目标对象
                        // words[1] 表达式
                        System.out.println("表达式 = V = :"+words[1]);
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
                        if(varStack.containsKey(words[0].trim())==false){
                            System.out.println( "当前变量  "+words[0]);
                            System.out.println(rows+" 行 编译错误 变量未被定义");
                            return;
                        }
                        System.out.println("(ident    "+words[0]+")");
                        //监测到 欲赋值的变量
                        System.out.println("("+signMap.get(":=")+"    :=)");
                        String var = "";
                        String calculateExpression = "";
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
                                    calculateExpression +=constStack.get(var);
                                }else if(varStack.containsKey(var.trim())){
                                    tmpVarStack.push(varStack.get(var));
                                    System.out.println("(ident     "+var+")");
                                    calculateExpression +=varStack.get(var);
                                }else if(Utils.isNum(var)){
                                    System.out.println("(number     "+var+")");
                                    calculateExpression+=var;
                                }
                                else{
                                    for(int q=0;i<var.length();q++){
                                        System.out.println(q+"  "+var.charAt(q));
                                    }
                                    System.out.println(var);
                                    System.out.println(rows+" 行 编译错误 变量未被定义");
                                    return ;
                                }
                                System.out.println("("+signMap.get(current)+"    "+current+")");
                                calculateExpression+=current;
                                var="";
                            }else{
                                var+=words[1].charAt(i);
                            }
                        }
                        //终结句子
                        if(functionStack.containsKey(var)){
                            System.out.println(rows+" 行 编译错误 函数无法参与运算");
                            return;
                        }
                        if(constStack.containsKey(var)){
                            tmpVarStack.push(constStack.get(var));
                            System.out.println("(ident     "+var+")");
                            calculateExpression+=constStack.get(var);
                        }else if(varStack.containsKey(var)){
                            tmpVarStack.push(varStack.get(var));
                            System.out.println("(ident     "+var+")");
                            calculateExpression+=varStack.get(var);
                        }else if(Utils.isNum(var)){
                            System.out.println("(number     "+var+")");
                            calculateExpression+=var;
                        }
                        else{
                            System.out.println(var);
                            System.out.println(rows+" 行 编译错误 变量未被定义");
                            return ;
                        }
                        System.out.println("转化后表达式 = V =  "+calculateExpression);
                        int value=0;
                        try{
                             value = Utils.culate(calculateExpression);
                        }catch(Exception e){
                            System.out.println("算数表达式存在错误");
                            return;
                        }
                        System.out.println(words[0] +" = "+value);
                        if(varStack.containsKey(words[0])){
                            varStack.put(words[0],value);
                        }else if (constStack.containsKey(words[0])){
                            constStack.put(words[0],value);
                        }
                    }
                    //分割 空格 转化为 Type  expression格式 如果 有的话
                    String words[] = segment.split("[ |( | )]");
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
                        //检测 read write
                        else if(words[0].equals("read")){
                            System.out.println("("+map.get(words[0])+"     "+words[0]+")");
                            System.out.println("("+signMap.get("(")+"     ()");
                            System.out.println("(ident     "+words[1]+")");
                            System.out.println("("+signMap.get(")")+"     ))");
                            System.out.println("请输入参数值");
                            String input = scanner.next();
                            if(varStack.containsKey(words[1])){
                                if(Utils.isNum(input)==false){
                                    System.out.println(rows+"行 输入非法 应该输入Integer类型");
                                }else{
                                    varStack.put(words[1],Integer.parseInt(input));
                                }
                            }else{
                                System.out.println(rows+" 行 类型错误 无法输入该类型变量");
                            }
                        }else if(words[0].equals("write")){
                            System.out.println("("+map.get(words[0])+"     "+words[0]+")");
                            System.out.println("("+signMap.get("(")+"     ()");
                            System.out.println("(ident     "+words[1]+")");
                            System.out.println("("+signMap.get(")")+"     ))");
                            if(constStack.containsKey(words[1])){
                                System.out.println("常量"+words[1]+" 值 为" +constStack.get(words[1]));
                            }else if(varStack.containsKey(words[1])){
                                System.out.println("变量"+words[1]+" 值 为" +varStack.get(words[1]));
                            }else{
                                System.out.println(rows+ " 行 变量非法");
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
            // 类别符
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
/*
模仿Java虚拟机分区概念 分别设立常量栈 变量栈 和 函数栈(底层用hashMap实现 时间复杂度约为O(1))
以变量为例 解释编译机制
假设 要创建一个变量对象 首先 要在 常量栈 变量栈 函数栈 里 使用其hash值查找 如果没有则说明 函数名初始化正确
如果没有对该变量 初始化 其值 那么 我们为其设定 INTEGER.MIN 表示 没有 对其初始化 如果有 初始化 则将其键值 设定为 初始化值
初始化 及 赋值 应注意不可为负 (参照汇编编码)
假设 要使用一个变量 那么那么我们应该在变量栈中 找到变量 并为其赋值 其中分两种情况 使用正则表达式 检验是否为数字
如果是数字 放入 变量栈 如果是变量 则 继续使用该条 找到变量对应的值 并修改 变量栈 如果变量栈中其对应的值为INTEGER.MIN 则说明
为赋值 报错
常量在变量基础上 加上两条 1)申明常量时 应为其初始化 否则报错 2)不可在出了初始化外常量赋值

if(words[0].equals("var")){
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
 */