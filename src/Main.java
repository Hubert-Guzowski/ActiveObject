import Locks.Executor;

public class Main {

    private static void programExecution(){

    }
    
    public static void main(String args[]){

        int buffer_size = 100;
        Long operationWeight = 10L;


        boolean ifTimeLimitedRun = true;
        Long duration = 20000L;
        int amount = 20;


        int Consumers_number = 5;
        int max_portion_consumers = 50;
        long consumersSeed = 123512532L;
        long worktimeConsumers = 10L;


        int Producers_number = 5;
        int max_portion_producers = 50;
        long producersSeed = 8376259234L;
        long worktimeProducers = 10L;

        Buffer buffer = new Buffer(buffer_size, operationWeight);
        RequestQueue requestQueue = new RequestQueue();
        Proxy proxy = new Proxy(requestQueue, buffer);

        Scheduler scheduler = new Scheduler(requestQueue);
        scheduler.start();

//        System.out.println("Starting active object threads\n");
        
        for (int i = 0; i < Consumers_number; i++) {
            if(ifTimeLimitedRun){
                new Consumer(proxy, max_portion_consumers, ifTimeLimitedRun, consumersSeed, System.currentTimeMillis(), duration, worktimeConsumers).start();
            }else{
                new Consumer(proxy, max_portion_consumers, ifTimeLimitedRun, consumersSeed, System.currentTimeMillis(), amount, worktimeConsumers).start();
            }
        }

        for (int i = 0; i < Producers_number; i++) {
            if(ifTimeLimitedRun){
                new Producer(proxy, max_portion_producers, ifTimeLimitedRun, producersSeed, System.currentTimeMillis(), duration, worktimeProducers).start();
            }else{
                new Producer(proxy, max_portion_producers, ifTimeLimitedRun, producersSeed, System.currentTimeMillis(), amount, worktimeProducers).start();
            }
        }

//        System.out.println("Starting locks threads\n");

        if(ifTimeLimitedRun){
            try {
                Executor.timeLimitedLocks("./example.txt", Producers_number, Consumers_number, buffer_size,
                        max_portion_producers, max_portion_consumers, producersSeed, consumersSeed, operationWeight,
                        worktimeProducers, worktimeConsumers, duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Executor.amountLimitedLocks("./example.txt", Producers_number, Consumers_number, buffer_size,
                        max_portion_producers, max_portion_consumers, producersSeed, consumersSeed, operationWeight,
                        worktimeProducers, worktimeConsumers, amount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
