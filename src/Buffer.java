import java.util.LinkedList;
import java.util.Queue;

public class Buffer {

    private Queue<Integer> buffer;
    private int size;
    private Long operationWeight;

    public Buffer(int size, Long operationWeight) {
        this.buffer = new LinkedList<>();
        this.operationWeight = operationWeight;
        this.size = size;
    }

    public int occupiedSpace(){
        return buffer.size();
    }

    public int freeSpace(){

        Long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < operationWeight){}

        return size - buffer.size();
    }

    public void add(int element){

        Long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < operationWeight){}

        this.buffer.add(element);
    }

    public int remove(){
        return buffer.poll();
    }
}
