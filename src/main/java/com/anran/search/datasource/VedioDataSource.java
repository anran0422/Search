package com.anran.search.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class VedioDataSource implements DataSource<Object>{

    @Override
    public Page<Object> doSearch(String searchText, long pageNum, long pageSize) {
        return null;
    }
}
