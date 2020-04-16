package com.sn.cykb.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/9 23:03
 */
@Data
public class UsersNovelsRelationVO {

    private String uniqueId;

    private String novelsId;

    private String chaptersId;

    /**
     * recentRead; updateTime
     */
    private String sortType;

    private List<String> novelsIdList;
}
