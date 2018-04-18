public class test {
    static public void main(String argv[]){
        String regx = "^[0-9]*";
        String num = "14213123123";
        System.out.println(num.matches(regx));
    }
}
