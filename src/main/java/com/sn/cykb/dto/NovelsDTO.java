package com.sn.cykb.dto;

import lombok.Data;

/**
 * @author: songning
 * @date: 2020/3/9 23:04
 */
@Data
public class NovelsDTO {

    private String novelsId;

    private String title;

    private String author;

    private String category;

    private String introduction;

    private String latestChapter;

    private String coverUrl;

    private Integer total;

    private Long createTime;

    private String sourceUrl;

    private String sourceName;
}
