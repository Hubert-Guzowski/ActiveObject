import java.util.LinkedList;

public class ConsumptionRequest implements MethodRequest {

    private Buffer buffer;
    private Future result;
    private int portion;

    public ConsumptionRequest(Buffer buffer, Future result, int portion) {
        this.buffer = buffer;
        this.result = result;
        this.portion = portion;
    }

    @Override
    public boolean canBeCalled(){
        return buffer.occupiedSpace() >= this.portion;
    }

    @Override
    public void call() {
        LinkedList<Integer> consumed = new LinkedList<>();
        for(int i=0; i<portion; i++) consumed.add(buffer.remove());
        result.setData(consumed);
    }
}
