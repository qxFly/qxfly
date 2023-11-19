package top.qxfly.pojo;

public class ChunkPO {
    private Integer chunkId;
    private String md5;
    private Integer index;

    public ChunkPO() {
    }

    public ChunkPO(String md5, Integer index) {
        this.md5 = md5;
        this.index = index;
    }

    public Integer getChunkId() {
        return chunkId;
    }

    public void setChunkId(Integer chunkId) {
        this.chunkId = chunkId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "ChunkPO{" +
                "chunkId=" + chunkId +
                ", md5='" + md5 + '\'' +
                ", index=" + index +
                '}';
    }
}
