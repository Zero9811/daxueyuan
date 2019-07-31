package com.daxueyuan.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: Sean
 * @Date: 2019/2/28 22:28
 */
@Data
@Entity
@Table(name = "register_table")
public class UserRegister implements Serializable {
    @Id
    private String account;
    private String password;
}
