package com.wjc.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    // 对应username字段
    private String username;
    // 对应password字段
    private String password;
    // 对应email字段
    private String email;
    // 对应created字段（日期类型）
    private Date created;
    // 对应valid字段（tinyint(1)，用布尔类型映射）
    private Boolean valid;
}
