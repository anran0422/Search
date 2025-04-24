package com.anran.search.datasource;


import com.anran.search.model.enums.SearchTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceRegistry {

    @Resource
    private PostDataSource postDataSource;
    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private UserDataSource userDataSource;
    private Map<String, DataSource> typeDataSourceMap;

    @PostConstruct
    public void init() {
        typeDataSourceMap = new HashMap() {{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
        }};
    }

    public DataSource getDataSource(String type) {
        if(typeDataSourceMap == null) {
            return null;
        }
        return typeDataSourceMap.get(type);
    }
}
