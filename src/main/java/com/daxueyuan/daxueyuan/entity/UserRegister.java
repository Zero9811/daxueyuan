package com.daxueyuan.daxueyuan.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: Sean
 * @Date: 2019/2/28 22:28
 */
@Data
@Entity
@Table(name = "register_table")
public class UserRegister {
    @Id
    private String account;
    private String password;
}
