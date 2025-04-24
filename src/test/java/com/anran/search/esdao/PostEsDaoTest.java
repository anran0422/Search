package com.anran.search.esdao;

import com.anran.search.model.dto.post.PostEsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//@SpringBootTest
public class PostEsDaoTest {

//    @Resource
    private PostEsDao postEsDao;


    @Test
    void testSelect() {
        System.out.println(postEsDao.count());
        Page<PostEsDTO> PostPage = postEsDao.findAll(
                PageRequest.of(0, 5, Sort.by("createTime"))); //分页查询
        List<PostEsDTO> postList = PostPage.getContent();
        System.out.println(postList);
        Optional<PostEsDTO> byId = postEsDao.findById(1L); // 根据 Id 查询
        System.out.println(byId);
    }

    @Test
    void testAdd() {
        PostEsDTO postEsDTO = new PostEsDTO();
        postEsDTO.setId(2L);
        postEsDTO.setTitle("编程");
        postEsDTO.setContent("编程是高深莫测的！");
        postEsDTO.setTags(Arrays.asList("java", "python"));
        postEsDTO.setUserId(1L);
        postEsDTO.setCreateTime(new Date());
        postEsDTO.setUpdateTime(new Date());
        postEsDTO.setIsDelete(0);
        postEsDao.save(postEsDTO);
        System.out.println(postEsDTO.getId());
    }

    @Test
    void testFindByTitle() {
        List<PostEsDTO> postList = postEsDao.findByTitle("编程");
        System.out.println(postList);
    }
}
