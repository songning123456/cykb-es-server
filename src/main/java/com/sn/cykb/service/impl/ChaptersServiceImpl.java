package com.sn.cykb.service.impl;

import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.feign.FeignClientQuery;
import com.sn.cykb.service.ChaptersService;
import com.sn.cykb.util.EsConvertUtil;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:58
 */
@Service
public class ChaptersServiceImpl implements ChaptersService {

    @Autowired
    private ElasticSearchDao elasticSearchDao;
    @Autowired
    private FeignClientQuery feignClientQuery;

    @Override
    public CommonDTO<ChaptersDTO> firstChapter(String novelsId) throws Exception {
        CommonDTO<ChaptersDTO> commonDTO = new CommonDTO<>();
        ElasticSearch chapterEsSearch = ElasticSearch.builder().index("chapters_index").type("chapters").sort("updateTime").order("asc").size(1).build();
        Map<String, Object> termParams = new HashMap<String, Object>(2) {{
            put("novelsId", novelsId);
        }};
        List<SearchResult.Hit<Object, Void>> src = elasticSearchDao.mustTermRangeQuery(chapterEsSearch, termParams, null);
        List<ChaptersDTO> target = EsConvertUtil.chaptersEntityConvert(src, true);
        ElasticSearch chapterEsSearch2 = ElasticSearch.builder().index("chapters_index").type("chapters").sort("updateTime").order("asc").size(10000).build();
        List<SearchResult.Hit<Object, Void>> ext = elasticSearchDao.mustTermRangeQuery(chapterEsSearch2, termParams, null);
        List<Map<String, Object>> listExt = EsConvertUtil.chaptersMapConvert(ext, false);
        commonDTO.setListExt(listExt);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<ChaptersDTO> readMore(String novelsId, String chaptersId) throws Exception {
        CommonDTO<ChaptersDTO> commonDTO = new CommonDTO<>();
        ElasticSearch chapterEsSearch = ElasticSearch.builder().index("chapters_index").type("chapters").build();
        Map chapters = (Map) elasticSearchDao.findById(chapterEsSearch, chaptersId);
        ChaptersDTO chaptersDTO = EsConvertUtil.chaptersMapConvert(chapters);
        if (!StringUtils.isEmpty(novelsId)) {
            ElasticSearch chapterEsSearch2 = ElasticSearch.builder().index("chapters_index").type("chapters").sort("updateTime").order("asc").size(10000).build();
            Map<String, Object> termParams = new HashMap<String, Object>(2) {{
                put("novelsId", novelsId);
            }};
            List<SearchResult.Hit<Object, Void>> ext = elasticSearchDao.mustTermRangeQuery(chapterEsSearch2, termParams, null);
            List<Map<String, Object>> listExt = EsConvertUtil.chaptersMapConvert(ext, false);
            commonDTO.setListExt(listExt);
        }
        commonDTO.setData(Collections.singletonList(chaptersDTO));
        return commonDTO;
    }

    @Override
    public CommonDTO<ChaptersDTO> someoneChapter(String chaptersId) {
        CommonDTO<ChaptersDTO> commonDTO = feignClientQuery.updateSomeoneChapters(chaptersId);
        return commonDTO;
    }

    @Override
    public CommonDTO<ChaptersDTO> allChapter(String novelsId) {
        CommonDTO<ChaptersDTO> commonDTO = feignClientQuery.updateAllChapters(novelsId);
        return commonDTO;
    }
}
