public class Scheduler extends Thread{

    private RequestQueue requestQueue;

    public Scheduler(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    private void schedule() throws InterruptedException{
        while(true){
            MethodRequest methodRequest;
            if(requestQueue.waitingIsEmpty()){
                methodRequest = requestQueue.getFirstIntput();
                if(methodRequest.canBeCalled()) methodRequest.call();
                else{
                    requestQueue.putToWaiting(methodRequest);
                    MethodRequest methodRequest1;
                    if(methodRequest instanceof ProductionRequest){
                        methodRequest1 = requestQueue.getFirstConsumptionRequest();
                        methodRequest1.call();
                    }
                    else if(methodRequest instanceof ConsumptionRequest){
                        methodRequest1 = requestQueue.getFirstProductionRequest();
                        methodRequest1.call();
                    }
                }
            }
            else{
                methodRequest = requestQueue.peakFirstWaiting();
                if(methodRequest.canBeCalled()){
                    requestQueue.getFirstWaiting();
                    methodRequest.call();
                }
                else{
                    MethodRequest methodRequest1;
                    if(methodRequest instanceof ProductionRequest){
                        methodRequest1 = requestQueue.getFirstConsumptionRequest();
                        methodRequest1.call();
                    }
                    else if(methodRequest instanceof ConsumptionRequest){
                        methodRequest1 = requestQueue.getFirstProductionRequest();
                        methodRequest1.call();
                    }
                }
            }

        }
    }

    @Override
    public void run(){
       try{
           this.schedule();
       }catch (InterruptedException e)
       {
           e.printStackTrace();
       }
    }
}
