package Locks;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

public class MonitoredBuffer {

    private final Queue<Integer> queue = new LinkedList<>();
    private final int buffer_size;
    private final long operationWeight;

    private final Lock myLock = new ReentrantLock();
    private final Condition producents = myLock.newCondition();
    private final Condition consuments = myLock.newCondition();
    private final Condition first_producent = myLock.newCondition();
    private final Condition first_consument = myLock.newCondition();
    private boolean first_producent_empty = true;
    private boolean first_consument_empty = true;

    private final Random generator = new Random();

    public MonitoredBuffer(int buffer_size, long operationWeight) {
        this.buffer_size = buffer_size;
        this.operationWeight = operationWeight;
    }

    public void produce(int portion) throws InterruptedException {
        myLock.lock();
        try {
            while(((ReentrantLock)(myLock)).hasWaiters(first_producent)) {
                    producents.await();
            }
            while(buffer_size - queue.size() < portion){

                    first_producent.await();

            }
            for(int i=0; i<portion; i++) {
                sleep(operationWeight);
                queue.add(generator.nextInt());
            }


            producents.signal();
            first_consument.signal();
        }
        finally {
            myLock.unlock();
        }
    }

    public void consume(int portion) throws InterruptedException {
        myLock.lock();
        try {
            while(((ReentrantLock)(myLock)).hasWaiters(first_consument)) {
                consuments.await();
            }
            while(queue.size() < portion) {
                first_consument.await();
            }
            for(int i=0; i<portion; i++){
                sleep(operationWeight);
                queue.poll();
            }


            consuments.signal();
            first_producent.signal();
        }
        finally {
            myLock.unlock();
        }
    }
}
