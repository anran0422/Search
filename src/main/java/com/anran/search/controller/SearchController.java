package com.anran.search.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.anran.search.common.BaseResponse;
import com.anran.search.common.ErrorCode;
import com.anran.search.common.ResultUtils;
import com.anran.search.exception.ThrowUtils;
import com.anran.search.manager.SearchFacade;
import com.anran.search.model.dto.post.PostQueryRequest;
import com.anran.search.model.dto.search.SearchRequest;
import com.anran.search.model.dto.user.UserQueryRequest;
import com.anran.search.model.entity.Picture;
import com.anran.search.model.vo.PostVO;
import com.anran.search.model.vo.SearchVO;
import com.anran.search.model.vo.UserVO;
import com.anran.search.service.PictureService;
import com.anran.search.service.PostService;
import com.anran.search.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 获取图片接口
 */
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> search(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
//        String type = searchRequest.getType();
//        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);

        String searchText = searchRequest.getSearchText();
        ThrowUtils.throwIf(StringUtils.isBlank(searchText), ErrorCode.PARAMS_ERROR); // 封装的异常工具

        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        Page<UserVO> userVOPage = userService.listUserVoBypage(userQueryRequest);

        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);

        Page<Picture> picturePage = pictureService.listPicture(searchText, 1, 10);

        SearchVO searchVO = new SearchVO();
        searchVO.setPictureList(picturePage.getRecords()); // page 对象获得 records 得到 List
        searchVO.setUserList(userVOPage.getRecords());
        searchVO.setPostList(postVOPage.getRecords());
        return ResultUtils.success(searchVO);
    }

    @PostMapping("/yibu")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        SearchVO searchVO = searchFacade.searchAll(searchRequest, request);
        return ResultUtils.success(searchVO);
    }

    @PostMapping("/adapter")
    public BaseResponse<SearchVO> searchAdapter(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        SearchVO searchVO = searchFacade.searchAdapter(searchRequest, request);
        return ResultUtils.success(searchVO);
    }
}
