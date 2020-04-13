package com.sn.cykb;

import com.sn.cykb.dao.ElasticSearchDao;
import com.sn.cykb.entity.Users;
import com.sn.cykb.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CykbEsServerApplicationTests {

    @Autowired
    private ElasticSearchDao elasticSearchDao;

    @Test
    public void testSave() {
        try {
            List<Users> usersList = this.generateUsersList();
            elasticSearchDao.saveAll("users_index", "users", usersList);
            Users users = this.generateUsers();
            elasticSearchDao.save("users_index", "users", users);
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Users> generateUsersList() {
        List<Users> usersList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Users users = Users.builder().id(String.valueOf(Math.random())).uniqueId("1585068219" + i).avatar("http://").gender(1).nickName("测试人员" + i).updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
            usersList.add(users);
        }
        return usersList;
    }

    private Users generateUsers() {
        Users users = Users.builder().uniqueId("15850682195").avatar("http://").gender(1).nickName("测试人员5").updateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")).build();
        return users;
    }
}
