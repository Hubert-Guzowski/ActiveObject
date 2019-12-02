package Locks;

import java.util.Random;

import static java.lang.Math.abs;

public class Consumer extends Thread {

    private final MonitoredBuffer monitoredBuffer;
    private Random random;
    private int max_portion;

    private long workTime;

    private boolean runFlag;
    private long timeStamp;
    private long timeLimit;

    private long amount = 0;
    private int amountLimit;

    public Consumer(MonitoredBuffer monitoredBuffer, long seed, int max_portion, long workTime, boolean runFlag, long timeLimit, int amountLimit) {
        this.monitoredBuffer = monitoredBuffer;
        this.random = new Random(seed);
        this.max_portion = max_portion;
        this.workTime = workTime;
        this.runFlag = runFlag;
        this.timeLimit = timeLimit;
        this.amountLimit = amountLimit;
    }

    private boolean canRun(){
        if(runFlag) return System.nanoTime() - this.timeStamp < timeLimit;
        else return this.amount < this.amountLimit;
    }

    @Override
    public void run() {
        this.timeStamp = System.nanoTime();

        while(canRun()){
            int portion = abs(random.nextInt()) % max_portion + 1;
            monitoredBuffer.consume(portion);
            this.amount += portion;

            try {
                sleep(workTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
