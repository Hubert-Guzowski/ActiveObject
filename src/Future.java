public class Future {

    private boolean available = false;
    private Object data;

    public boolean isAvailable() {
        return available;
    }

    public Object getData(){
        return data;
    }

    public void setData(Object data){
        this.data = data;
        this.available = true;
    }
}
