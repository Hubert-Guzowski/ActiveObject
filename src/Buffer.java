import java.util.LinkedList;
import java.util.Queue;

public class Buffer {

    private Queue<Integer> buffer;
    private int size;

    public Buffer(int size) {
        this.buffer = new LinkedList<>();
        this.size = size;
    }

    public int occupiedSpace(){
        return buffer.size();
    }

    public int freeSpace(){
        return size - buffer.size();
    }

    public void add(int element){
        this.buffer.add(element);
    }

    public int remove(){
        return buffer.poll();
    }
}
