package com.sn.cykb.entity;

import lombok.*;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Users {

    private String uniqueId;

    private String nickName;

    private String avatar;

    private Integer gender;

    private String updateTime;
}
