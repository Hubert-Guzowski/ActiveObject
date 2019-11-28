import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {

    private BlockingQueue<MethodRequest> inputQueue;
    private Queue<MethodRequest> waiting;

    public RequestQueue() {
        this.inputQueue = new LinkedBlockingQueue<>();
        this.waiting = new LinkedList<>();
    }

    public void put(MethodRequest methodRequest) throws InterruptedException{
        this.inputQueue.put(methodRequest);
    }

    public MethodRequest getFirstIntput() throws InterruptedException{
        return inputQueue.take();
    }

    public MethodRequest getFirstWaiting() throws InterruptedException{
        return waiting.poll();
    }

    public MethodRequest peakFirstWaiting() throws InterruptedException{
        return waiting.peek();
    }

    public boolean waitingIsEmpty() {
        return waiting.isEmpty();
    }

    public ProductionRequest getFirstProductionRequest() throws InterruptedException{
        MethodRequest methodRequest = inputQueue.take();
        while(!(methodRequest instanceof ProductionRequest)){
            waiting.add(methodRequest);
            methodRequest = inputQueue.take();
        }
        return (ProductionRequest) methodRequest;
    }

    public ConsumptionRequest getFirstConsumptionRequest() throws InterruptedException{
        MethodRequest methodRequest = inputQueue.take();
        while(!(methodRequest instanceof ConsumptionRequest)){
            waiting.add(methodRequest);
            methodRequest = inputQueue.take();
        }
        return (ConsumptionRequest) methodRequest;
    }

    public void putToWaiting(MethodRequest methodRequest){
        this.waiting.add(methodRequest);
    }

}
