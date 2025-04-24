package com.anran.search;

import cn.hutool.json.JSONUtil;
import com.anran.search.model.entity.Picture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Test
    void testFetchPicture() throws IOException {
        int curPage = 1;
        String url = "https://cn.bing.com/images/search?q=%E7%BE%8E%E5%A5%B3%E5%9B%BE%E7%89%87&first" + curPage;

        Document doc = Jsoup.connect(url).get();
        Elements div = doc.select(".dgControl.waterfall");
        Elements ulList = div.select(".dgControl_list");
        List<Picture> pictures = new ArrayList<>();
        for(Element ul : ulList){
            Elements liList = ul.children();
            for(Element li : liList){
                // 取图片地址
                String m = li.select(".iusc").get(0).attr("m");
                Map<String, Object> map = JSONUtil.toBean(m, Map.class);
                String murl = (String) map.get("murl");
//                System.out.println(murl);
                // 取标题
                String title = li.select(".infopt").select(".inflnk").get(0).attr("aria-label");
                System.out.println(title);
                // 建立图片对象
                Picture picture = new Picture();
                if(title.equals("")) {
                    picture.setTitle("美女图片");
                } else picture.setTitle(title);
                picture.setUrl(murl);
                pictures.add(picture);
            }
        }
        System.out.println(pictures);
    }
}
//    @Test
//    void testFetchPassage() {
//        String json = "{category: 文章,current:"1", hiddenContent" ": " "true" "pageSize" ": " "12" "reviewStatus" ": " "1" "sortField" ": " "\"createTime\"" "sortOrder" ": " "\"descend\"""tags" ": " "[]}";
//        String url = "https://www.codefather.cn/api/post/list/page/vo";
//        String result = HttpRequest.post(url)
//                .body(json)
//                .execute().body();
//
//        System.out.println(result);
//    }
//}
