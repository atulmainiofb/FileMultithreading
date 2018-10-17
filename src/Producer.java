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
    private static final int BUFFER_SIZE=4096*1024;
    private int POOL_SIZE=10;
    ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

    public void produceFile(){
        String filePath = getFilePath();
        File file = new File(filePath);

        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = null;
            long size = file.length();
            long numberOfChunks = getNumberOfChunks(file);
            long chunkNumber = 1;
            while (chunkNumber<=numberOfChunks) {
               buffer = copy(is);
               FileChunk chunk = createFileChunk(buffer,numberOfChunks,chunkNumber);
               chunkNumber++;
               executor.submit(()->{
//                   try {
//                       int random=new Random().nextInt(10000);
//                       Thread.sleep(random);
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

    private long getNumberOfChunks(File file) throws IOException {
        long numberOfChunks=0;

        BufferedInputStream inputStream=null;
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(file);
            inputStream = new BufferedInputStream(fis);
            long cnt=1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while (cnt> 0){
                cnt = inputStream.read(buffer);
                numberOfChunks++;
            }
        }finally {
            if(fis !=null){
                fis.close();
                if(inputStream!=null){
                    inputStream.close();
                }
            }

        }

return numberOfChunks;
    }

    private FileChunk createFileChunk(byte[] buffer,long totalChunks,long chunkNumber) {
        return new FileChunk(chunkNumber,buffer,totalChunks);
    }


    private String getFilePath() {
        return "head-first-java.pdf";
    }

    public void setQueue(FileChunksQueue queue){
        this.queue = queue;
    }

    public byte[] copy(InputStream input)throws IOException{
        byte[] buffer = new byte[BUFFER_SIZE];
         input.read(buffer);
         return buffer;
    }

    public int copy1(InputStream input,byte[] buffer)throws IOException{
        return input.read(buffer);
    }


}
