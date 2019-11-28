public class Main {
    
    public static void main(String args[]){

        int buffer_size = 100;
        Buffer buffer = new Buffer(buffer_size);
        RequestQueue requestQueue = new RequestQueue();
        Proxy proxy = new Proxy(requestQueue, buffer);

        Scheduler scheduler = new Scheduler(requestQueue);
        scheduler.start();

        int Consumers_number = 5;
        int Producers_number = 5;
        
        Consumer[] Consumers = new Consumer[Consumers_number];
        for (int i = 0; i < Consumers_number; i++) {
            Consumers[i] = new Consumer( proxy,buffer_size/2);
        }

        Producer[] Producers = new Producer[Producers_number];
        for (int i = 0; i < Producers_number; i++) {
            Producers[i] = new Producer(proxy,buffer_size/2);
        }

        for (int i = 0; i < Consumers_number; i++) Consumers[i].start();
        for (int i = 0; i < Producers_number; i++) Producers[i].start();
    }
}
