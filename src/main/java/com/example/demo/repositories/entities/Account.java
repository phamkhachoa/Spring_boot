package com.example.demo.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Getter
@Setter
public class Account {

    @Id
    private String id;
    private Integer status;
    private String email;
    private Long updated;
    private String fullname;
    private String phone;
    private String updatedBy;
    private String createdBy;
    private String salt;
    private String password;
}

