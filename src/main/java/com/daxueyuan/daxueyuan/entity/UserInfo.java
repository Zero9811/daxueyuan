package com.daxueyuan.daxueyuan.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: Sean
 * @Date: 2019/2/28 22:53
 */
@Data
@Entity
@Table(name = "User_table")
public class UserInfo {
    @Id
    private String account;
    private String headPortraid;
    private String nickName;
    private String email;
    private String sex;
    private String studentState;
}
