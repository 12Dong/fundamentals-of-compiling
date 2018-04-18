public class test {
    static public void main(String argv[]){
        String segments[] = "read(x);".split("[()]");
        for(String segment:segments){
            System.out.println(segment);
        }
    }
}
