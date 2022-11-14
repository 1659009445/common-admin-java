package com.huii.admin.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private int totalCount;

    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 当前页数
     */
    private int currentPage;

    /**
     * 数据集合
     */
    private List<?> data;

    /**
     * 参数集合
     */
    private List<?> param;

    public PageUtil(IPage<?> page) {
        this.data = page.getRecords();
        this.totalCount = (int)page.getTotal();
        this.pageSize = (int)page.getSize();
        this.currentPage = (int)page.getCurrent();
        this.totalPage = (int)page.getPages();
    }

}
