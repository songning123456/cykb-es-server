package com.sn.cykb.entity;

import lombok.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:44
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Comments {

    private String content;

    private String contact;

    private String updateTime;
}
