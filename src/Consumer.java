import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Consumer {

    private FileChunksQueue queue;
    private int totalChunks;
    private int chunksReceived;
    private static final String TEMP = "tempFile";


    public void setQueue(FileChunksQueue queue) {
        this.queue = queue;
    }

    public void startReadingQueue() throws InterruptedException, IOException {
        FileChunk chunk = queue.getNextChunk();
        if (chunk != null) {
            totalChunks = chunk.getTotalChunks();
            System.out.println("chunkReceived " + chunk.getChunkNumber());
            chunksReceived = 1;
            createTempFileChunk(chunk);
            while (chunksReceived <= totalChunks) {
                chunk = queue.getNextChunk();
                if (chunk != null) {
                    System.out.println("chunkReceived " + chunk.getChunkNumber());
                    createTempFileChunk(chunk);
                    chunksReceived++;
                    System.out.println("consumer received chunks " + chunksReceived + "total " + totalChunks);
                }
            }
            makeFinalFile();
        }
    }

    private void makeFinalFile() throws IOException {
        System.out.println("making final file");
        File tgtFile = new File("finalNewFile");
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(tgtFile));
            int i = 1;
            while (i <= totalChunks) {
                byte[] bytes = Files.readAllBytes(Paths.get(TEMP+ i));
                outputStream.write(bytes);
                i++;
            }
            outputStream.flush();
            outputStream.close();
        } finally {
            if (outputStream != null)
                outputStream.close();
        }
    }

    private void createTempFileChunk(FileChunk chunk) throws IOException {
        BufferedOutputStream outputStream = null;
        try {
            String filePath = TEMP + chunk.getChunkNumber();
            File file = new File(filePath);


            file.createNewFile();
            System.out.println(file.getAbsolutePath());


            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(chunk.getData());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }


}
