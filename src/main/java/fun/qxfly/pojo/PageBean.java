package fun.qxfly.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean<T> implements Serializable {

    private int currPage; //当前页数
    private int pageSize; //每页显示数
    private int totalPage; //总页数
    private int totalRecord; //总的记录数
    private List<T> data; //当前页面的数据集合
    private int start;
    private int end;

    public PageBean(int currPage, int pageSize, int totalRecord) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        //计算总页数
        this.totalPage = totalRecord % pageSize == 0 ? (totalRecord / pageSize) : (totalRecord / pageSize + 1);

        //计算每页的起始下标
        this.start = (currPage - 1) * pageSize;
        this.end = this.start + pageSize;
    }
}
