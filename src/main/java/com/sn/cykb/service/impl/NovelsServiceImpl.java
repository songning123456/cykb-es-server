package com.sn.cykb.service.impl;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.elasticsearch.entity.Range;
import com.sn.cykb.service.NovelsService;
import com.sn.cykb.util.EsConvertUtil;
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
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").sort("createTime").order("desc").size(pageRecordNum).build();
        if (null != recordStartNo) {
            // 第一次查询
            src = elasticSearchDao.mustTermRangeQuery(novelsEsSearch, null, null);
        } else {
            // 第二次开始查询
            Long createTime = commonVO.getCondition().getCreateTime();
            Range range = Range.builder().rangeName("createTime").ltOrLte("lt").max(createTime).build();
            src = elasticSearchDao.mustTermRangeQuery(novelsEsSearch, null, Collections.singletonList(range));
        }
        List<NovelsDTO> target = EsConvertUtil.novelsAllFieldConvert(src);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> classifyCount() throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").build();
        List<TermsAggregation.Entry> src = elasticSearchDao.aggregationTermQuery(novelsEsSearch, null, "sourceName");
        List<NovelsDTO> target = new ArrayList<>();
        Map<String, Object> dataExt = new HashMap<>(2);
        for (TermsAggregation.Entry item : src) {
            NovelsDTO dto = new NovelsDTO();
            String sourceName = item.getKey();
            dto.setSourceName(sourceName);
            dto.setTotal(item.getCount());
            Map<String, Object> termParams = new HashMap<String, Object>(2) {{
                put("sourceName", sourceName);
            }};
            List<TermsAggregation.Entry> list = elasticSearchDao.aggregationTermQuery(novelsEsSearch, termParams, "category");
            List<Map<String, Object>> tempList = new ArrayList<>();
            list.forEach(val -> {
                Map<String, Object> ext = new HashMap<>(2);
                ext.put("category", val.getKey());
                ext.put("categoryTotal", val.getCount());
                tempList.add(ext);
            });
            dataExt.put(sourceName, tempList);
            target.add(dto);
        }
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
        List<SearchResult.Hit<Object, Void>> src;
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").sort("createTime").order("desc").size(pageRecordNum).build();
        Map<String, Object> termParams = new HashMap<String, Object>(2) {
            {
                put("sourceName", sourceName);
                put("category", category);
            }
        };
        if (null != recordStartNo) {
            src = elasticSearchDao.mustTermRangeQuery(novelsEsSearch, termParams, null);
        } else {
            Long createTime = commonVO.getCondition().getCreateTime();
            Range range = Range.builder().rangeName("createTime").ltOrLte("lt").max(createTime).build();
            src = elasticSearchDao.mustTermRangeQuery(novelsEsSearch, termParams, Collections.singletonList(range));
        }
        List<NovelsDTO> target = EsConvertUtil.novelsAllFieldConvert(src);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> sameAuthor(CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        String author = commonVO.getCondition().getAuthor();
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").sort("createTime").order("desc").build();
        Map<String, Object> termParams = new HashMap<String, Object>(2) {
            {
                put("author", author);
            }
        };
        List<SearchResult.Hit<Object, Void>> src = elasticSearchDao.mustTermRangeQuery(novelsEsSearch, termParams, null);
        List<NovelsDTO> target = EsConvertUtil.novelsAllFieldConvert(src);
        commonDTO.setData(target);
        commonDTO.setTotal((long) target.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> fastSearch(String authorOrTitle) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").build();
        Map<String, Object> wildCardParams = new HashMap<String, Object>() {
            {
                put("author", authorOrTitle);
                put("title", authorOrTitle);
            }
        };
        List<SearchResult.Hit<Object, Void>> src = elasticSearchDao.mustTermShouldWildCardQuery(novelsEsSearch, null, wildCardParams, null);
        List<NovelsDTO> target = EsConvertUtil.novelsAllFieldConvert(src);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> searchResult(CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        String authorOrTitle = commonVO.getCondition().getAuthorOrTitle();
        List<SearchResult.Hit<Object, Void>> src;
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").sort("createTime").order("desc").size(pageRecordNum).build();
        Map<String, Object> wildCardParams = new HashMap<String, Object>() {
            {
                put("author", authorOrTitle);
                put("title", authorOrTitle);
            }
        };
        if (recordStartNo != null) {
            src = elasticSearchDao.mustTermShouldWildCardQuery(novelsEsSearch, null, wildCardParams, null);
        } else {
            Long createTime = commonVO.getCondition().getCreateTime();
            Range range = Range.builder().rangeName("createTime").ltOrLte("lt").max(createTime).build();
            src = elasticSearchDao.mustTermShouldWildCardQuery(novelsEsSearch, null, wildCardParams, Collections.singletonList(range));
        }
        List<NovelsDTO> target = EsConvertUtil.novelsAllFieldConvert(src);
        commonDTO.setData(target);
        return commonDTO;
    }
}
