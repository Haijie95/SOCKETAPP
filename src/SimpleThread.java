public class SimpleThread extends Thread {
    
    public SimpleThread() {}

    @Override
    public void run(){
        System.out.println("Thread started execution + "+Thread.currentThread().getName());
        try{
            Thread.sleep(100);

        }catch(InterruptedException e){

        }//wait for some time
        System.out.println("Thread stopped execution + "+Thread.currentThread().getName());
    }
}

