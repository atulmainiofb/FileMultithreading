public class FileChunk {

    private long chunkNumber;
    private byte[] data;
    private long totalChunks;

    public FileChunk(long chunkNumber, byte[] data, long totalChunks) {
        this.chunkNumber = chunkNumber;
        this.data = data;
        this.totalChunks = totalChunks;
    }

    public long getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(long chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(long totalChunks) {
        this.totalChunks = totalChunks;
    }
}
