public class Application {

    public static void main(String[] args) {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        FileChunksQueue queue = new FileChunksQueue();
        producer.setQueue(queue);
        Thread t1=new Thread(()->{producer.produceFile();});
        consumer.setQueue(queue);
        Thread t2=new Thread(()->{
            try{
                consumer.startReadingQueue();
            }catch (Exception ex){
                ex.printStackTrace();
            }});
        t1.start();
        t2.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.produceFile();
    }


}
