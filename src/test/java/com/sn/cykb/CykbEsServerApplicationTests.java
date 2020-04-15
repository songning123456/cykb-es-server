package com.sn.cykb;

import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.elasticsearch.entity.Range;
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
    public void test1() {
        try {
            List<Users> usersList = this.generateUsersList();
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").build();
            elasticSearchDao.bulk(elasticSearch, usersList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            Users users = this.generateUsers();
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").build();
            elasticSearchDao.save(elasticSearch, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").from(0).size(20).sort("updateTime").order("desc").build();
            Map<String, Object> termParams = new HashMap<>();
            termParams.put("nickName", "测试人员5");
            elasticSearchDao.mustTermRangeQuery(elasticSearch, termParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").from(0).size(20).build();
            Map<String, Object> termParams = new HashMap<>();
            termParams.put("nickName", "测试人员5");
            elasticSearchDao.mustTermRangeQuery(elasticSearch, termParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").build();
            elasticSearchDao.findById(elasticSearch, "bRnSfXEBj8NokppAGbzw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test6() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").build();
            Map<String, Object> termParams = new HashMap<>();
            termParams.put("nickName", "测试人员5");
            termParams.put("updateTime", "2020-04-15 20:30:41");
            elasticSearchDao.mustTermRangeQuery(elasticSearch, termParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test29() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").sort("updateTime").order("desc").build();
            Map<String, Object> termParams = new HashMap<>();
            termParams.put("nickName", "测试人员5");
            elasticSearchDao.mustTermRangeQuery(elasticSearch, termParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test7() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").sort("updateTime").order("desc").build();
            Map<String, Object> termParams = new HashMap<>();
            termParams.put("nickName", "测试人员5");
            termParams.put("avatarUrl", "http://5");
            Range range = Range.builder().rangeName("updateTime").ltOrLte("lt").max("2020-04-15 20:30:42").build();
            elasticSearchDao.mustTermRangeQuery(elasticSearch, termParams, Collections.singletonList(range));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test8() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").sort("updateTime").order("desc").build();
            Range range = Range.builder().rangeName("updateTime").ltOrLte("lt").max("2020-04-15 20:30:42").build();
            elasticSearchDao.mustTermRangeQuery(elasticSearch, null, Collections.singletonList(range));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test9() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").from(0).size(20).sort("updateTime").order("desc").build();
            Map<String, String[]> termsParams = new HashMap<>();
            termsParams.put("nickName", new String[]{"测试人员5", "测试人员6"});
            elasticSearchDao.mustTermsRangeQuery(elasticSearch, termsParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test10() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").sort("updateTime").order("desc").build();
            Map<String, Object> wildCardParams = new HashMap<>();
            wildCardParams.put("nickName", "员5");
            wildCardParams.put("avatarUrl", "http:/");
            elasticSearchDao.mustTermShouldWildCardQuery(elasticSearch, null, wildCardParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test28() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").sort("updateTime").order("desc").build();
            Map<String, Object> wildCardParams = new HashMap<>();
            wildCardParams.put("nickName", "员5");
            wildCardParams.put("avatarUrl", "http:/");
            Range range = Range.builder().rangeName("updateTime").ltOrLte("lt").max("2020-04-15 20:36:45").build();
            elasticSearchDao.mustTermShouldWildCardQuery(elasticSearch, null, wildCardParams, Collections.singletonList(range));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test11() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").sort("updateTime").order("desc").build();
            elasticSearchDao.aggregationTermQuery(elasticSearch, null, "nickName");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test12() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").sort("updateTime").order("desc").build();
            Map<String, Object> termParams = new HashMap<>();
            termParams.put("nickName", "测试人员5");
            elasticSearchDao.aggregationTermCountOrderQuery(elasticSearch, termParams, "nickName");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test13() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").build();
            Map<String, Object> termParams = new HashMap<>();
            termParams.put("nickName", "测试人员5");
            termParams.put("updateTime", "2020-04-15 20:30:41");
            List<SearchResult.Hit<Object, Void>> list = elasticSearchDao.mustTermRangeQuery(elasticSearch, termParams, null);
            String id = list.get(0).id;
            Users users = Users.builder().avatar("http://12").gender(1).nickName("测试人员12").uniqueId("15850682195").updateTime("2020-04-15 20:30:41").build();
            elasticSearchDao.update(elasticSearch, id, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test14() {
        try {
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").build();
            Map<String, String[]> termsParams = new HashMap<>();
            termsParams.put("uniqueId", new String[]{"15850682195"});
            termsParams.put("nickName", new String[]{"测试人员12"});
            elasticSearchDao.mustTermsDelete(elasticSearch, termsParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test15() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test16() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test17() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test18() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test19() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test20() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test21() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test22() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test23() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test24() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test25() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test26() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test27() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Users> generateUsersList() {
        List<Users> usersList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int gender = 0;
            if (i % 2 == 0) {
                gender = 1;
            }
            Users users = Users.builder().uniqueId("1585068219" + i).avatar("http://" + i).gender(gender).nickName("测试人员" + i).updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
            usersList.add(users);
        }
        return usersList;
    }

    private Users generateUsers() {
        Users users = Users.builder().uniqueId("15850682199").avatar("http://9").gender(0).nickName("测试人员5").updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
        return users;
    }
}
