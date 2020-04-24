package com.sn.cykb.entity;

import lombok.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:38
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Relation {

    private String usersId;

    private String novelsId;

    private String updateTime;
}
