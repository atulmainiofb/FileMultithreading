import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Producer {

    private FileChunksQueue queue;
    private ArrayBlockingQueue queue1;
    private Map<Integer,String> data = new HashMap<>();
    private static final int BUFFER_SIZE=4096;
    private int POOL_SIZE=10;
    ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

    public void produceFile(){
        String filePath = getFilePath();
        File file = new File(filePath);

        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = null;
            long cnt = 1;
            long size = file.length();
            int numberOfChunks=(int)size/BUFFER_SIZE+1;
            int chunkNumber = 1;
            while (chunkNumber<=numberOfChunks) {
               buffer = copy(is);
               FileChunk chunk = createFileChunk(buffer,numberOfChunks,chunkNumber);
               chunkNumber++;
               executor.submit(()->{
//                   try {
//                       int random=new Random().nextInt(10000);
//                       //Thread.sleep(random);
//                   } catch (InterruptedException e) {
//                       e.printStackTrace();
//                   }

                       queue.addChunkToQueue(chunk);

               });

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private FileChunk createFileChunk(byte[] buffer,int totalChunks,int chunkNumber) {
        return new FileChunk(chunkNumber,buffer,totalChunks);
    }


    private String getFilePath() {
        return "mongoData.txt";
    }

    public void setQueue(FileChunksQueue queue){
        this.queue = queue;
    }

    public byte[] copy(InputStream input)throws IOException{
        byte[] buffer = new byte[BUFFER_SIZE];
         input.read(buffer);
         return buffer;
    }
}
