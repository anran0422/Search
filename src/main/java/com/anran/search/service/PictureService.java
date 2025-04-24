package com.anran.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.anran.search.model.entity.Picture;


/**
 * 图片服务
 */
public interface PictureService{
    Page<Picture> listPicture(String searchText, long pageNum, long pageSize);
}
