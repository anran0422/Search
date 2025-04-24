package com.anran.search.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.anran.search.common.ErrorCode;
import com.anran.search.datasource.*;
import com.anran.search.exception.BusinessException;
import com.anran.search.exception.ThrowUtils;
import com.anran.search.model.dto.post.PostQueryRequest;
import com.anran.search.model.dto.search.SearchRequest;
import com.anran.search.model.dto.user.UserQueryRequest;
import com.anran.search.model.entity.Picture;
import com.anran.search.model.enums.SearchTypeEnum;
import com.anran.search.model.vo.PostVO;
import com.anran.search.model.vo.SearchVO;
import com.anran.search.model.vo.UserVO;
import com.anran.search.service.PictureService;
import com.anran.search.service.PostService;
import com.anran.search.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private DataSourceRegistry dataSourceRegistry;
    @Resource
    private PictureService pictureService;
    @Resource
    private PostService postService;
    @Resource
    private UserService userService;
    public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        String searchText = searchRequest.getSearchText();
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        if (ObjectUtils.isEmpty(searchTypeEnum)) {
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userService.listUserVoBypage(userQueryRequest);
                return userVOPage;
            });

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                return postVOPage;
            });

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureService.listPicture(searchText, 1, 10);
                return picturePage;
            });

            CompletableFuture.allOf(userTask, postTask, pictureTask).join();
            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();

                SearchVO searchVO = new SearchVO();
                searchVO.setPictureList(picturePage.getRecords()); // page 对象获得 records 得到 List
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {
            SearchVO searchVO = new SearchVO();
            switch (searchTypeEnum) {
                case POST:
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setSearchText(searchText);
                    Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                    searchVO.setPostList(postVOPage.getRecords());
                    break;
                case USER:
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userVOPage = userService.listUserVoBypage(userQueryRequest);
                    searchVO.setUserList(userVOPage.getRecords());
                    break;
                case PICTURE:
                    Page<Picture> picturePage = pictureService.listPicture(searchText, 1, 10);
                    searchVO.setPictureList(picturePage.getRecords());
                    break;
                default:
            }
            return searchVO;
        }
    }

    public SearchVO searchAdapter(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);

        // doSearch 所需变量全部引入
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();

        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        if (searchTypeEnum == null) {
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize);
                return userVOPage;
            });

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postDataSource.doSearch(searchText, current, pageSize);
                return postVOPage;
            });

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureDataSource.doSearch(searchText, current, pageSize);
                return picturePage;
            });

            CompletableFuture.allOf(userTask, postTask, pictureTask).join();
            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();

                SearchVO searchVO = new SearchVO();
                searchVO.setPictureList(picturePage.getRecords()); // page 对象获得 records 得到 List
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else { // 检测参数不为空
            SearchVO searchVO = new SearchVO();

            DataSource dataSource = dataSourceRegistry.getDataSource(type);
            Page page = dataSource.doSearch(searchText, current, pageSize);

            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
