package com.anran.search.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 定义数据源接口(新接入的数据源必须实现）
 */
public interface DataSource<T> {
    /**
     * 搜索数据源规范接口
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
