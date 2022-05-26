package com.maxy.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
 若对象名与数据库表名不一致，则必须添加@TableName注解以指定数据库表与对象之间的映射关系
* */
@TableName("tb_user") // 指定表名
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userName;
    private String password;
    private String name;
    /**
     * @TableField(exist = false) 忽略指定字段的查询或插入
     */
    private Integer age;
    @TableField(fill = FieldFill.INSERT)
    private String email;
}