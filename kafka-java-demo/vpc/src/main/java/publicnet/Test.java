package publicnet;
public class Test {
    public static void main(String[] args) {
        int i=-1;
        test(i--);
        System.out.println(i);
    }
     static void test(int i){
        System.out.println(i);
    }
}
