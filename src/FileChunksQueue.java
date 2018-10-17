import java.util.concurrent.ArrayBlockingQueue;

public class FileChunksQueue {

    private ArrayBlockingQueue<FileChunk> queue;

    private int QUEUE_SIZE=100000;

    public FileChunksQueue() {
        queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    }

    public boolean addChunkToQueue(FileChunk chunk) {
        System.out.println("adding chunk to queue "+chunk.getChunkNumber());
        queue.add(chunk);
        return true;
    }

    public FileChunk getNextChunk() throws InterruptedException {
        FileChunk chunk = queue.take();
        return chunk;
    }

}
