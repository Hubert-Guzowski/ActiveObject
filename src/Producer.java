import java.util.List;
import java.util.Random;

public class Producer extends Thread{

    private Proxy proxy;
    private int max_portion;
    private Future future;
    private Random random;
    private boolean runFlag;
    private Long timestamp;
    private int amount;


    public Producer(Proxy proxy, int max_portion, boolean runFlag, long seed, Long timestamp){
        this(proxy, max_portion, runFlag, seed, timestamp, 0);
    }

    public Producer(Proxy proxy, int max_portion, boolean runFlag, long seed, int amount){
        this(proxy, max_portion, runFlag, seed, 0L, amount);
    }

    private Producer(Proxy proxy, int max_portion, boolean runFlag, long seed, Long timestamp, int amount){
        this.proxy = proxy;
        this.max_portion = max_portion;
        this.random = new Random(seed);
        this.runFlag = runFlag;
        this.timestamp = timestamp;
        this.amount = amount;
    }

    private void printResult(Object result){
        List<Integer> produced = (List<Integer>) result;
        System.out.println("Produced: [");
        for(Integer i : produced) System.out.print(i + ",");
        System.out.println("\b ]");
    }

    private void timeLimitedRun(){

    }

    private void amountLimitedTime(){

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
