package com.sn.cykb.elasticsearch.entity;

import lombok.*;

/**
 * @author songning
 * @date 2020/4/15
 * description
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class ElasticSearch {

    private String index;

    private String type;

    private Integer from;

    private Integer size;

    private String sort;

    private String order;

    public ElasticSearch() {
        this.from = 0;
        this.size = 10;
    }
}
