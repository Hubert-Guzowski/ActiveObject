import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.abs;

public class ProductionRequest implements MethodRequest {

    private Buffer buffer;
    private Future result;
    private int portion;
    private Random random;

    public ProductionRequest(Buffer buffer, Future result, int portion) {
        this.buffer = buffer;
        this.result = result;
        this.portion = portion;
        this.random = new Random();
    }

    @Override
    public boolean canBeCalled() {
        return buffer.freeSpace() >= this.portion;
    }

    @Override
    public void call() {
        LinkedList<Integer> produced = new LinkedList<>();
        for(int i=0; i<portion; i++)
        {
            int rand = abs(random.nextInt()) % 100 + 1;
            buffer.add(rand);
            produced.add(rand);
        }
        result.setData(produced);
    }
}
