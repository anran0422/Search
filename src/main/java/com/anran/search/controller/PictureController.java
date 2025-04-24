package com.anran.search.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.anran.search.common.BaseResponse;
import com.anran.search.common.ErrorCode;
import com.anran.search.common.ResultUtils;
import com.anran.search.exception.ThrowUtils;
import com.anran.search.model.dto.Picture.PictureQueryRequest;
import com.anran.search.model.entity.Picture;
import com.anran.search.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 获取图片接口
 */
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取图片（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                        HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.listPicture(searchText, current, size);
        return ResultUtils.success(picturePage);
    }
}
