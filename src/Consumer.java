import java.util.List;
import java.util.Random;

public class Consumer extends Thread {

    private Proxy proxy;
    private int max_portion;
    private Future future;
    private Random random;
    private boolean runFlag;
    private Long timestamp;
    private Long duration;
    private long worktime;
    private int amount;


    public Consumer(Proxy proxy, int max_portion, boolean runFlag, long seed, Long timestamp, Long duration, long worktime){
        this(proxy, max_portion, runFlag, seed, timestamp, duration, 0, worktime);
    }

    public Consumer(Proxy proxy, int max_portion, boolean runFlag, long seed, Long timestamp, int amount, long worktime){
        this(proxy, max_portion, runFlag, seed, timestamp, 0L, amount, worktime);
    }

    private Consumer(Proxy proxy, int max_portion, boolean runFlag, long seed, Long timestamp, Long duration, int amount, long worktime){
        this.proxy = proxy;
        this.max_portion = max_portion;
        this.random = new Random(seed);
        this.runFlag = runFlag;
        this.timestamp = timestamp;
        this.duration = duration;
        this.amount = amount;
        this.worktime = worktime;
    }

    private void printResult(Object result){
        List<Integer> produced = (List<Integer>) result;
        System.out.println("Consumed: [");
        for(Integer i : produced) System.out.print(i + ",");
        System.out.println("\b ]");
    }

    private int timeLimitedRun(){
        amount = 0;

        while(System.nanoTime() - timestamp < duration){
            try {
                future = proxy.consume(random.nextInt() % max_portion + 1);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            try {
                sleep(worktime);
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
        }

        return amount;
    }

    private Long amountLimitedRun(){
        int currentlyConsumed = 0;

        while(currentlyConsumed < amount){
            try {
                future = proxy.consume(random.nextInt() % max_portion + 1);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            try {
                sleep(worktime);
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
            currentlyConsumed += 1;
        }

        return System.nanoTime() - timestamp;
    }

    @Override
    public void run() {

        if(runFlag){
            System.out.println(timeLimitedRun());
        }else{
            System.out.println(amountLimitedRun());
        }
    }
}
