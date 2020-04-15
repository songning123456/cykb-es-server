package com.sn.cykb.elasticsearch.dao;

import com.sn.cykb.elasticsearch.entity.ElasticSearch;
import com.sn.cykb.elasticsearch.entity.Range;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.TermsAggregation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author songning
 * @date 2020/4/15
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
     * @param elasticSearch
     * @param entity
     * @param <T>
     * @throws Exception
     */
    public <T> void save(ElasticSearch elasticSearch, T entity) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType()) || entity == null) {
            throw new Exception("elasticSearch-save 参数异常");
        }
        Index action = new Index.Builder(entity).index(elasticSearch.getIndex()).type(elasticSearch.getType()).build();
        JestResult jestResult = jestClient.execute(action);
        if (!jestResult.isSucceeded()) {
            throw new Exception(jestResult.getErrorMessage());
        }
    }

    /**
     * bulk批量插入
     *
     * @param elasticSearch
     * @param list
     * @param <T>
     * @throws Exception
     */
    public <T> void bulk(ElasticSearch elasticSearch, List<T> list) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType()) || list == null || list.isEmpty()) {
            throw new Exception("elasticSearch-bulk 参数异常");
        }
        List<Index> actionList = new ArrayList<>();
        list.forEach(item -> actionList.add(new Index.Builder(item).build()));
        Bulk bulk = new Bulk.Builder().defaultIndex(elasticSearch.getIndex()).defaultType(elasticSearch.getType()).addAction(actionList).build();
        JestResult jestResult = jestClient.execute(bulk);
        if (!jestResult.isSucceeded()) {
            throw new Exception(jestResult.getErrorMessage());
        }
    }

    /**
     * 根据_id更新
     *
     * @param elasticSearch
     * @param esId
     * @param entity
     * @param <T>
     * @throws Exception
     */
    public <T> void update(ElasticSearch elasticSearch, String esId, T entity) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType()) || StringUtils.isEmpty(esId) || entity == null) {
            throw new Exception("elasticSearch-update 参数异常");
        }
        Index index = new Index.Builder(entity).index(elasticSearch.getIndex()).type(elasticSearch.getType()).id(esId).build();
        JestResult jestResult = jestClient.execute(index);
        if (!jestResult.isSucceeded()) {
            throw new Exception(jestResult.getErrorMessage());
        }
    }

    /**
     * select * from type where esId = :value
     *
     * @param index
     * @param type
     * @param esId
     * @return
     * @throws Exception
     */
    public Object findById(String index, String type, String esId) throws Exception {
        Get get = new Get.Builder(index, esId).type(type).build();
        JestResult result = jestClient.execute(get);
        return result.getSourceAsObject(Object.class);
    }

    /**
     * delete from :type where :termFieldName1 in :termFieldValues1 and :termsFieldName2 in :termsFieldValues2;
     *
     * @param elasticSearch
     * @param termsParams
     * @throws Exception
     */
    public void mustTermsDelete(ElasticSearch elasticSearch, Map<String, String[]> termsParams) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType())) {
            throw new Exception("elasticSearch-mustTermsDelete 参数异常");
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (termsParams != null) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (Map.Entry<String, String[]> item : termsParams.entrySet()) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(item.getKey(), item.getValue()));
            }
            searchSourceBuilder.query(boolQueryBuilder);
        }
        DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(searchSourceBuilder.toString()).addIndex(elasticSearch.getIndex()).addType(elasticSearch.getType()).build();
        JestResult jestResult = jestClient.execute(deleteByQuery);
        if (!jestResult.isSucceeded()) {
            throw new Exception(jestResult.getErrorMessage());
        }
    }

    /**
     * select * from :type where :fieldName1 = :fieldValue1 && :fieldName2 = :fieldValue2 order by :sort :order limit :from, :size;
     *
     * @param elasticSearch
     * @param termParams
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> mustTermRangeQuery(ElasticSearch elasticSearch, Map<String, Object> termParams, List<Range> rangeList) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType())) {
            throw new Exception("elasticSearch-mustTermRangeQuery 参数异常");
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (termParams != null && !termParams.isEmpty()) {
            for (Map.Entry<String, Object> item : termParams.entrySet()) {
                boolQueryBuilder.must(QueryBuilders.termQuery(item.getKey(), item.getValue()));
            }
        }
        if (rangeList != null && !rangeList.isEmpty()) {
            for (Range range : rangeList) {
                if (StringUtils.isEmpty(range.getRangeName()) && (!StringUtils.isEmpty(range.getMin()) || !StringUtils.isEmpty(range.getMax()))) {
                    continue;
                }
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(range.getRangeName());
                if (!StringUtils.isEmpty(range.getMin()) && "gt".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gt(range.getMin());
                } else if (!StringUtils.isEmpty(range.getMin()) && "gte".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gte(range.getMin());
                }
                if (!StringUtils.isEmpty(range.getMax()) && "lt".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gt(range.getMax());
                } else if (!StringUtils.isEmpty(range.getMax()) && "lte".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gte(range.getMax());
                }
                boolQueryBuilder.must(rangeQueryBuilder);
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(elasticSearch.getFrom()).size(elasticSearch.getSize());
        if (StringUtils.isEmpty(elasticSearch.getOrder()) && ("ASC".equals(elasticSearch.getOrder()) || "asc".equals(elasticSearch.getOrder()))) {
            searchSourceBuilder.sort(elasticSearch.getSort(), SortOrder.ASC);
        } else if (StringUtils.isEmpty(elasticSearch.getOrder()) && ("DESC".equals(elasticSearch.getOrder()) || "desc".equals(elasticSearch.getOrder()))) {
            searchSourceBuilder.sort(elasticSearch.getSort(), SortOrder.DESC);
        }
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(elasticSearch.getIndex()).addType(elasticSearch.getType()).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getHits(Object.class);
    }

    /**
     * select * from :type where :fieldName1 in :fieldValues1 && :fieldName2 in :fieldValues2 order by :sort :order limit :from, :size;
     *
     * @param elasticSearch
     * @param termsParams
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> mustTermsRangeQuery(ElasticSearch elasticSearch, Map<String, String[]> termsParams, List<Range> rangeList) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType())) {
            throw new Exception("elasticSearch-mustTermsRangeQuery 参数异常");
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (termsParams != null && !termsParams.isEmpty()) {
            for (Map.Entry<String, String[]> item : termsParams.entrySet()) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(item.getKey(), item.getValue()));
            }
        }
        if (rangeList != null && !rangeList.isEmpty()) {
            for (Range range : rangeList) {
                if (StringUtils.isEmpty(range.getRangeName()) && (!StringUtils.isEmpty(range.getMin()) || !StringUtils.isEmpty(range.getMax()))) {
                    continue;
                }
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(range.getRangeName());
                if (!StringUtils.isEmpty(range.getMin()) && "gt".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gt(range.getMin());
                } else if (!StringUtils.isEmpty(range.getMin()) && "gte".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gte(range.getMin());
                }
                if (!StringUtils.isEmpty(range.getMax()) && "lt".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gt(range.getMax());
                } else if (!StringUtils.isEmpty(range.getMax()) && "lte".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gte(range.getMax());
                }
                boolQueryBuilder.must(rangeQueryBuilder);
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(elasticSearch.getFrom()).size(elasticSearch.getSize());
        if (StringUtils.isEmpty(elasticSearch.getOrder()) && ("ASC".equals(elasticSearch.getOrder()) || "asc".equals(elasticSearch.getOrder()))) {
            searchSourceBuilder.sort(elasticSearch.getSort(), SortOrder.ASC);
        } else if (StringUtils.isEmpty(elasticSearch.getOrder()) && ("DESC".equals(elasticSearch.getOrder()) || "desc".equals(elasticSearch.getOrder()))) {
            searchSourceBuilder.sort(elasticSearch.getSort(), SortOrder.DESC);
        }
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(elasticSearch.getIndex()).addType(elasticSearch.getType()).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getHits(Object.class);
    }

    /**
     * select * from :type where :fieldName1 like concat('%', :fieldValue1, '%') or :fieldName2 like concat('%', :fieldValue2, '%') limit :from, :size;
     *
     * @param elasticSearch
     * @param wildCardParams
     * @return
     * @throws Exception
     */
    public List<SearchResult.Hit<Object, Void>> shouldWildCardQuery(ElasticSearch elasticSearch, Map<String, Object> wildCardParams, List<Range> rangeList) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType())) {
            throw new Exception("elasticSearch-shouldWildCardQuery 参数异常");
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (wildCardParams != null && !wildCardParams.isEmpty()) {
            BoolQueryBuilder innerBoolQueryBuilder = QueryBuilders.boolQuery();
            for (Map.Entry<String, Object> item : wildCardParams.entrySet()) {
                innerBoolQueryBuilder.should(QueryBuilders.wildcardQuery(item.getKey(), "*" + item.getValue() + "*"));
            }
            boolQueryBuilder.must(innerBoolQueryBuilder);
        }
        if (rangeList != null && !rangeList.isEmpty()) {
            for (Range range : rangeList) {
                if (StringUtils.isEmpty(range.getRangeName()) && (!StringUtils.isEmpty(range.getMin()) || !StringUtils.isEmpty(range.getMax()))) {
                    continue;
                }
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(range.getRangeName());
                if (!StringUtils.isEmpty(range.getMin()) && "gt".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gt(range.getMin());
                } else if (!StringUtils.isEmpty(range.getMin()) && "gte".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gte(range.getMin());
                }
                if (!StringUtils.isEmpty(range.getMax()) && "lt".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gt(range.getMax());
                } else if (!StringUtils.isEmpty(range.getMax()) && "lte".equals(range.getGtOrGte())) {
                    rangeQueryBuilder.gte(range.getMax());
                }
                boolQueryBuilder.must(rangeQueryBuilder);
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(elasticSearch.getFrom()).size(elasticSearch.getSize());
        if (StringUtils.isEmpty(elasticSearch.getOrder()) && ("ASC".equals(elasticSearch.getOrder()) || "asc".equals(elasticSearch.getOrder()))) {
            searchSourceBuilder.sort(elasticSearch.getSort(), SortOrder.ASC);
        } else if (StringUtils.isEmpty(elasticSearch.getOrder()) && ("DESC".equals(elasticSearch.getOrder()) || "desc".equals(elasticSearch.getOrder()))) {
            searchSourceBuilder.sort(elasticSearch.getSort(), SortOrder.DESC);
        }
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(elasticSearch.getIndex()).addType(elasticSearch.getType()).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getHits(Object.class);
    }

    public List<TermsAggregation.Entry> aggregationTermQuery(ElasticSearch elasticSearch, Map<String, Object> termParams, String aggField) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType())) {
            throw new Exception("elasticSearch-aggregationTermQuery 参数异常");
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (termParams != null && !termParams.isEmpty()) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (Map.Entry<String, Object> item : termParams.entrySet()) {
                boolQueryBuilder.must(QueryBuilders.termQuery(item.getKey(), item.getValue()));
            }
            searchSourceBuilder.query(boolQueryBuilder);
        }
        searchSourceBuilder.aggregation(AggregationBuilders.terms(aggField + "Agg").field(aggField).size(elasticSearch.getSize()));
        searchSourceBuilder.from(elasticSearch.getFrom()).size(elasticSearch.getSize());
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(elasticSearch.getIndex()).addType(elasticSearch.getType()).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getAggregations().getTermsAggregation(aggField + "Agg").getBuckets();
    }

    public List<TermsAggregation.Entry> aggregationSubTermQuery(ElasticSearch elasticSearch, Map<String, Object> termParams, String aggField, String subAggField) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType())) {
            throw new Exception("elasticSearch-aggregationSubTermQuery 参数异常");
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (termParams != null && !termParams.isEmpty()) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (Map.Entry<String, Object> item : termParams.entrySet()) {
                boolQueryBuilder.must(QueryBuilders.termQuery(item.getKey(), item.getValue()));
            }
            searchSourceBuilder.query(boolQueryBuilder);
        }
        searchSourceBuilder.aggregation(AggregationBuilders.terms(aggField + "Agg").field(aggField).size(elasticSearch.getSize())
                .subAggregation(AggregationBuilders.terms(subAggField + "SubAgg").field(subAggField).size(elasticSearch.getSize())));
        searchSourceBuilder.from(elasticSearch.getFrom()).size(elasticSearch.getSize());
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(elasticSearch.getIndex()).addType(elasticSearch.getType()).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getAggregations().getTermsAggregation(aggField + "Agg").getBuckets();
    }

    public List<TermsAggregation.Entry> aggregationTermCountQuery(ElasticSearch elasticSearch, Map<String, Object> termParams, String aggField) throws Exception {
        if (StringUtils.isEmpty(elasticSearch.getIndex()) || StringUtils.isEmpty(elasticSearch.getType())) {
            throw new Exception("elasticSearch-aggregationSubCountQuery 参数异常");
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (termParams != null && !termParams.isEmpty()) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (Map.Entry<String, Object> item : termParams.entrySet()) {
                boolQueryBuilder.must(QueryBuilders.termQuery(item.getKey(), item.getValue()));
            }
            searchSourceBuilder.query(boolQueryBuilder);
        }
        searchSourceBuilder.aggregation(AggregationBuilders.terms(aggField + "Agg").field(aggField).size(elasticSearch.getSize())
                .order(Terms.Order.aggregation("total", false))
                .subAggregation(AggregationBuilders.count("total").field(aggField)));
        searchSourceBuilder.from(elasticSearch.getFrom()).size(elasticSearch.getSize());
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query).addIndex(elasticSearch.getIndex()).addType(elasticSearch.getType()).build();
        SearchResult searchResult = jestClient.execute(search);
        return searchResult.getAggregations().getTermsAggregation(aggField + "Agg").getBuckets();
    }
}
