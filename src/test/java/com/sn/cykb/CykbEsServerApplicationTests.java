package com.sn.cykb;

import com.sn.cykb.dao.ElasticSearchDao;
import com.sn.cykb.entity.Users;
import com.sn.cykb.util.DateUtil;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CykbEsServerApplicationTests {

    @Autowired
    private ElasticSearchDao elasticSearchDao;

    @Test
    public void testSave() {
        try {
            List<Users> usersList = this.generateUsersList();
            elasticSearchDao.bulk("users_index", "users", usersList);
            Users users = this.generateUsers();
            elasticSearchDao.save("users_index", "users", users);
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQuery() {
        try {
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.termQuery("users_index", "users", 0, 30, "updateTime", false, "avatar", "http://");
            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.termsQuery("users_index", "users", 0, 30, "_id", new String[]{"QRlBcnEBj8NokppASryb", "RBlBcnEBj8NokppASryb"}, "updateTime", false);
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.termQuery("users_index", "users", 0, 30, "updateTime", false);
            Map<String, Object> params = new HashMap<>();
            params.put("avatar", "http://");
            params.put("nickName", "测试人员0");
//            params.put("updateTime", "2020-04-13 15:52:24");
            Map<String, Object> range = new HashMap<>();
            range.put("gt", "2020-04-13 14:31:57");
            range.put("lt", "2020-04-13 14:55:29");
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.boolTermsQuery("users_index", "users", 0, 30, "update_time", false,params);
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.boolTermsRangeQuery("users_index", "users", 0, 30, "updateTime", false, params, "updateTime", range);
//            Object result = elasticSearchDao.findById("users_index", "users", "ZRmGcnEBj8NokppA4bwD");
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.rangeQuery("users_index", "users", 0, 30, "updateTime", fromTo, "updateTime", false);
//            List<TermsAggregation.Entry> result = elasticSearchDao.aggregationQuery("users_index", "users", 0, 30, "nickName", "updateTime", "2020-04-13 15:52:24");
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Users> generateUsersList() {
        List<Users> usersList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Users users = Users.builder().uniqueId("1585068219" + i).avatar("http://").gender(1).nickName("测试人员" + i).updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
            usersList.add(users);
        }
        return usersList;
    }

    private Users generateUsers() {
        Users users = Users.builder().uniqueId("15850682195").avatar("http://").gender(1).nickName("测试人员5").updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
        return users;
    }
}
