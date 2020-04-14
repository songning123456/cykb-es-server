package com.sn.cykb.dao;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 插入单条数据
     *
     * @param index
     * @param type
     * @param entity
     * @param <T>
     * @throws Exception
     */
    public <T> void save(String index, String type, T entity) throws Exception {
        Index action = new Index.Builder(entity).index(index).type(type).build();
        JestResult jestResult = jestClient.execute(action);
        if (!jestResult.isSucceeded()) {
            throw new Exception(jestResult.getErrorMessage());
        }
    }

    /**
     * bulk批量插入
     *
     * @param index
     * @param type
     * @param list
     * @param <T>
     * @throws Exception
     */
    public <T> void bulk(String index, String type, List<T> list) throws Exception {
        List<Index> actions = new ArrayList<>();
        assert list != null;
        list.forEach(item -> actions.add(new Index.Builder(item).build()));
        Bulk bulk = new Bulk.Builder().defaultIndex(index).defaultType(type).addAction(actions).build();
        JestResult jestResult = jestClient.execute(bulk);
        if (!jestResult.isSucceeded()) {
            throw new Exception(jestResult.getErrorMessage());
        }
    }

    /**
     * select * from :type where :fieldName = :fieldValue limit :from, :size;
     *
     * @param index
     * @param type
     * @param fieldName
     * @param fieldValue
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> termQuery(String index, String type, int from, int size, String fieldName, String fieldValue) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.termsQuery(fieldName, fieldValue);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getHits(Object.class);
    }

    /**
     * select * from :type where :fieldName = :fieldValue order by :sortField :sortType limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param sortField
     * @param sortType
     * @param fieldName
     * @param fieldValue
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> termQuery(String index, String type, int from, int size, String sortField, boolean sortType, String fieldName, String fieldValue) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.termsQuery(fieldName, fieldValue);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        if (sortType) {
            searchSourceBuilder.sort(sortField, SortOrder.ASC);
        } else {
            searchSourceBuilder.sort(sortField, SortOrder.DESC);
        }
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getHits(Object.class);
    }

    /**
     * select * from :type where :fieldName in :fieldValues limit :from, :size;
     *
     * @param index
     * @param type
     * @param fieldName
     * @param fieldValues
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> termsQuery(String index, String type, int from, int size, String fieldName, String[] fieldValues) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.termsQuery(fieldName, fieldValues);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getHits(Object.class);
    }

    /**
     * select * from :type where :fieldName1 = :fieldValue1 and :fieldName2 = :fieldValue2 and :fieldName3 = :fieldValue3 limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param params
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> boolQuery1(String index, String type, int from, int size, Map<String, Object> params) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            String fieldName = item.getKey();
            Object fieldValue = item.getValue();
            queryBuilder.must(QueryBuilders.termsQuery(fieldName, fieldValue));
        }
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getHits(Object.class);
    }

    /**
     * select * from type where :id = :id
     * @param index
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    public Object findById(String index, String type, String id) throws Exception {
        Get get = new Get.Builder(index, id).type(type).build();
        JestResult result = jestClient.execute(get);
        return result.getSourceAsObject(Object.class);
    }
}
