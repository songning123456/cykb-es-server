package com.sn.cykb.service.impl;

import com.alibaba.fastjson.JSON;
import com.sn.cykb.constant.HttpStatus;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersDTO;
import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.entity.Users;
import com.sn.cykb.service.UsersService;
import com.sn.cykb.util.DateUtil;
import com.sn.cykb.util.HttpUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersVO;
import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        Map<String, Object> termParams = new HashMap<String, Object>(2) {{
            put("uniqueId", code);
        }};
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

    @Override
    public CommonDTO<UsersDTO> getWxUsersInfo(CommonVO<UsersVO> commonVO) throws Exception {
        CommonDTO<UsersDTO> commonDTO = new CommonDTO<>();
        String code = commonVO.getCondition().getCode();
        String avatar = commonVO.getCondition().getAvatar();
        String nickName = commonVO.getCondition().getNickName();
        int gender = commonVO.getCondition().getGender();
        if (StringUtils.isEmpty(code)) {
            commonDTO.setStatus(HttpStatus.HTTP_ACCEPTED);
            commonDTO.setMessage("weixin code不能为空");
            return commonDTO;
        }
        String uniqueId = this.getWeixinUniqueId(code);
        if (StringUtils.isEmpty(uniqueId)) {
            commonDTO.setStatus(HttpStatus.HTTP_ACCEPTED);
            commonDTO.setMessage("未获取到微信openId");
            return commonDTO;
        }
        Map<String, Object> termParams = new HashMap<String, Object>(2) {{
            put("uniqueId", uniqueId);
        }};
        ElasticSearch usersEsSearch = ElasticSearch.builder().index("users_index").type("users").build();
        List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.mustTermRangeQuery(usersEsSearch, termParams, null);
        // 判断是否存在
        String updateTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        Users users;
        UsersDTO usersDTO;
        if (!result.isEmpty()) {
            SearchResult.Hit<Object, Void> item = result.get(0);
            users = Users.builder().avatar(((Map) item.source).get("avatar").toString()).gender(((Double)(((Map) item.source).get("gender"))).intValue()).updateTime(updateTime).uniqueId(uniqueId).nickName(((Map) item.source).get("nickName").toString()).build();
            // 判断是否修改过
            if (!avatar.equals(users.getAvatar()) || !nickName.equals(users.getNickName()) || gender != users.getGender()) {
                JestResult jestResult = elasticSearchDao.update(usersEsSearch, item.id, users);
                users = jestResult.getSourceAsObject(Users.class);
            }
        } else {
            users = Users.builder().uniqueId(uniqueId).avatar(avatar).nickName(nickName).gender(gender).updateTime(updateTime).build();
            JestResult jestResult = elasticSearchDao.save(usersEsSearch, users);
            users = jestResult.getSourceAsObject(Users.class);
        }
        usersDTO = UsersDTO.builder().avatar(users.getAvatar()).gender(users.getGender()).nickName(users.getNickName()).uniqueId(users.getUniqueId()).build();
        commonDTO.setData(Collections.singletonList(usersDTO));
        return commonDTO;
    }

    private String getWeixinUniqueId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + "wxa0c6ab139ce8d933" + "&secret=" + "8154f064fd75361e41ee71f1e1de6fd2" + "&js_code=" + code + "&grant_type=" + code;
        String respond = HttpUtil.doGet(url);
        String uniqueId = JSON.parseObject(respond).getString("openid");
        if (!StringUtils.isEmpty(uniqueId)) {
            return uniqueId;
        } else {
            return "";
        }
    }
}
