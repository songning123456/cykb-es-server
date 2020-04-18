package com.sn.cykb.service.impl;

import com.sn.cykb.constant.HttpStatus;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.dto.RelationDTO;
import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.entity.Relation;
import com.sn.cykb.service.RelationService;
import com.sn.cykb.util.DateUtil;
import com.sn.cykb.util.EsConvertUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.RelationVO;
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
public class RelationServiceImpl implements RelationService {

    @Autowired
    private ElasticSearchDao elasticSearchDao;

    @Override
    public CommonDTO<NovelsDTO> bookcase(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        ElasticSearch relationEsSearch = ElasticSearch.builder().index("relation_index").type("relation").sort("updateTime").order("desc").size(10000).build();
        Map<String, Object> termParams = new HashMap<String, Object>(2) {{
            put("uniqueId", uniqueId);
        }};
        List<SearchResult.Hit<Object, Void>> src = elasticSearchDao.mustTermRangeQuery(relationEsSearch, termParams, null);
        if (src.isEmpty()) {
            commonDTO.setTotal(0L);
            commonDTO.setStatus(HttpStatus.HTTP_ACCEPTED);
            commonDTO.setMessage("书架暂无您的书籍");
        } else {
            String sortType = commonVO.getCondition().getSortType();
            List<NovelsDTO> target = new ArrayList<>();
            List<RelationDTO> relationList = EsConvertUtil.relationEntityConvert(src);
            if ("最近阅读".equals(sortType)) {
                ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").build();
                // 根据 最近阅读 排序 users_novels_relation => update_time
                for (RelationDTO item : relationList) {
                    String novelsId = item.getNovelsId().toString();
                    Map novelsMap = (Map) elasticSearchDao.findById(novelsEsSearch, novelsId);
                    target.add(EsConvertUtil.novelsMapConvert(novelsMap));
                }
            } else {
                // 根据最近更新排序 novels => update_time
                ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").sort("updateTime").order("desc").size(10000).build();
                List<String> novelsIds = new ArrayList<>();
                for (RelationDTO item : relationList) {
                    novelsIds.add(item.getNovelsId().toString());
                }
                Map<String, String[]> termsParams = new HashMap<String, String[]>(2) {{
                    put("_id", novelsIds.toArray(new String[novelsIds.size()]));
                }};
                List<SearchResult.Hit<Object, Void>> res = elasticSearchDao.mustTermsRangeQuery(novelsEsSearch, termsParams, null);
                target = EsConvertUtil.novelsEntityConvert(res);
            }
            commonDTO.setData(target);
            commonDTO.setTotal((long) target.size());
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<RelationDTO> insertBookcase(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = new CommonDTO<>();
        String novelsId = commonVO.getCondition().getNovelsId();
        String uniqueId = commonVO.getCondition().getUniqueId();
        ElasticSearch elasticSearch = ElasticSearch.builder().index("relation_index").type("relation").build();
        Map<String, Object> termParams = new HashMap<String, Object>() {
            {
                put("uniqueId", uniqueId);
                put("novelsId", novelsId);
            }
        };
        List<SearchResult.Hit<Object, Void>> src = elasticSearchDao.mustTermRangeQuery(elasticSearch, termParams, null);
        if (!src.isEmpty()) {
            commonDTO.setStatus(201);
            commonDTO.setMessage("书架已存在此书");
            return commonDTO;
        }
        Relation relation = Relation.builder().novelsId(novelsId).uniqueId(uniqueId).updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
        elasticSearchDao.save(elasticSearch, relation);
        return commonDTO;
    }

    @Override
    public CommonDTO<RelationDTO> topBookcase(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        String novelsId = commonVO.getCondition().getNovelsId();
        ElasticSearch elasticSearch = ElasticSearch.builder().index("relation_index").type("relation").build();
        Map<String, Object> termParams = new HashMap<String, Object>() {
            {
                put("uniqueId", uniqueId);
                put("novelsId", novelsId);
            }
        };
        List<SearchResult.Hit<Object, Void>> src = elasticSearchDao.mustTermRangeQuery(elasticSearch, termParams, null);
        if (!src.isEmpty()) {
            List<RelationDTO> target = EsConvertUtil.relationEntityConvert(src);
            for (RelationDTO item : target) {
                Relation relation = Relation.builder().novelsId(item.getNovelsId().toString()).uniqueId(item.getUniqueId().toString()).updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
                elasticSearchDao.update(elasticSearch, item.getRelationId().toString(), relation);
            }
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<RelationDTO> deleteBookcase(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        List<String> novelsIdList = commonVO.getCondition().getNovelsIdList();
        ElasticSearch elasticSearch = ElasticSearch.builder().index("relation_index").type("relation").build();
        Map<String, String[]> termsParams = new HashMap<String, String[]>() {{
            put("uniqueId", new String[]{uniqueId});
            put("novelsId", novelsIdList.toArray(new String[novelsIdList.size()]));
        }};
        elasticSearchDao.mustTermsDelete(elasticSearch, termsParams);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> ourSearch() throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        ElasticSearch elasticSearch = ElasticSearch.builder().index("relation_index").type("relation").order("desc").build();
        List<TermsAggregation.Entry> src = elasticSearchDao.aggregationTermQuery(elasticSearch, null, "novelsId");
        List<String> novelsIdList = new ArrayList<>();
        for (TermsAggregation.Entry item : src) {
            String novelsId = item.getKey();
            novelsIdList.add(novelsId);
        }
        ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").sort("updateTime").order("desc").size(10000).build();
        Map<String, String[]> termsParams = new HashMap<String, String[]>() {{
            put("_id", novelsIdList.toArray(new String[novelsIdList.size()]));
        }};
        List<SearchResult.Hit<Object, Void>> novelsList = elasticSearchDao.mustTermsRangeQuery(novelsEsSearch, termsParams, null);
        List<NovelsDTO> target = EsConvertUtil.novelsEntityConvert(novelsList);
        commonDTO.setData(target);
        commonDTO.setTotal((long) target.size());
        return commonDTO;
    }
}
