package com.sn.cykb.service.impl;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.elasticsearch.entity.Range;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.service.NovelsService;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.NovelsVO;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:59
 */
@Service
public class NovelsServiceImpl implements NovelsService {

    @Autowired
    private ElasticSearchDao elasticSearchDao;

    @Override
    public CommonDTO<NovelsDTO> homePage(CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        List<SearchResult.Hit<Object, Void>> src;
        List<NovelsDTO> target = new ArrayList<>();
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").order("createTime").sort("desc").size(pageRecordNum).build();
        if (null != recordStartNo) {
            // 第一次查询
            src = elasticSearchDao.mustTermRangeQuery(novelsEsSearch, null, null);
        } else {
            // 第二次开始查询
            Long createTime = commonVO.getCondition().getCreateTime();
            Range range = Range.builder().rangeName("createTime").ltOrLte("lt").max(createTime).build();
            src = elasticSearchDao.mustTermRangeQuery(novelsEsSearch, null, Collections.singletonList(range));
        }
        for (SearchResult.Hit<Novels, Void> item : ((SearchResult) src).getHits(Novels.class)) {
            NovelsDTO dto = new NovelsDTO();
            dto.setNovelsId(item.id);
            dto.setTitle(item.source.getTitle());
            dto.setAuthor(item.source.getAuthor());
            dto.setCategory(item.source.getCategory());
            dto.setIntroduction(item.source.getIntroduction());
            dto.setLatestChapter(item.source.getLatestChapter());
            dto.setCoverUrl(item.source.getCoverUrl());
            dto.setSourceUrl(item.source.getSourceUrl());
            dto.setSourceName(item.source.getSourceName());
        }
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> classifyCount() throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").build();
        List<TermsAggregation.Entry> src = elasticSearchDao.aggregationTermSubQuery(novelsEsSearch, null, "sourceName", "category");
        List<NovelsDTO> target = new ArrayList<>();
        Map<String, Object> dataExt = new HashMap<>(2);
        for (TermsAggregation.Entry item : src) {
            NovelsDTO dto = new NovelsDTO();
            dto.setSourceName(item.getKey());
            dataExt.put(item.getKey(), )
        }
        src.forEach(item -> {
            NovelsDTO dto = new NovelsDTO();
            String sourceName = String.valueOf(item.get("sourceName"));
            dto.setSourceName(sourceName);
            List<Map<String, Object>> mapList = novelsRepository.countCategoryBySourceNative(sourceName);
            dataExt.put(sourceName, mapList);
            dto.setTotal(Integer.parseInt(item.get("total").toString()));
            target.add(dto);
        });
        commonDTO.setTotal((long) src.size());
        commonDTO.setData(target);
        commonDTO.setDataExt(dataExt);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> classifyResult(CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        String sourceName = commonVO.getCondition().getSourceName();
        String category = commonVO.getCondition().getCategory();
        List<Novels> src;
        List<NovelsDTO> target = new ArrayList<>();
        if (null != recordStartNo) {
            src = novelsRepository.findFirstClassifyNative(sourceName, category, pageRecordNum);
        } else {
            Long createTime = commonVO.getCondition().getCreateTime();
            src = novelsRepository.findMoreClassifyNative(sourceName, category, createTime, pageRecordNum);
        }
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> sameAuthor(CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        String author = commonVO.getCondition().getAuthor();
        List<Novels> src = novelsRepository.findByAuthorOrderByCreateTimeDesc(author);
        List<NovelsDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        commonDTO.setTotal((long) target.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> fastSearch(String authorOrTitle) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        List<Novels> src = novelsRepository.findByAuthorOrTitleNative(authorOrTitle);
        List<NovelsDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> searchResult(CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        String authorOrTitle = commonVO.getCondition().getAuthorOrTitle();
        List<Novels> src;
        List<NovelsDTO> target = new ArrayList<>();
        if (recordStartNo != null) {
            src = novelsRepository.findFirstByAuthorOrTitleNative(authorOrTitle, pageRecordNum);
        } else {
            Long createTime = commonVO.getCondition().getCreateTime();
            src = novelsRepository.findMoreByAuthorOrTitleNative(authorOrTitle, createTime, pageRecordNum);
        }
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        return commonDTO;
    }
}
