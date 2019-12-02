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

    public void produce(int portion){
        myLock.lock();
        try {
            while(((ReentrantLock)(myLock)).hasWaiters(first_producent)) {
                try {
                    producents.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            while(buffer_size - queue.size() < portion){
                try {
                    first_producent.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            for(int i=0; i<portion; i++) queue.add(generator.nextInt());

            //sztuczna praca
            try {
                sleep(operationWeight);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            producents.signal();
            first_consument.signal();
        }
        finally {
            myLock.unlock();
        }
    }

    public void consume(int portion){
        myLock.lock();
        try {
            while(((ReentrantLock)(myLock)).hasWaiters(first_consument)) {
                try {
                    consuments.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            while(queue.size() < portion) {
                try {
                    first_consument.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            for(int i=0; i<portion; i++) queue.poll();

            //sztuczna praca
            try {
                sleep(operationWeight);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            consuments.signal();
            first_producent.signal();
        }
        finally {
            myLock.unlock();
        }
    }
}
