package com.anran.search.datasource;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.anran.search.common.ErrorCode;
import com.anran.search.exception.BusinessException;
import com.anran.search.model.entity.Picture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片服务实现类
 */
@Service
public class PictureDataSource implements DataSource<Picture> {

    @Override
    public Page<Picture> doSearch(String searchText, long pageNum, long pageSize) {
        long curPage = (pageNum - 1) * pageSize;
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s",searchText, curPage);

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片数据获取失败");
        }
        Elements div = doc.select(".dgControl.waterfall");
        Elements ulList = div.select(".dgControl_list");
        List<Picture> pictures = new ArrayList<>();
        for (Element ul : ulList) {
            Elements liList = ul.children();
            for (Element li : liList) {
                // 限制用户搜索的图片条数
                if(pictures.size() > pageSize) {
                    break;
                }
                // 取图片地址
                String m = li.select(".iusc").get(0).attr("m");
                Map<String, Object> map = JSONUtil.toBean(m, Map.class);
                String murl = (String) map.get("murl");
//                System.out.println(murl);
                // 取标题
                String title = li.select(".infopt").select(".inflnk").get(0).attr("aria-label");
//                System.out.println(title);
                // 建立图片对象
                Picture picture = new Picture();
                if (title.equals("")) {
                    picture.setTitle("美女图片");
                } else picture.setTitle(title);
                picture.setUrl(murl);
                pictures.add(picture);
            }
        }
        Page<Picture> picturePage = new Page<>(pageNum,pageSize);
        picturePage.setRecords(pictures);
        return picturePage;
    }
}
