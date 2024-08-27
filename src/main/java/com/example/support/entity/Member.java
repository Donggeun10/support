package com.example.support.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Table(name="TB_MEMBER")
@ToString(exclude = "password")
public class Member {

    @Id
    private String memberId;

    private String password;
}
