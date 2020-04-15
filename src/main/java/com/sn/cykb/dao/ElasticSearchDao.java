package com.sn.cykb.dao;

import com.sn.cykb.entity.Users;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.TermsAggregation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
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
     * select * from :type order by :sortField :sortType limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param sortField
     * @param sortType
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> termQuery(String index, String type, int from, int size, String sortField, boolean sortType) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
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

    public List<SearchResult.Hit<Object, Void>> termQuery(String index, String type, int from, int size, Map<String, Object> params, String sortField, boolean sortType) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(item.getKey(), item.getValue()));
        }
        searchSourceBuilder.query(boolQueryBuilder);
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
     * select * from :type where :fieldName in :fieldValues order by :sortField :sortType limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param fieldName
     * @param fieldValues
     * @param sortField
     * @param sortType
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> termsQuery(String index, String type, int from, int size, String fieldName, String[] fieldValues, String sortField, boolean sortType) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.termsQuery(fieldName, fieldValues);
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
    public List<SearchResult.Hit<Object, Void>> boolTermsQuery(String index, String type, int from, int size, Map<String, Object> params) throws Exception {
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
     * select * from :type where :fieldName1 = :fieldValue1 and :fieldName2 = :fieldValue2 and :fieldName3 = :fieldValue3 order by :sortField :sortType limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param sortField
     * @param sortType
     * @param params
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> boolTermsQuery(String index, String type, int from, int size, String sortField, Boolean sortType, Map<String, Object> params) throws Exception {
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
     * select * from :type where :fieldName1 = :fieldValue1 and :fieldName2 = :fieldValue2 and :fieldName3 = :fieldValue3 and :fieldName > :min and :fieldName < :max order by :sortField :sortType limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param sortField
     * @param sortType
     * @param params
     * @param fieldName
     * @param range
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> boolTermsRangeQuery(String index, String type, int from, int size, String sortField, Boolean sortType, Map<String, Object> params, String fieldName, Map<String, Object> range) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        List<TermsQueryBuilder> termsQueryBuilderList = new ArrayList<>();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            termsQueryBuilderList.add(QueryBuilders.termsQuery(key, value));
        }
        queryBuilder.must().addAll(termsQueryBuilderList);
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(fieldName);
        Object gt = range.get("gt");
        Object lt = range.get("lt");
        Object gte = range.get("gte");
        Object lte = range.get("lte");
        if (gt != null) {
            rangeQueryBuilder.gt(gt);
        } else if (gte != null) {
            rangeQueryBuilder.gte(gte);
        }
        if (lt != null) {
            rangeQueryBuilder.lt(lt);
        } else if (lte != null) {
            rangeQueryBuilder.lte(lte);
        }
        queryBuilder.must(rangeQueryBuilder);
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
     * select * from :type where :fieldName1 like concat('%', :fieldValue1, '%') or :fieldName2 like concat('%', :fieldValue2, '%') limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param params
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> boolShouldWildCardQuery(String index, String type, int from, int size, Map<String, Object> params) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        List<WildcardQueryBuilder> wildcardQueryBuilderList = new ArrayList<>();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            wildcardQueryBuilderList.add(QueryBuilders.wildcardQuery(key, "*" + value + "*"));
        }
        queryBuilder.should().addAll(wildcardQueryBuilderList);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getHits(Object.class);
    }

    /**
     * select * from :type where :fieldName1 like concat('%', :fieldValue1, '%') or :fieldName2 like concat('%', :fieldValue2, '%') order by :sortField :sortType limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param params
     * @param sortField
     * @param sortType
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> boolShouldWildCardQuery(String index, String type, int from, int size, Map<String, Object> params, String sortField, boolean sortType) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        List<WildcardQueryBuilder> wildcardQueryBuilderList = new ArrayList<>();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            wildcardQueryBuilderList.add(QueryBuilders.wildcardQuery(key, "*" + value + "*"));
        }
        queryBuilder.should().addAll(wildcardQueryBuilderList);
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
     * select * from :type where (:wildCardFieldName1 like concat('%', :wildCardFieldValue1, '%') or :wildCardFieldName2 like concat('%', :wildCardFieldValue2, '%')) and rangeFieldName < :rangeFieldValue  order by sortField desc limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param rangeFieldName
     * @param rangeParams
     * @param shouldWildCardParams
     * @param sortField
     * @param sortType
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> boolMustShouldWildCardRangeQuery(String index, String type, int from, int size, String rangeFieldName, Map<String, Object> rangeParams, Map<String, Object> shouldWildCardParams, String sortField, boolean sortType) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder shouldQueryBuilder = QueryBuilders.boolQuery();
        List<WildcardQueryBuilder> wildcardQueryBuilderList = new ArrayList<>();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(rangeFieldName);
        for (Map.Entry<String, Object> item : shouldWildCardParams.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            wildcardQueryBuilderList.add(QueryBuilders.wildcardQuery(key, "*" + value + "*"));
        }
        shouldQueryBuilder.should().addAll(wildcardQueryBuilderList);
        boolQueryBuilder.must().add(shouldQueryBuilder);
        Object gt = rangeParams.get("gt");
        Object lt = rangeParams.get("lt");
        Object gte = rangeParams.get("gte");
        Object lte = rangeParams.get("lte");
        if (gt != null) {
            rangeQueryBuilder.gt(gt);
        } else if (gte != null) {
            rangeQueryBuilder.gte(gte);
        }
        if (lt != null) {
            rangeQueryBuilder.lt(lt);
        } else if (lte != null) {
            rangeQueryBuilder.lte(lte);
        }
        boolQueryBuilder.must().add(rangeQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
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
     * select * from type where :id = :id
     *
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

    /**
     * select * from :type where :fieldName > :min and :fieldName < :max order by :sortField :sortType limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param fieldName
     * @param range
     * @param sortField
     * @param sortType
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> rangeQuery(String index, String type, int from, int size, String fieldName, Map<String, Object> range, String sortField, boolean sortType) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(fieldName);
        Object gt = range.get("gt");
        Object lt = range.get("lt");
        Object gte = range.get("gte");
        Object lte = range.get("lte");
        if (gt != null) {
            rangeQueryBuilder.gt(gt);
        } else if (gte != null) {
            rangeQueryBuilder.gte(gte);
        }
        if (lt != null) {
            rangeQueryBuilder.lt(lt);
        } else if (lte != null) {
            rangeQueryBuilder.lte(lte);
        }
        searchSourceBuilder.query(rangeQueryBuilder);
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
     * select count(*), :fieldName from :type group by :fieldName
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param aggFieldName
     * @return
     * @throws Exception
     */
    public List<TermsAggregation.Entry> aggregationQuery(String index, String type, int from, int size, String aggFieldName) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(AggregationBuilders.terms(aggFieldName + "Agg").field(aggFieldName).size(size));
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getAggregations().getTermsAggregation(aggFieldName + "Agg").getBuckets();
    }

    /**
     * select count(*), :fieldName from :type where :conditionFieldName = :conditionFieldValue group by :fieldName
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param aggFieldName
     * @param conditionFieldName
     * @param conditionFieldValue
     * @return
     * @throws Exception
     */
    public List<TermsAggregation.Entry> aggregationQuery(String index, String type, int from, int size, String aggFieldName, String conditionFieldName, String conditionFieldValue) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termsQuery(conditionFieldName, conditionFieldValue));
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.aggregation(AggregationBuilders.terms(aggFieldName + "Agg").field(aggFieldName).size(size));
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getAggregations().getTermsAggregation(aggFieldName + "Agg").getBuckets();
    }

    /**
     * select count(*), :aggFieldName, :termsFieldName from :type group by :aggFieldName
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param aggFieldName
     * @param termsFieldName
     * @return
     * @throws Exception
     */
    public List<TermsAggregation.Entry> aggregationSubQuery(String index, String type, int from, int size, String aggFieldName, String termsFieldName) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(AggregationBuilders.terms(aggFieldName + "Agg").field(aggFieldName).size(size)
                .subAggregation(AggregationBuilders.terms(termsFieldName + "SubAgg").field(termsFieldName).size(size)));
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getAggregations().getTermsAggregation(aggFieldName + "Agg").getBuckets();
    }

    /**
     * select :aggFieldName, count(:aggFieldName) as total from :type group by :aggFieldName order by total desc limit :from, :size;
     *
     * @param index
     * @param type
     * @param from
     * @param size
     * @param aggFieldName
     * @return
     * @throws Exception
     */
    public List<TermsAggregation.Entry> aggregationSubCountQuery(String index, String type, int from, int size, String aggFieldName) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(AggregationBuilders.terms(aggFieldName + "Agg").field(aggFieldName).size(size)
                .order(Terms.Order.aggregation("total", false))
                .subAggregation(AggregationBuilders.count("total").field(aggFieldName)));
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getAggregations().getTermsAggregation(aggFieldName + "Agg").getBuckets();
    }

    /**
     * delete from :type where :termFieldName = :termFieldValue
     *
     * @param index
     * @param type
     * @param params
     * @return
     * @throws Exception
     */
    public boolean deleteByFieldNames(String index, String type, Map<String, Object> params) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            boolQueryBuilder.must(QueryBuilders.termQuery(item.getKey(), item.getValue()));
        }
        searchSourceBuilder.query(boolQueryBuilder);
        DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(searchSourceBuilder.toString()).addIndex(index).addType(type).build();
        JestResult searchResult = jestClient.execute(deleteByQuery);
        if (!searchResult.isSucceeded()) {
            return false;
        }
        return true;
    }

    /**
     * delete from :type where :termFieldName = :termFieldValue and :termsFieldName in :termsFieldNames
     *
     * @param index
     * @param type
     * @param termParams
     * @param termsParams
     * @return
     * @throws Exception
     */
    public boolean deleteByFieldNames(String index, String type, Map<String, Object> termParams, Map<String, String[]> termsParams) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> item : termParams.entrySet()) {
            boolQueryBuilder.must(QueryBuilders.termQuery(item.getKey(), item.getValue()));
        }
        for (Map.Entry<String, String[]> item : termsParams.entrySet()) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(item.getKey(), item.getValue()));
        }
        searchSourceBuilder.query(boolQueryBuilder);
        DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(searchSourceBuilder.toString()).addIndex(index).addType(type).build();
        JestResult searchResult = jestClient.execute(deleteByQuery);
        if (!searchResult.isSucceeded()) {
            return false;
        }
        return true;
    }

    public boolean updateIndexDoc(String indexName, String indexType, String id, Users users) throws Exception {
        Index index = new Index.Builder(users).index(indexName).type(indexType).id(id).build();
        JestResult jr = null;
        boolean bool = false;
        jr = jestClient.execute(index);
        bool = jr.isSucceeded();
        return bool;
    }
}
