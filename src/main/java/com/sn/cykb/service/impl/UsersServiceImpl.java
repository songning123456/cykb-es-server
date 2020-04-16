package com.sn.cykb.service.impl;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersDTO;
import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.entity.Users;
import com.sn.cykb.service.UsersService;
import com.sn.cykb.util.DateUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersVO;
import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private ElasticSearchDao elasticSearchDao;


    @Override
    public CommonDTO<UsersDTO> getUniUsersInfo(CommonVO<UsersVO> commonVO) throws Exception {
        CommonDTO<UsersDTO> commonDTO = new CommonDTO<>();
        String code = commonVO.getCondition().getCode();
        ElasticSearch usersEsSearch = ElasticSearch.builder().index("users_index").type("users").build();
        Map<String, Object> termParams = new HashMap<String, Object>(2) {
            {
                put("uniqueId", code);
            }
        };
        UsersDTO usersDTO = new UsersDTO();
        List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.mustTermRangeQuery(usersEsSearch, termParams, null);
        if (result.isEmpty()) {
            Users users = Users.builder().uniqueId(code).updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
            JestResult jestResult = elasticSearchDao.save(usersEsSearch, users);
            usersDTO.setUniqueId(((DocumentResult) jestResult).getId());
        } else {
            usersDTO.setUniqueId(result.get(0).id);
        }
        commonDTO.setData(Collections.singletonList(usersDTO));
        return commonDTO;
    }
}
