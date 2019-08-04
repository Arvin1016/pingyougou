package entity;

import com.pinyougou.pojo.TbBrand;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Arvin
 * @Project Name : pinyougou-parent
 * @Package Name : entity
 * @Creation Date: 2019-08-03 22:18
 * @Description :分页结果类
 */
public class PageResult implements Serializable {

    private long total;//总记录数
    private List rows;//当前页记录数

    public PageResult() {
    }

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}

