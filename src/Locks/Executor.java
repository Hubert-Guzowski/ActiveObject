package Locks;

import java.io.FileNotFoundException;
import java.util.Random;

public class Executor {

    public static void timeLimitedLocks(String fileName,
                                        int producerNumber,
                                        int consumerNumber,
                                        int bufferSize,
                                        int maxPortionProducer,
                                        int maxPortionConsumer,
                                        long bufferWork,
                                        long producerWork,
                                        long consumerWork) throws InterruptedException {


        Consumer[] consumers = new Consumer[consumerNumber];
        Producer[] producers = new Producer[producerNumber];
        MonitoredBuffer monitoredBuffer = new MonitoredBuffer(bufferSize, bufferWork);
        int seed = (new Random()).nextInt();

        for(int i=0; i<consumerNumber; i++) consumers[i] = new Consumer(monitoredBuffer, seed, maxPortionConsumer, consumerWork, true, 30000, 0);
        for(int i=0; i<producerNumber; i++) producers[i] = new Producer(monitoredBuffer, seed, maxPortionProducer, producerWork, true, 30000, 0);

        long timestamp = System.currentTimeMillis();
        for(Consumer consumer : consumers) consumer.start();
        for(Producer producer : producers) producer.start();

        long productions = 0;
        long consumtions = 0;

        while(System.currentTimeMillis() - timestamp < 3000) {};
        for(Consumer consumer : consumers) consumer.interrupt();
        for(Producer producer : producers) producer.interrupt();

        for(Consumer consumer : consumers) {
//            consumer.join();
            consumtions += consumer.getAmount();
        }
        for(Producer producer : producers) {
//            producer.join();
            productions += producer.getAmount();
        }

        System.out.println("Consumptions: " + consumtions);
        System.out.println("Productions: " + productions);

    }

    public static void amountLimitedLocks(String fileName,
                                        int producerNumber,
                                        int consumerNumber,
                                        int bufferSize,
                                        int maxPortionProducer,
                                        int maxPortionConsumer,
                                        long bufferWork,
                                        long producerWork,
                                        long consumerWork,
                                          int amountLimit) throws InterruptedException {


        Consumer[] consumers = new Consumer[consumerNumber];
        Producer[] producers = new Producer[producerNumber];
        MonitoredBuffer monitoredBuffer = new MonitoredBuffer(bufferSize, bufferWork);
        int seed = (new Random()).nextInt();

        for(int i=0; i<consumerNumber; i++) consumers[i] = new Consumer(monitoredBuffer, seed, maxPortionConsumer, consumerWork, false, 0, amountLimit);
        for(int i=0; i<producerNumber; i++) producers[i] = new Producer(monitoredBuffer, seed, maxPortionProducer, producerWork, false, 0, amountLimit);

        long timestamp = System.currentTimeMillis();
        for(Consumer consumer : consumers) consumer.start();
        for(Producer producer : producers) producer.start();

        for(Consumer consumer : consumers) consumer.join();
        for(Producer producer : producers) producer.join();

        System.out.println("Time: " + ((double)(System.currentTimeMillis() - timestamp))/1000);

    }

    public static void main(String[] args) {
        try {
            amountLimitedLocks("./example.txt", 100, 100, 1000, 500, 500, 0, 0, 0, 100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}