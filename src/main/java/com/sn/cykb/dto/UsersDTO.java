package com.sn.cykb.dto;

import lombok.Data;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
@Data
public class UsersDTO {
    private String uniqueId;

    private String nickName;

    private String avatar;

    private Integer gender;
}
