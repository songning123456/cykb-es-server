package com.sn.cykb;

import io.searchbox.client.JestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CykbEsServerApplicationTests {

    @Autowired
    private JestClient jestClient;

    @Test
    public void contextLoads() {
    }

}
