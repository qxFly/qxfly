package fun.qxfly.entity;

public class FilePO {
    private Integer fileId;
    private String md5;
    private String name;
    private Long size;


    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }


    public FilePO(String name, String md5, Long size) {

        this.md5 = md5;
        this.name = name;
        this.size = size;
    }

    public FilePO() {
    }

    @Override
    public String toString() {
        return "FilePO{" +
                "fileId=" + fileId +
                ", md5='" + md5 + '\'' +

                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
