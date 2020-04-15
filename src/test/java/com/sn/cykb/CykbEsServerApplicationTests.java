package com.sn.cykb;

import com.sn.cykb.entity.Users;
import com.sn.cykb.util.DateUtil;
import io.searchbox.core.SearchResult;
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
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.termsQuery("users_index", "users", 0, 30, "_id", new String[]{"QRlBcnEBj8NokppASryb", "RBlBcnEBj8NokppASryb"}, "updateTime", false);
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.termQuery("users_index", "users", 0, 30, "updateTime", false);
            Map<String, Object> params = new HashMap<>();
            params.put("avatar", "http://");
            params.put("nickName", "测试人员0");
//            params.put("updateTime", "2020-04-13 15:52:24");
            Map<String, Object> range = new HashMap<>();
            // range.put("gt", "2020-04-13 14:31:57");
            range.put("lt", "2020-04-13 14:55:29");
            Map<String, Object> wildParams = new HashMap<>();
            wildParams.put("nickName", "0");
            wildParams.put("uniqueId", "2190");
            Map<String, Object> delParams = new HashMap<>();
            Map<String, String[]> delsParams = new HashMap<>();
            delParams.put("nickName", "测试人员1");
            delsParams.put("updateTime", new String[]{"2020-04-13 15:03:58"});
//            List<TermsAggregation.Entry> result = elasticSearchDao.aggregationSubCountQuery("users_index", "users", 0, 30, "nickName");
//            elasticSearchDao.deleteByFieldNames("users_index", "users", delParams, delsParams);
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.boolMustShouldWildCardRangeQuery("users_index", "users", 0, 30, "updateTime", range, wildParams, "updateTime", false);
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.boolShouldWildCardQuery("users_index", "users", 0, 30, wildParams, "updateTime", false);
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.boolTermsQuery("users_index", "users", 0, 30, "update_time", false,params);
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.boolTermsRangeQuery("users_index", "users", 0, 30, "updateTime", false, params, "updateTime", range);
//            Object result = elasticSearchDao.findById("users_index", "users", "ZRmGcnEBj8NokppA4bwD");
//            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.rangeQuery("users_index", "users", 0, 30, "updateTime", fromTo, "updateTime", false);
//            List<TermsAggregation.Entry> result = elasticSearchDao.aggregationQuery("users_index", "users", 0, 30, "nickName", "updateTime", "2020-04-13 15:52:24");
            Map<String, Object> termParams = new HashMap<>();
            termParams.put("nickName", "测试人员2");
            termParams.put("updateTime", "2020-04-13 15:03:58");
            List<SearchResult.Hit<Object, Void>> result = elasticSearchDao.termQuery("users_index", "users", 0, 30, termParams, "updateTime", false);
            String id = result.get(0).id;
            Users users = Users.builder().nickName("测试人员2").gender(0).avatar("https://").uniqueId("xxxxxxxx").updateTime("2020-04-13 15:03:59").build();
            elasticSearchDao.updateIndexDoc("users_index", "users", id, users);
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEs() {

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
