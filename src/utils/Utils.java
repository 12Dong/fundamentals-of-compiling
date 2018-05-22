package utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Pattern;


public class Utils {

    public static int culate(String s) {
            //将中缀表达式转化为后缀表达式
            Queue<Object> queue = new LinkedList<Object>();
            Stack<Character> stack = new Stack<Character>();
            int x = 0;
            boolean isInt =false;
            for(int i=0; i<s.length(); i++){
                char ch = s.charAt(i);
                if(ch>='0'&&ch<='9'){
                    x = x*10 + ch-'0';
                    isInt = true;
                }else{
                    if(isInt)
                        queue.add((Integer)x);
                    x = 0;
                    isInt = false;
                    if(ch=='('){
                        stack.push(ch);
                    }else if(ch==')'){
//						System.out.println(stack);
//						System.out.println(queue);
                        while(stack.peek()!='('){
//							System.out.println(stack.peek());
                            queue.add(stack.pop());
                        }
                        stack.pop();
                    }else{
                        while(!stack.empty() && rank(stack.peek())>=rank(ch)){
                            queue.add(stack.pop());
                        }
                        stack.push(ch);
                    }
                }
            }
            if(x!=0) queue.add(x);
            while(!stack.empty()) queue.add(stack.pop());

//			计算逆波兰表达式
            Stack<Integer> integers = new Stack<Integer>();
            for(Object object : queue){
                if(object instanceof Integer){
                    integers.push((Integer)object);
                }else{
                    int b = integers.pop();
                    int a = integers.pop();
                    char op = (Character)object;
                    if(op=='+') integers.push(a+b);
                    else if(op=='-') integers.push(a-b);
                    else if(op=='*') integers.push(a*b);
                    else integers.push(a/b);
                }
            }
            return (integers.peek());
        }

    private static int rank(char ch){
        if(ch=='+'||ch=='-'){
            return 1;
        }else if(ch=='*' || ch=='/'){
            return 2;
        }else{	//( )
            return 0;	//( ) 的优先级应该跟高，但这里为了代码的简洁，将其设为最小
        }
    }


    static public boolean isNum(String str){
        Pattern pattern = Pattern.compile("^[0-9]*");
        return pattern.matcher(str).matches();
    }
}
