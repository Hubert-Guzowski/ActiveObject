import java.util.List;
import java.util.Random;

public class Producer extends Thread{

    private Proxy proxy;
    private int max_portion;
    private Future future;
    private Random random;

    public Producer(Proxy proxy, int max_portion){
        this.proxy = proxy;
        this.max_portion = max_portion;
        this.random = new Random();
    }

    private void printResult(Object result){
        List<Integer> produced = (List<Integer>) result;
        System.out.println("Produced: [");
        for(Integer i : produced) System.out.print(i + ",");
        System.out.println("\b ]");
    }

    @Override
    public void run() {
        System.out.println("p");
        while(true){
            try {
                future = proxy.produce(random.nextInt() % max_portion + 1);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            while(!future.isAvailable()){
                try {
                    sleep(10);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("Produced");
//            printResult(future.getData());
        }
    }
}
