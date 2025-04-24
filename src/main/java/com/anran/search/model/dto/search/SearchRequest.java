package com.anran.search.model.dto.search;

import com.anran.search.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索内容
     */
    private String searchText;

    /**
     * 搜索类型
     */
    private String type;
}
