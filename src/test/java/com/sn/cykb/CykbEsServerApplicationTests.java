package com.sn.cykb;

import com.sn.cykb.elasticsearch.dao.ElasticSearchDao;
import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.entity.Users;
import com.sn.cykb.util.DateUtil;
import org.apache.catalina.User;
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
            ElasticSearch elasticSearch = ElasticSearch.builder().index("users_index").type("users").build();
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test7() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test8() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test9() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test10() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test28() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test11() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test12() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test13() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test14() {
        try {

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
