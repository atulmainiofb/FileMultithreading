import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Consumer {

    private FileChunksQueue queue;
    private long totalChunks;
    private long chunksReceived=1;
    private static final String TEMP = "tempFile";
    Map<Long,String> data = new HashMap<>();


    public void setQueue(FileChunksQueue queue) {
        this.queue = queue;
    }

    public void startReadingQueue() throws InterruptedException, IOException {
        FileChunk chunk = queue.getNextChunk();
        if (chunk != null) {
            totalChunks = chunk.getTotalChunks();
            System.out.println("chunkReceived " + chunk.getChunkNumber());
            createTempFileChunk(chunk);
            while (chunksReceived <= totalChunks) {
                chunk = queue.getNextChunk();
                if (chunk != null) {
                    createTempFileChunk(chunk);

                    System.out.println("chunkReceived " + chunk.getChunkNumber());
                    System.out.println("consumer received chunks " + chunksReceived + "total " + totalChunks);
                    chunksReceived++;
                }
            }
            makeFinalFile();
        }
    }

    private void makeFinalFile() throws IOException {
        System.out.println("making final file");
        sortData();
        File tgtFile = new File("finalNewFile");
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(tgtFile));
//            int i = 1;
//            while (i <= totalChunks) {
//                byte[] bytes = Files.readAllBytes(Paths.get(TEMP+ i));
//                outputStream.write(bytes);
//                i++;
//            }
            for (Map.Entry<Long,String> entry:data.entrySet()) {
                byte[] bytes = Files.readAllBytes(Paths.get(TEMP+ entry.getKey()));
                outputStream.write(bytes);
            }
            outputStream.flush();
            outputStream.close();
        } finally {
            if (outputStream != null)
                outputStream.close();
        }
    }

    private void sortData() {
        Map<Long,String> treeMap = new TreeMap<Long,String>(
                new Comparator<Long>() {
                    @Override
                    public int compare(Long o1, Long o2) {
                        return o2.compareTo(o1);//sort in descending order
                    }
                });
        data = treeMap;
    }

    private void createTempFileChunk(FileChunk chunk) throws IOException {
        BufferedOutputStream outputStream = null;
        try {
            String filePath = TEMP + chunk.getChunkNumber();
            File file = new File(filePath);
            data.put(chunk.getChunkNumber(),filePath);


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
