package com.anran.search.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.anran.search.model.dto.post.PostQueryRequest;
import com.anran.search.model.entity.Post;
import com.anran.search.model.vo.PostVO;
import com.anran.search.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务实现
 *
 */
@Service
@Slf4j
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent((int) pageNum);
        postQueryRequest.setPageSize((int) pageSize);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); // 请求属性
        HttpServletRequest request = servletRequestAttributes.getRequest(); // 从属性中拿到请求

        // 原本的获取数据的方法
        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request); // 这里不能传入 null
        // 从 ES 中筛选数据的方法
//        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
//        Page<PostVO> postVOPage = postService.getPostVOPage(postPage,request);
        return postVOPage;
    }
}




