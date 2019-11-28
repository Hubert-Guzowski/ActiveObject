public class Proxy {

    private RequestQueue requestQueue;
    private Buffer buffer;

    public Proxy(RequestQueue requestQueue, Buffer buffer) {
        this.requestQueue = requestQueue;
        this.buffer = buffer;
    }

    public Future produce(int portion) throws InterruptedException{
        Future future = new Future();
        this.requestQueue.put(new ProductionRequest(buffer,future, portion));
        return future;
    }

    public Future consume(int portion) throws InterruptedException{
        Future future = new Future();
        this.requestQueue.put(new ConsumptionRequest(buffer, future, portion));
        return future;
    }

}
