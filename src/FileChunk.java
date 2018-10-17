import java.util.Arrays;

public class FileChunk {

    private int chunkNumber;
    private byte[] data;
    private int totalChunks;

    public FileChunk(int chunkNumber, byte[] data, int totalChunks) {
        this.chunkNumber = chunkNumber;
        this.data = data;
        this.totalChunks = totalChunks;
    }

    public int getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(int chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }

    @Override
    public String toString() {
        return "FileChunk{" +
                "chunkNumber=" + chunkNumber +
                ", data=" + Arrays.toString(data) +
                ", totalChunks=" + totalChunks +
                '}';
    }
}
