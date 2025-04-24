package com.anran.search.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.anran.search.model.dto.user.UserQueryRequest;
import com.anran.search.model.vo.UserVO;
import com.anran.search.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户数据源接口
 */
@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent((int) pageNum);
        userQueryRequest.setPageSize((int) pageSize);
        Page<UserVO> userVOPage = userService.listUserVoBypage(userQueryRequest);
        return userVOPage;
    }
}
