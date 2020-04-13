package com.sn.cykb.dao;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songning
 * @date 2020/4/13
 * description
 */
@Component
@Slf4j
public class ElasticSearchDao {

    @Autowired
    private JestClient jestClient;

    public <T> void save(String index, String type, T entity) throws Exception {
        Index action = new Index.Builder(entity).index(index).type(type).build();
        JestResult jestResult = jestClient.execute(action);
    }

    public <T> void saveAll(String index, String type, List<T> list) throws Exception {
        List<Index> actions = new ArrayList<>();
        assert list != null;
        list.forEach(item -> actions.add(new Index.Builder(item).build()));
        Bulk bulk = new Bulk.Builder().defaultIndex(index).defaultType(type).addAction(actions).build();
        JestResult jestResult = jestClient.execute(bulk);
    }
}
