package com.sn.cykb.dto;

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
public class UsersDTO {
    private Object usersId;

    private Object uniqueId;

    private Object nickName;

    private Object avatar;

    private Object gender;
}
