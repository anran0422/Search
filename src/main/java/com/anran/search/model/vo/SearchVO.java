package com.anran.search.model.vo;

import com.google.gson.Gson;
import com.anran.search.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索
 */
@Data
public class SearchVO implements Serializable {

    private final static Gson gson = new Gson();

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;

    private List<Object> dataList;

    private static final long serialVersionUID = 1L;
}