public class Playground implements Runnable{

    private int test;
    public Playground(int test){
        this.test = test;
    }
    @Override
    public void run() {
        System.out.println("Hi " + test);
    }

    public static void main(String[] args){
        Playground play = new Playground(5);
        for (int i = 0;i<3;i++){
            Thread test = new Thread(play);
            test.start();
        }
    }
}
