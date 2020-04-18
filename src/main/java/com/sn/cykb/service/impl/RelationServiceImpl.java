package com.sn.cykb.service.impl;

import com.sn.cykb.constant.HttpStatus;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.dto.RelationDTO;
import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.entity.Relation;
import com.sn.cykb.service.RelationService;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.RelationVO;
import io.searchbox.core.SearchResult;
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
    public CommonDTO<RelationDTO> bookcase(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        ElasticSearch relationEsSearch = ElasticSearch.builder().index("relation_index").type("relation").sort("updateTime").order("desc").build();
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
            List<RelationDTO> target = new ArrayList<>();
            if ("最近阅读".equals(sortType)) {
                // 根据 最近阅读 排序 users_novels_relation => update_time
                RelationDTO relationDTO;
                ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").build();
                for (SearchResult.Hit<Relation, Void> item : ((SearchResult) src).getHits(Relation.class)) {
                    String novelsId = item.source.getNovelsId();
                    relationDTO = new RelationDTO();
                    Novels novels = (Novels) elasticSearchDao.findById(novelsEsSearch, novelsId);
                    relationDTO.setAuthor(novels.getAuthor());
                    target.add(relationDTO);
                }
            } else {
                // 根据最近更新排序 novels => update_time
                ElasticSearch novelsEsSearch = ElasticSearch.builder().index("novels_index").type("novels").sort("updateTime").order("desc").size(10000).build();
                List<String> novelsIds = new ArrayList<>();
                for (SearchResult.Hit<Relation, Void> item : ((SearchResult) src).getHits(Relation.class)) {
                    novelsIds.add(item.source.getNovelsId());
                }
                Map<String, Object> termsParams = new HashMap<String, Object>(2){{
                    put("_id", novelsIds);
                }};
                List<SearchResult.Hit<Object, Void>> res = elasticSearchDao.mustTermsRangeQuery(novelsEsSearch, termsParams, null);
                for (SearchResult.Hit<Novels, Void> item: ((SearchResult)res).getHits(Novels.class)) {
                    RelationDTO dto = new RelationDTO();
                    dto.setAuthor(item.source.getAuthor());
                }
            }
            commonDTO.setData(target);
            commonDTO.setTotal((long) target.size());
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<RelationDTO> insertBookcase(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = new CommonDTO<>();
//        String novelsId = commonVO.getCondition().getNovelsId();
//        String uniqueId = commonVO.getCondition().getUniqueId();
//        Relation relation = usersNovelsRelationRepository.findByUniqueIdAndAndNovelsId(uniqueId, novelsId);
//        if (relation != null) {
//            commonDTO.setStatus(201);
//            commonDTO.setMessage("书架已存在此书");
//            return commonDTO;
//        }
//        relation = Relation.builder().novelsId(novelsId).uniqueId(uniqueId).updateTime(new Date()).build();
//        usersNovelsRelationRepository.save(relation);
        return commonDTO;
    }

    @Override
    public CommonDTO<RelationDTO> topBookcase(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = new CommonDTO<>();
//        String uniqueId = commonVO.getCondition().getUniqueId();
//        String novelsId = commonVO.getCondition().getNovelsId();
//        Relation relation = usersNovelsRelationRepository.findByUniqueIdAndAndNovelsId(uniqueId, novelsId);
//        if (relation != null) {
//            usersNovelsRelationRepository.updateByRecentReadNative(uniqueId, novelsId, new Date());
//        }
        return commonDTO;
    }

    @Override
    public CommonDTO<RelationDTO> deleteBookcase(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = new CommonDTO<>();
//        String uniqueId = commonVO.getCondition().getUniqueId();
//        List<String> novelsIdList = commonVO.getCondition().getNovelsIdList();
//        usersNovelsRelationRepository.deleteInNative(uniqueId, novelsIdList);
        return commonDTO;
    }

    @Override
    public CommonDTO<RelationDTO> isExist(CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = new CommonDTO<>();
//        String uniqueId = commonVO.getCondition().getUniqueId();
//        String novelsId = commonVO.getCondition().getNovelsId();
//        Relation relation = usersNovelsRelationRepository.findByUniqueIdAndAndNovelsId(uniqueId, novelsId);
//        if (relation == null) {
//            commonDTO.setStatus(202);
//        }
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> ourSearch() throws Exception {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
//        List<Map<String, Object>> novelsList = usersNovelsRelationRepository.countByNovelsIdNative();
//        List<String> novelsIdList = new ArrayList<>();
//        for (Map<String, Object> item : novelsList) {
//            String novelsId = String.valueOf(item.get("novelsId"));
//            novelsIdList.add(novelsId);
//        }
//        List<Novels> src = novelsRepository.findAllByIdInOrderByUpdateTimeDesc(novelsIdList);
//        List<NovelsDTO> target = new ArrayList<>();
//        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
//        commonDTO.setData(target);
//        commonDTO.setTotal((long) target.size());
        return commonDTO;
    }
}
