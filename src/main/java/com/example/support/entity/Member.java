package com.example.support.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name="TB_MEMBER")
public class Member {

    @Id
    private String memberId;

    private String password;
}
