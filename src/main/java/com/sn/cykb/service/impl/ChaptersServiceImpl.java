package com.sn.cykb.service.impl;

import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.entity.Chapters;
import com.sn.cykb.service.ChaptersService;
import com.sn.cykb.vo.ChaptersVO;
import com.sn.cykb.vo.CommonVO;
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

    @Override
    public CommonDTO<ChaptersDTO> directory(CommonVO<ChaptersVO> commonVO) throws Exception {
        CommonDTO<ChaptersDTO> commonDTO = new CommonDTO<>();
        String novelsId = commonVO.getCondition().getNovelsId();
        ElasticSearch chapterEsSearch = ElasticSearch.builder().index("chapters_index").type("chapters").sort("updateTime").order("desc").size(10000).build();
        Map<String, Object> termParams = new HashMap<String, Object>() {
            {
                put("novelsId", novelsId);
            }
        };
        List<SearchResult.Hit<Object, Void>> src = elasticSearchDao.mustTermRangeQuery(chapterEsSearch, termParams, null);
        List<ChaptersDTO> target = new ArrayList<>();
        ChaptersDTO dto;
        for (SearchResult.Hit<Chapters, Void> item : ((SearchResult) src).getHits(Chapters.class)) {
            dto = new ChaptersDTO();
            dto.setNovelsId(item.id);
            dto.setChapter(item.source.getChapter());
            target.add(dto);
        }
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<ChaptersDTO> firstChapter(String novelsId) throws Exception {
        CommonDTO<ChaptersDTO> commonDTO = new CommonDTO<>();
        ElasticSearch chapterEsSearch = ElasticSearch.builder().index("chapters_index").type("chapters").sort("updateTime").order("desc").size(1).build();
        Map<String, Object> termParams = new HashMap<String, Object>(2) {{
            put("novelsId", novelsId);
        }};
        List<SearchResult.Hit<Object, Void>> src = elasticSearchDao.mustTermRangeQuery(chapterEsSearch, termParams, null);
        List<ChaptersDTO> target = new ArrayList<>();
        for (SearchResult.Hit<Chapters, Void> item : ((SearchResult) src).getHits(Chapters.class)) {
            ChaptersDTO dto = new ChaptersDTO();
            dto.setChaptersId(item.id);
            dto.setChapter(item.source.getChapter());
            dto.setContent(item.source.getContent());
            dto.setNovelsId(item.source.getNovelsId());
            target.add(dto);
        }
        ElasticSearch chapterEsSearch2 = ElasticSearch.builder().index("chapters_index").type("chapters").sort("updateTime").order("desc").size(10000).build();
        List<SearchResult.Hit<Object, Void>> ext = elasticSearchDao.mustTermRangeQuery(chapterEsSearch2, termParams, null);
        List<Map<String, Object>> listExt = new ArrayList<>();
        for (SearchResult.Hit<Chapters, Void> item : ((SearchResult) ext).getHits(Chapters.class)) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("chapterId", item.id);
            map.put("chapter", item.source.getChapter());
            map.put("content", item.source.getContent());
            map.put("novelsId", item.source.getNovelsId());
            listExt.add(map);
        }
        commonDTO.setListExt(listExt);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<ChaptersDTO> readMore(String novelsId, String chaptersId) throws Exception {
        CommonDTO<ChaptersDTO> commonDTO = new CommonDTO<>();
        ElasticSearch chapterEsSearch = ElasticSearch.builder().index("chapters_index").type("chapters").build();
        Chapters chapters = (Chapters) elasticSearchDao.findById(chapterEsSearch, chaptersId);
        ChaptersDTO chaptersDTO = new ChaptersDTO();
        chaptersDTO.setNovelsId(chapters.getNovelsId());
        chaptersDTO.setContent(chapters.getContent());
        chaptersDTO.setChaptersId(chaptersId);
        if (!StringUtils.isEmpty(novelsId)) {
            ElasticSearch chapterEsSearch2 = ElasticSearch.builder().index("chapters_index").type("chapters").sort("updateTime").order("desc").size(10000).build();
            Map<String, Object> termParams = new HashMap<String, Object>(2) {{
                put("novelsId", novelsId);
            }};
            List<SearchResult.Hit<Object, Void>> ext = elasticSearchDao.mustTermRangeQuery(chapterEsSearch2, termParams, null);
            List<Map<String, Object>> listExt = new ArrayList<>();
            for (SearchResult.Hit<Chapters, Void> item : ((SearchResult) ext).getHits(Chapters.class)) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("chapterId", item.id);
                map.put("chapter", item.source.getChapter());
                map.put("content", item.source.getContent());
                map.put("novelsId", item.source.getNovelsId());
                listExt.add(map);
            }
            commonDTO.setListExt(listExt);
        }
        commonDTO.setData(Collections.singletonList(chaptersDTO));
        return commonDTO;
    }
}
