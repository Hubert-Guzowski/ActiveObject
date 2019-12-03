import java.util.List;
import java.util.Random;

public class Producer extends Thread{

    private Proxy proxy;
    private int max_portion;
    private Future future;
    private Random random;
    private boolean runFlag;
    private Long timestamp;
    private Long duration;
    private int amount;


    public Producer(Proxy proxy, int max_portion, boolean runFlag, long seed, Long timestamp, Long duration){
        this(proxy, max_portion, runFlag, seed, timestamp, duration, 0);
    }

    public Producer(Proxy proxy, int max_portion, boolean runFlag, long seed, Long timestamp, int amount){
        this(proxy, max_portion, runFlag, seed, timestamp, 0L, amount);
    }

    private Producer(Proxy proxy, int max_portion, boolean runFlag, long seed, Long timestamp, Long duration, int amount){
        this.proxy = proxy;
        this.max_portion = max_portion;
        this.random = new Random(seed);
        this.runFlag = runFlag;
        this.timestamp = timestamp;
        this.duration = duration;
        this.amount = amount;
    }

    private void printResult(Object result){
        List<Integer> produced = (List<Integer>) result;
        System.out.println("Produced: [");
        for(Integer i : produced) System.out.print(i + ",");
        System.out.println("\b ]");
    }

    private int timeLimitedRun(){
        amount = 0;

        while(System.nanoTime() - timestamp < duration){
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
            amount += 1;
            System.out.println("Produced");
        }

        return amount;

    }

    private Long amountLimitedRun(){
        int currentlyProduced = 0;

        while(currentlyProduced < amount){
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
            currentlyProduced += 1;
            System.out.println("Produced");
        }

        return System.nanoTime() - timestamp;
    }

    @Override
    public void run() {
        System.out.println("p");

        if(runFlag){
            System.out.println(timeLimitedRun());
        }else{
            System.out.println(amountLimitedRun());
        }
    }
}
