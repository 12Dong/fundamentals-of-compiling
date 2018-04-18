public class test {
    static public void main(String argv[]){
        String regx = "^[a-zA-Z]{1}[a-zA-Z0-9]*";
        String num = "14213123123";
        System.out.println(num.matches(regx));
    }
}
