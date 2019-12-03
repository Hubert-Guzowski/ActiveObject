public class Main {

    private static void programExecution(){

    }
    
    public static void main(String args[]){

        int buffer_size = 100;
        Long operationWeight = 10000000L;


        boolean ifTimeLimitedRun = false;
        Long duration = 20000000000L;
        int amount = 20;


        int Consumers_number = 5;
        long consumersSeed = 123512532L;


        int Producers_number = 5;
        long producersSeed = 8376259234L;

        Buffer buffer = new Buffer(buffer_size, operationWeight);
        RequestQueue requestQueue = new RequestQueue();
        Proxy proxy = new Proxy(requestQueue, buffer);

        Scheduler scheduler = new Scheduler(requestQueue);
        scheduler.start();
        
//        Consumer[] Consumers = new Consumer[Consumers_number];
        for (int i = 0; i < Consumers_number; i++) {
            if(ifTimeLimitedRun){
                new Consumer(proxy, buffer_size/2, ifTimeLimitedRun, consumersSeed, System.nanoTime(), duration).start();
//                Consumers[i] = new Consumer(proxy, buffer_size/2, ifTimeLimitedRun, consumersSeed, System.nanoTime(), duration);
            }else{
                new Consumer(proxy, buffer_size/2, ifTimeLimitedRun, consumersSeed, System.nanoTime(), amount).start();
//                Consumers[i] = new Consumer(proxy, buffer_size/2, ifTimeLimitedRun, consumersSeed, System.nanoTime(), amount);
            }

        }

//        Producer[] Producers = new Producer[Producers_number];
        for (int i = 0; i < Producers_number; i++) {
            if(ifTimeLimitedRun){
                new Producer(proxy, buffer_size/2, ifTimeLimitedRun, producersSeed, System.nanoTime(), duration).start();
//                Producer[i] = new Consumer(proxy, buffer_size/2, ifTimeLimitedRun, producersSeed, System.nanoTime(), duration);
            }else{
                new Producer(proxy, buffer_size/2, ifTimeLimitedRun, producersSeed, System.nanoTime(), amount).start();
//                Producer[i] = new Consumer(proxy, buffer_size/2, ifTimeLimitedRun, producersSeed, System.nanoTime(), amount);
            }
        }

//        for (int i = 0; i < Consumers_number; i++) Consumers[i].start();
//        for (int i = 0; i < Producers_number; i++) Producers[i].start();
    }
}
